package ru.akaneiro.boilerplatecoder.core

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Runnable
import javax.swing.SwingUtilities
import kotlin.coroutines.CoroutineContext

@Suppress("UnusedReceiverParameter")
val Dispatchers.UI: CoroutineDispatcher
    get() = UiDispatcher

private object UiDispatcher : CoroutineDispatcher() {

    override fun dispatch(context: CoroutineContext, block: Runnable) {
        SwingUtilities.invokeLater(block)
    }

    override fun isDispatchNeeded(context: CoroutineContext) =
        !SwingUtilities.isEventDispatchThread()
}
