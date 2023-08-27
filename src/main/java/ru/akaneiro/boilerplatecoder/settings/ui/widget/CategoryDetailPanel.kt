package ru.akaneiro.boilerplatecoder.settings.ui.widget

import com.intellij.ui.IdeBorderFactory
import ru.akaneiro.boilerplatecoder.settings.ui.SettingsUiModel
import ru.akaneiro.boilerplatecoder.widget.addTextChangeListener
import ru.akaneiro.boilerplatecoder.widget.constraintsLeft
import ru.akaneiro.boilerplatecoder.widget.constraintsRight
import ru.akaneiro.boilerplatecoder.widget.updateText
import java.awt.GridBagLayout
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField

class CategoryDetailsPanel : JPanel() {

    var onNameTextChanged: ((String) -> Unit)? = null

    private val nameTextField = JTextField()

    private var listenersBlocked = false

    init {
        border = IdeBorderFactory.createTitledBorder("Category Details", false)
        layout = GridBagLayout()

        add(JLabel("Category Name:"), constraintsLeft(0, 0))
        add(nameTextField, constraintsRight(1, 0))

        nameTextField.addTextChangeListener { if (!listenersBlocked) onNameTextChanged?.invoke(it) }
    }

    fun render(model: SettingsUiModel) {
        listenersBlocked = true
        val selectedCategory = model.selectedCategoryWithScreenElements
        nameTextField.updateText(selectedCategory?.category?.name ?: "")
        isEnabled = selectedCategory != null
        components.forEach { it.isEnabled = selectedCategory != null }
        listenersBlocked = false
    }
}
