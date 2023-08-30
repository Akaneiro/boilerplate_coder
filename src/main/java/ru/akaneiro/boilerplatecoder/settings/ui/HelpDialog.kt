package ru.akaneiro.boilerplatecoder.settings.ui

import com.intellij.openapi.ui.DialogWrapper
import ru.akaneiro.boilerplatecoder.settings.ui.widget.HelpPanel

class HelpDialog : DialogWrapper(false) {
    init {
        init()
    }

    override fun createCenterPanel() = HelpPanel()
}