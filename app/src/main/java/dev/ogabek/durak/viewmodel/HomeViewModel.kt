package dev.ogabek.durak.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val database: DatabaseReference
): ViewModel() {

    private val _isLoading = mutableStateOf(false)
    val isLoading = _isLoading

    private val _isGameHave = mutableStateOf<Boolean?>(null)
    val isGameHave = _isGameHave

    private val _isError = mutableStateOf(false)
    val isError = _isError

    private val _errorMessage = mutableStateOf("")
    val errorMessage = _errorMessage

    fun isGameHave(gameId: String) {
        _isLoading.value = true
        database.child(gameId).addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                _isGameHave.value = snapshot.exists()
                _isLoading.value = false
            }

            override fun onCancelled(error: DatabaseError) {
                _isError.value = true
                _errorMessage.value = error.message
                _isLoading.value = false
            }

        })
    }

}