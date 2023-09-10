package dev.ogabek.durak.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.ogabek.durak.model.Card
import dev.ogabek.durak.model.CardType
import dev.ogabek.durak.model.OnBoardCard
import dev.ogabek.durak.model.Player
import dev.ogabek.durak.utils.allCardPack
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
@SuppressLint("StaticFieldLeak")
class PlayViewModel @Inject constructor (
    private val database: DatabaseReference
): ViewModel() {

    private val _isWaiting = mutableStateOf(false)
    val isWaiting = _isWaiting

    private val _gameId = mutableStateOf("")
    val gameId = _gameId

    private val _error = mutableStateOf(false)
    val error = _error

    private val _errorMessage = mutableStateOf("")
    val errorMessage = _errorMessage

    fun loadGame(gameId: String, isNew: Boolean) = viewModelScope.launch {
        _gameId.value = UUID.randomUUID().toString().take(5)
        _isWaiting.value = !isNew
        database.child(gameId).child("players").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.hasChild("secondPlayer")) {
                    _isWaiting.value = false
                } else if (!snapshot.hasChild("firstPlayer")) {
                    if (isNew) {
                        snapshot.child("firstPlayer").ref.setValue(gameId)
                        _isWaiting.value = true
                    }
                } else if (snapshot.hasChild("firstPlayer") && !snapshot.hasChild("secondPlayer")) {
                    if (!isNew)
                        snapshot.child("secondPlayer").ref.setValue(gameId)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                _error.value = true
                _errorMessage.value = error.message
            }

        })
    }

    private val _isFinish = mutableStateOf(false)
    val isFinish = _isFinish

    private val _whoWon = mutableStateOf("")
    val whoWon = _whoWon

    private val _firstPlayer = mutableStateListOf<Card>()
    val firstPlayer = _firstPlayer

    private val _secondPlayer = mutableStateListOf<Card>()
    val secondPlayer = _secondPlayer

    private val _onBoardCard = mutableStateListOf<OnBoardCard>()
    val onBoardCard = _onBoardCard

    private val _cards = mutableStateOf(0)
    val cards = _cards

    private val _mainCard = mutableStateOf<Card?>(null)
    val mainCard = _mainCard

    private val _mainType = mutableStateOf<CardType?>(null)
    val mainType = _mainType

    fun setDatabase(gameId: String) = viewModelScope.launch {

        database.child(gameId).child("won").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val won = snapshot.value as String
                    if (won.isNotEmpty()) {
                        _isFinish.value = true
                        _whoWon.value = won
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                _error.value = true
                _errorMessage.value = error.message
            }
        })

        database.child(gameId).child("game").child("firstPlayer").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                _firstPlayer.clear()
                for (i in snapshot.children) {
                    val id = snapshot.child("id").value as String
                    val code = snapshot.child("code").value as Long
                    val type = enumValueOf(snapshot.child("type").value as String) as CardType
                    val card = Card(id, code.toInt(), type)
                    _firstPlayer.add(card)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                _error.value = true
                _errorMessage.value = error.message
            }
        })

        database.child(gameId).child("game").child("secondPlayer").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                _secondPlayer.clear()
                for (i in snapshot.children) {
                    val id = snapshot.child("id").value as String
                    val code = snapshot.child("code").value as Long
                    val type = enumValueOf(snapshot.child("type").value as String) as CardType
                    val card = Card(id, code.toInt(), type)
                    _secondPlayer.add(card)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                _error.value = true
                _errorMessage.value = error.message
            }
        })

        database.child(gameId).child("game").child("onBoard").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (i in snapshot.children) {

                    var fCard: Card? = null
                    var sCard: Card? = null

                    if (snapshot.hasChild("firstCard")) {
                        val firstId = snapshot.child("firstCard/id").value as String
                        val firstCode = snapshot.child("firstCard/code").value as Long
                        val firstType = enumValueOf(snapshot.child("firstCard/type").value as String) as CardType
                        val firstCard = Card(firstId, firstCode.toInt(), firstType)
                        fCard = firstCard
                        if (snapshot.hasChild("secondCard")) {
                            val secondId = snapshot.child("secondCard/id").value as String
                            val secondCode = snapshot.child("secondCard/code").value as Long
                            val secondType = enumValueOf(snapshot.child("secondCard/type").value as String) as CardType
                            val secondCard = Card(secondId, secondCode.toInt(), secondType)
                            sCard = secondCard
                        }
                    }

                    _onBoardCard.add(OnBoardCard(fCard, sCard))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                _error.value = true
                _errorMessage.value = error.message
            }
        })

        database.child(gameId).child("game").child("cards").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                _cards.value = snapshot.childrenCount.toInt()
            }

            override fun onCancelled(error: DatabaseError) {
                _error.value = true
                _errorMessage.value = error.message
            }
        })

        database.child(gameId).child("game").child("mainType").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                _mainType.value = enumValueOf(snapshot.value as String) as CardType
            }

            override fun onCancelled(error: DatabaseError) {
                _error.value = true
                _errorMessage.value = error.message
            }
        })

        database.child(gameId).child("game").child("mainCard").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val id = snapshot.child("id").value as String
                val code = snapshot.child("code").value as Long
                val type = enumValueOf(snapshot.child("type").value as String) as CardType
                val card = Card(id, code.toInt(), type)
                _mainCard.value = card
            }

            override fun onCancelled(error: DatabaseError) {
                _error.value = true
                _errorMessage.value = error.message
            }
        })

    }

    fun startGame(gameId: String) = viewModelScope.launch {
        val cards = allCardPack()
        val firstPlayer = cards.take(6)
        cards.removeAll(firstPlayer.toSet())
        val secondPlayer = cards.take(6)
        cards.removeAll(secondPlayer.toSet())

        val mCard = cards.last()

        database.child(gameId).child("game").addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.ref.removeValue().addOnCompleteListener {
                    snapshot.child("cards").ref.setValue(cards)
                    snapshot.child("mainCard").ref.setValue(mCard)
                    snapshot.child("mainType").ref.setValue(mCard.type)
                    snapshot.child("firstPlayer").ref.setValue(firstPlayer)
                    snapshot.child("secondPlayer").ref.setValue(secondPlayer)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                _error.value = true
                _errorMessage.value = error.message
            }
        })
    }


}