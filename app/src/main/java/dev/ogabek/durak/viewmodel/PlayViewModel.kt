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
import dev.ogabek.durak.model.Player
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@SuppressLint("StaticFieldLeak")
class PlayViewModel @Inject constructor (
    private val database: DatabaseReference
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

    private val _opponent = mutableStateOf<Player?>(null)
    val opponent = _opponent

    fun setDatabase(isNewGame: Boolean, gameId: String) = viewModelScope.launch {



    }

}