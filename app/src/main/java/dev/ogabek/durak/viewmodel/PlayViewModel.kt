package dev.ogabek.durak.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@SuppressLint("StaticFieldLeak")
class PlayViewModel @Inject constructor (
    private val database: DatabaseReference,
    private val context: Context
): ViewModel() {

    private val _loading = mutableStateOf(false)
    val loading = _loading

    private val _roomId = mutableStateOf("")
    val roomId = _roomId

    private val _opponentFound = mutableStateOf(false)
    val opponentFound = _opponentFound

    private val _playerId = mutableStateOf("")
    val playerId = _playerId

    private val _showDialog = mutableStateOf(false)
    val showDialog = _showDialog

    private var _playerTurn = mutableStateOf("")
    val playerTurn = _playerTurn

    private val _error = mutableStateOf(false)
    val error = _error

    private val _errorMessage = mutableStateOf("")
    val errorMessage = _errorMessage

    fun setDatabase(isNewGame: Boolean, gameId: String) = viewModelScope.launch {

        if(isNewGame) {
            _roomId.value = gameId

            database.child("connections").child(roomId.value).addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (!opponentFound.value) {
                        when(snapshot.childrenCount) {
                            0L -> {
                                snapshot.child("firstPlayer").ref.setValue(playerId)
                                opponentFound.value = false
                            }
                            1L -> {
                                showDialog.value = true
                            }
                            2L -> {
                                showDialog.value = false

                                opponentFound.value = true

                                playerTurn.value = playerId.value
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    _error.value = true
                    _errorMessage.value = error.message + "  " + error.details
                }

            })

        } else {
            database.child("connections").child(roomId.value).addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (!opponentFound.value) {
                        if (snapshot.childrenCount == 1L) {
                            snapshot.child("secondPlayer").ref.setValue(playerId)
                            _roomId.value = gameId

                            playerTurn.value = snapshot.child("firstPlayer").value as String
                            opponentFound.value = true
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    _error.value = true
                    _errorMessage.value = error.message + "  " + error.details
                }

            })
        }

    }

}