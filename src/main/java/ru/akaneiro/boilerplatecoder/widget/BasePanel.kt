package ru.akaneiro.boilerplatecoder.widget

import com.intellij.ui.JBSplitter
import javax.swing.JComponent
import javax.swing.JPanel

const val DEFAULT_HORIZONTAL_PROPORTION = 0.35f

abstract class BasePanel<MODEL> : JPanel() {

    protected var listenersBlocked: Boolean = false
        private set

    fun render(model: MODEL) {
        listenersBlocked = true
        performRender(model)
        listenersBlocked = false
    }

    abstract fun performRender(model: MODEL)

    protected fun createDetailComponent(
        firstElement: JComponent,
        secondElement: JComponent,
        proportion: Float = DEFAULT_HORIZONTAL_PROPORTION,
    ) = JBSplitter(proportion).apply {
        firstComponent = firstElement
        secondComponent = secondElement
    }
}