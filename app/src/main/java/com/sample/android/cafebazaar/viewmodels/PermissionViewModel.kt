package com.sample.android.cafebazaar.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PermissionViewModel : ViewModel() {

    private val _mutableState = MutableStateFlow(State.PERMISSION_DIALOG)
    val mutableState: StateFlow<State>
        get() = _mutableState

    fun setState(state: State) {
        _mutableState.value = state
    }

    enum class State {
        PERMISSION_DIALOG,
        PERMISSION_DENIED,
        NEXT_SCREEN
    }
}