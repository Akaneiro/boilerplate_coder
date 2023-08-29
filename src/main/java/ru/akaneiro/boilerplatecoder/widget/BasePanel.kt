package ru.akaneiro.boilerplatecoder.widget

import javax.swing.JPanel

abstract class BasePanel<MODEL> : JPanel() {

    protected var listenersBlocked: Boolean = false
        private set

    fun render(model: MODEL) {
        listenersBlocked = true
        performRender(model)
        listenersBlocked = false
    }

    abstract fun performRender(model: MODEL)
}