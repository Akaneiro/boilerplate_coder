package ru.akaneiro.boilerplatecoder.widget

import kotlinx.coroutines.CoroutineScope
import ru.akaneiro.boilerplatecoder.base.BaseViewModel

interface ViewModelScreen<STATE: Any, EFFECT: Any, ACTION: Any, VIEW_MODEL: BaseViewModel<STATE, EFFECT, ACTION>>: CoroutineScope {
    var viewModel: VIEW_MODEL

    fun setAction(action: ACTION) {
        viewModel.setAction(action)
    }
}