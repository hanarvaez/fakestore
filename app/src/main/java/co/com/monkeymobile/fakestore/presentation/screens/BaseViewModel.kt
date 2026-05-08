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

abstract class BaseViewModel : ViewModel() {

    protected abstract val initialState: ViewState

    protected val _state = MutableStateFlow(initialState)
    val state: StateFlow<ViewState> = _state.asStateFlow()

    protected abstract val _effect: MutableSharedFlow<ViewEvent>
    val effect = _effect.asSharedFlow()

    open suspend fun handleEvent(event: ViewEvent) {
        Log.d("ViewEvent", event.name)
    }

    private fun updateState(state: ViewState) {
        viewModelScope.launch(Dispatchers.Main) {
            Log.d("ViewState", state.name)
            _state.emit(state)
        }
    }
}
