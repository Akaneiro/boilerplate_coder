package ru.akaneiro.boilerplatecoder.base

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.akaneiro.boilerplatecoder.core.UI
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel<STATE : Any, EFFECT : Any, ACTION : Any> : CoroutineScope {

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext = Dispatchers.UI + job

    private val initialState: STATE by lazy { setInitialState() }
    abstract fun setInitialState(): STATE

    private val _state: MutableStateFlow<STATE> = MutableStateFlow(initialState)
    val state: StateFlow<STATE> = _state

    private val _action: MutableSharedFlow<ACTION> = MutableSharedFlow()

    private val _effect: Channel<EFFECT> = Channel()
    val effect = _effect.receiveAsFlow()

    protected fun getState() = state.value

    init {
        observeActions()
    }

    fun clear() {
        job.cancel()
    }

    fun setAction(action: ACTION) {
        launch { _action.emit(action) }
    }

    protected fun setState(reducer: STATE.() -> STATE) {
        launch {
            val newState = _state.value.reducer()
            _state.value = newState
        }
    }

    private fun observeActions() {
        launch {
            _action.collect {
                handleActions(it)
            }
        }
    }

    protected fun setEffect(builder: () -> EFFECT) {
        val effectValue = builder()
        launch { _effect.send(effectValue) }
    }

    abstract fun handleActions(action: ACTION)
}