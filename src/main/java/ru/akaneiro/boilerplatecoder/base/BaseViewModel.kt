package ru.akaneiro.boilerplatecoder.base

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.akaneiro.boilerplatecoder.core.UI
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel<State : Any, Effect : Any, Action : Any> : CoroutineScope {

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext = Dispatchers.UI + job

    private val initialState: State by lazy { setInitialState() }
    abstract fun setInitialState(): State

    private val _state: MutableStateFlow<State> = MutableStateFlow(initialState)
    val state: StateFlow<State> = _state

    private val _action: MutableSharedFlow<Action> = MutableSharedFlow()

    private val _effect: Channel<Effect> = Channel()
    val effect = _effect.receiveAsFlow()

    protected fun getState() = state.value

    init {
        observeActions()
    }

    fun clear() {
        job.cancel()
    }

    fun setAction(action: Action) {
        launch { _action.emit(action) }
    }

    protected fun setState(reducer: State.() -> State) {
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

    protected fun setEffect(builder: () -> Effect) {
        val effectValue = builder()
        launch { _effect.send(effectValue) }
    }

    abstract fun handleActions(action: Action)
}