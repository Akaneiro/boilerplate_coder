package ru.akaneiro.boilerplatecoder.settings.ui.widget

import ru.akaneiro.boilerplatecoder.model.DefaultVariable
import java.awt.BorderLayout
import java.awt.FlowLayout
import javax.swing.BoxLayout
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField

class HelpPanel : JPanel() {

    init {
        layout = BorderLayout()
        val panel = JPanel().apply {
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            DefaultVariable.values().forEach {
                add(
                    JPanel().apply {
                        layout = FlowLayout(FlowLayout.LEADING)
                        add(JTextField(it.template).apply { isEditable = false })
                        add(JLabel(" - ${it.description}"))
                    }
                )
            }
        }
        add(JLabel("Available variables in templates:"), BorderLayout.NORTH)
        add(panel, BorderLayout.CENTER)
    }
}