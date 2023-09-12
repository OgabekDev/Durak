package dev.ogabek.durak.viewmodel

import android.annotation.SuppressLint
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
import dev.ogabek.durak.utils.allCardPack
import dev.ogabek.durak.utils.check
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
@SuppressLint("StaticFieldLeak")
class PlayViewModel @Inject constructor(
    private val database: DatabaseReference
) : ViewModel() {

    private val _isPlaying = mutableStateOf(false)
    val isPlaying = _isPlaying

    private val _isWaiting = mutableStateOf(true)
    val isWaiting = _isWaiting

    private val _playerId = mutableStateOf("")
    val playerId = _playerId

    private val _opponentId = mutableStateOf("")
    private val opponentId = _opponentId

    private var myOpponent: Int = 0

    private val _error = mutableStateOf(false)
    val error = _error

    private val _errorMessage = mutableStateOf("")
    val errorMessage = _errorMessage

    private val _playerTurn = mutableStateOf("")
    val playerTurn = _playerTurn

    fun loadGame(gameId: String, isNew: Boolean) = viewModelScope.launch {
        _playerId.value = UUID.randomUUID().toString().take(5)
        _isWaiting.value = !isNew
        database.child(gameId).child("players").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.hasChild("secondPlayer")) {
                    if (isNew) {
                        _opponentId.value = snapshot.child("secondPlayer").value as String
                        myOpponent = 2
                    } else {
                        _opponentId.value = snapshot.child("firstPlayer").value as String
                        myOpponent = 1
                    }
                    _isWaiting.value = false
                } else if (!snapshot.hasChild("firstPlayer")) {
                    if (isNew) {
                        snapshot.child("firstPlayer").ref.setValue(playerId.value)
                        _isWaiting.value = true
                    }
                } else if (snapshot.hasChild("firstPlayer") && !snapshot.hasChild("secondPlayer")) {
                    if (!isNew)
                        snapshot.child("secondPlayer").ref.setValue(playerId.value)
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

    private val _whoWon = mutableStateOf<Boolean?>(null)
    val whoWon = _whoWon

    private val _draw = mutableStateOf(false)
    val draw = _draw

    private val _firstPlayer = mutableStateListOf<Card>()
    val firstPlayer = _firstPlayer

    private val _secondPlayer = mutableStateListOf<Card>()
    val secondPlayer = _secondPlayer

    private val _onBoardCard = mutableStateListOf<OnBoardCard>()
    val onBoardCard = _onBoardCard

    private val _cards = mutableStateListOf<Card>()
    val cards = _cards

    private val _mainCard = mutableStateOf<Card?>(null)
    val mainCard = _mainCard

    private val _mainType = mutableStateOf<CardType?>(null)
    private val mainType = _mainType

    // Buttons action
    private val _isTake = mutableStateOf(false)
    val isTake = _isTake

    private val _isPass = mutableStateOf(false)
    val isPass = _isPass

    private val _isBat = mutableStateOf(false)
    val isBat = _isBat

    private var whoTake: String = ""

    fun setDatabase(gameId: String, isNew: Boolean) = viewModelScope.launch {

        database.child(gameId).child("won").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val won = snapshot.value as String
                    if (won.isNotEmpty()) {
                        _isFinish.value = true
                        if (won == "draw") {
                            _draw.value = true
                        } else {
                            _whoWon.value = won == playerId.value
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                _errorMessage.value = error.message
                _error.value = true
            }
        })

        database.child(gameId).child("game").child("firstPlayer")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    _firstPlayer.clear()
                    for (i in snapshot.children) {
                        val id = i.child("id").value as String
                        val code = i.child("code").value as Long
                        val type = enumValueOf(i.child("type").value as String) as CardType
                        val card = Card(id, code.toInt(), type)
                        _firstPlayer.add(card)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    _errorMessage.value = error.message
                    _error.value = true
                }
            })

        database.child(gameId).child("game").child("secondPlayer")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    _secondPlayer.clear()
                    for (i in snapshot.children) {
                        val id = i.child("id").value as String
                        val code = i.child("code").value as Long
                        val type = enumValueOf(i.child("type").value as String) as CardType
                        val card = Card(id, code.toInt(), type)
                        _secondPlayer.add(card)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    _errorMessage.value = error.message
                    _error.value = true
                }
            })

        database.child(gameId).child("game").child("onBoard")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    _onBoardCard.clear()
                    for (i in snapshot.children) {

                        var fCard: Card? = null
                        var sCard: Card? = null

                        if (i.hasChild("firstCard")) {
                            val firstId = i.child("firstCard/id").value as String
                            val firstCode = i.child("firstCard/code").value as Long
                            val firstType =
                                enumValueOf(i.child("firstCard/type").value as String) as CardType
                            val firstCard = Card(firstId, firstCode.toInt(), firstType)
                            fCard = firstCard
                            if (i.hasChild("secondCard")) {
                                val secondId = i.child("secondCard/id").value as String
                                val secondCode = i.child("secondCard/code").value as Long
                                val secondType =
                                    enumValueOf(i.child("secondCard/type").value as String) as CardType
                                val secondCard = Card(secondId, secondCode.toInt(), secondType)
                                sCard = secondCard
                            }
                        } else {
                            _isTake.value = false
                            _isBat.value = false
                        }

                        _onBoardCard.add(OnBoardCard(fCard, sCard))
                    }

                    if (onBoardCard.isNotEmpty()) {
                        val last = onBoardCard.last()
                        if (last.secondCard == null) {
                            _isTake.value = playerTurn.value != playerId.value
                            _isBat.value = false
                        } else {
                            _isTake.value = false
                            _isBat.value = playerTurn.value != playerId.value
                        }
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    _errorMessage.value = error.message
                    _error.value = true
                }
            })

        database.child(gameId).child("game").child("whoTake")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.value != null) {
                        _isPass.value = (snapshot.value as String) == opponentId.value
                    } else {
                        _isTake.value = false
                        _isPass.value = false
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    _errorMessage.value = error.message
                    _error.value = true
                }

            })

        database.child(gameId).child("game").child("cards")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    _cards.clear()
                    for (i in snapshot.children) {
                        val id = i.child("id").value as String
                        val code = i.child("code").value as Long
                        val type = enumValueOf(i.child("type").value as String) as CardType
                        val card = Card(id, code.toInt(), type)
                        _cards.add(card)
                    }
                    _mainCard.value = if (cards.isEmpty())
                        null
                    else
                        cards.last()
                }

                override fun onCancelled(error: DatabaseError) {
                    _errorMessage.value = error.message
                    _error.value = true
                }
            })

        database.child(gameId).child("game").child("mainType")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.value != null)
                        _mainType.value = enumValueOf(snapshot.value as String) as CardType
                }

                override fun onCancelled(error: DatabaseError) {
                    _errorMessage.value = error.message
                    _error.value = true
                }
            })

        database.child(gameId).child("game").child("playerTurn")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.value != null)
                        _playerTurn.value = snapshot.value as String
                }

                override fun onCancelled(error: DatabaseError) {
                    _errorMessage.value = error.message
                    _error.value = true
                }

            })

        database.child(gameId).child("game/whoTake").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.value
                if (value != null) {
                    if (value.toString() == playerId.value || value == opponentId.value) {
                        whoTake = value.toString()
                    }
                    if (value.toString() == opponentId.value) {
                        database.child(gameId).child("game/playerTurn").ref.setValue(playerId.value)
                    } else if (value.toString() == "done" && whoTake == playerId.value) {
                        takeCards(gameId, isNew)
                        _isPass.value = false
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                _errorMessage.value = error.message
                _error.value = true
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

        database.child(gameId).child("game")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.ref.removeValue().addOnCompleteListener {
                        snapshot.child("cards").ref.setValue(cards)
                        snapshot.child("mainCard").ref.setValue(mCard)
                        snapshot.child("mainType").ref.setValue(mCard.type)
                        snapshot.child("firstPlayer").ref.setValue(firstPlayer)
                        snapshot.child("secondPlayer").ref.setValue(secondPlayer)
                        snapshot.child("playerTurn").ref.setValue(playerId.value)
                        _isPlaying.value = true
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    _errorMessage.value = error.message
                    _error.value = true
                }
            })
    }

    fun cardClick(gameId: String, card: Card, isNew: Boolean) = viewModelScope.launch {
        val gameDB = database.child(gameId).child("game")

        val player = isNew.check(_firstPlayer, _secondPlayer)
        val playerStr = isNew.check("firstPlayer", "secondPlayer")

        val onBoard = _onBoardCard

        val new = onBoard.isEmpty()
        val isCardMain = card.type == mainType.value

        var isCardHave = false
        for (i in onBoard)
            if (i.firstCard!!.code == card.code || i.secondCard?.code == card.code) {
                isCardHave = true
                break
            }

        if (playerTurn.value == playerId.value) {
            if (isPass.value && isCardHave) {
                val boardCard = OnBoardCard(
                    firstCard = card,
                    null
                )
                onBoard.add(boardCard)
                player.remove(card)

                gameDB.child(playerStr).ref.setValue(player)
                gameDB.child("onBoard").ref.setValue(onBoard)
                isWin(gameId, isNew)
            } else if (new && !isPass.value) {
                val boardCard = OnBoardCard(
                    firstCard = card,
                    null
                )
                onBoard.add(boardCard)
                player.remove(card)

                gameDB.child(playerStr).ref.setValue(player)
                gameDB.child("onBoard").ref.setValue(onBoard)
                gameDB.child("playerTurn").ref.setValue(opponentId.value)
            } else if (!isPass.value) {
                if (onBoard.last().secondCard == null) {
                    val isOnBoardCardMain = onBoard.last().firstCard!!.type == mainType.value
                    val isPossible =
                        ((onBoard.last().firstCard!!.type == card.type) && (onBoard.last().firstCard!!.code < card.code)) || ((card.type == mainType.value) && (onBoard.last().firstCard!!.code + isOnBoardCardMain.check(10, 0) < card.code + isCardMain.check(10, 0)))
                    if (isPossible) {
                        onBoard.last().secondCard = card
                        player.remove(card)

                        gameDB.child(playerStr).ref.setValue(player)
                        gameDB.child("onBoard").ref.setValue(onBoard)
                        gameDB.child("playerTurn").ref.setValue(opponentId.value)
                        isWin(gameId, isNew)
                    }
                } else {
                    if (isCardHave) {
                        val boardCard = OnBoardCard(
                            firstCard = card,
                            null
                        )
                        onBoard.add(boardCard)
                        player.remove(card)

                        gameDB.child(playerStr).ref.setValue(player)
                        gameDB.child("onBoard").ref.setValue(onBoard)
                        gameDB.child("playerTurn").ref.setValue(opponentId.value)
                    }
                }
            }
        }
    }

    private fun isWin(gameId: String, isNew: Boolean) = viewModelScope.launch {
        val gameDB = database.child(gameId)
        if (firstPlayer.size == 0 && secondPlayer.size == 0) {
            gameDB.child("won").ref.setValue("draw")
        } else if (firstPlayer.size == 0) {
            gameDB.child("won").ref.setValue(isNew.check(playerId.value, opponentId.value))
        } else if (secondPlayer.size == 0) {
            gameDB.child("won").ref.setValue(isNew.check(playerId.value, opponentId.value))
        }
    }

    fun closeError() = viewModelScope.launch {
        _error.value = false
    }

    fun iBat(gameId: String) = viewModelScope.launch {
        val gameDB = database.child(gameId).child("game")

        val onBoard = _onBoardCard
        onBoard.clear()

        takeCardsFromPack(gameId)

        gameDB.child("onBoard").ref.setValue(onBoard)
        gameDB.child("playerTurn").ref.setValue(opponentId.value)
        _isBat.value = false

    }

    private fun takeCardsFromPack(gameId: String) = viewModelScope.launch {

        val gameDB = database.child(gameId).child("game")

        val forFirst = firstPlayer
        val forSecond = secondPlayer
        val mCards = cards

        if ((firstPlayer.size < 6) && (mCards.size >= 6 - firstPlayer.size)) {
            val temp = mCards.take(6 - firstPlayer.size)
            forFirst.addAll(temp)
            mCards.removeAll(temp)
        } else if (firstPlayer.size < 6) {
            forFirst.addAll(mCards)
            mCards.clear()
        }

        if ((secondPlayer.size < 6) && (mCards.size >= 6 - secondPlayer.size)) {
            val temp = mCards.take(6 - secondPlayer.size)
            forSecond.addAll(temp)
            mCards.removeAll(temp)
        } else if(secondPlayer.size < 6) {
            forSecond.addAll(mCards)
            mCards.clear()
        }

        gameDB.child("firstPlayer").ref.setValue(forFirst)
        gameDB.child("secondPlayer").ref.setValue(forSecond)
        gameDB.child("cards").ref.setValue(mCards)
    }


    fun takeCards(gameId: String, isNew: Boolean) = viewModelScope.launch {

        val gameDB = database.child(gameId).child("game")

        val onBoard = _onBoardCard

        val player = isNew.check(_firstPlayer, _secondPlayer)
        val playerStr = isNew.check("firstPlayer", "secondPlayer")

        for (i in onBoard) {
            player.addAll(i.cards())
        }
        onBoard.clear()

        gameDB.child(playerStr).ref.setValue(player)
        gameDB.child("onBoard").ref.setValue(onBoard)
        gameDB.child("playerTurn").ref.setValue(opponentId.value)

        _isTake.value = false

        takeCardsFromPack(gameId)

    }

    fun iTake(gameId: String) = viewModelScope.launch {
        database.child(gameId).child("game/whoTake").ref.setValue(playerId.value)
        database.child(gameId).child("game/playerTurn").ref.setValue(opponentId.value)
    }

    fun iPass(gameId: String) = viewModelScope.launch {
        val gameDB = database.child(gameId).child("game")
        gameDB.child("whoTake").ref.setValue("done")
        _isPass.value = false
    }

}