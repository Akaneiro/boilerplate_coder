package ru.akaneiro.boilerplatecoder.newscreen.ui

import ru.akaneiro.boilerplatecoder.model.ScreenElement
import java.awt.Component
import javax.swing.JCheckBox
import javax.swing.JList
import javax.swing.ListCellRenderer

data class CheckboxListItem(
    val screenElement: ScreenElement,
    var isSelected: Boolean = false
) {

    override fun toString(): String {
        return screenElement.name
    }
}

// Handles rendering cells in the list using a check box
class CheckboxListRenderer : JCheckBox(), ListCellRenderer<CheckboxListItem> {
    override fun getListCellRendererComponent(
        list: JList<out CheckboxListItem>, value: CheckboxListItem,
        index: Int, isSelected: Boolean, cellHasFocus: Boolean
    ): Component {
        isEnabled = list.isEnabled
        setSelected(value.isSelected)
        font = list.font
        background = list.background
        foreground = list.foreground
        text = value.toString()
        return this
    }
}