package co.com.monkeymobile.fakestore.presentation.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<State : ViewState, Event : ViewEvent>(
    private val initialState: State
) : ViewModel() {

    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<ViewState> = _state.asStateFlow()

    private val _snackbarMessage = MutableSharedFlow<String>()
    val snackbarMessage = _snackbarMessage.asSharedFlow()

    open fun handleViewEvent(event: Event) {
        Log.d("ViewEvent", event.name)
    }

    protected fun updateUIState(state: State) {
        Log.d("ViewState", state.name)
        viewModelScope.launch(Dispatchers.Main) {
            _state.emit(state)
        }
    }

    protected fun showMessage(message: String) {
        viewModelScope.launch(Dispatchers.Main) {
            _snackbarMessage.emit(message)
        }
    }
}
