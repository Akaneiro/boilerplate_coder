package ru.akaneiro.boilerplatecoder.settings.ui.widget

import com.intellij.ui.IdeBorderFactory
import ru.akaneiro.boilerplatecoder.settings.ui.SettingsView
import ru.akaneiro.boilerplatecoder.widget.*
import java.awt.GridBagLayout
import javax.swing.JLabel
import javax.swing.JTextField

class CategoryDetailsPanel : BasePanel<SettingsView.SettingsUiModel>() {

    private companion object {
        private const val CATEGORY_DETAILS_TEXT_LABEL = "Category Details"
        private const val CATEGORY_NAME_TEXT_LABEL = "Category Name:"
    }

    var onNameTextChanged: ((String) -> Unit)? = null

    private val nameTextField = JTextField()

    init {
        border = IdeBorderFactory.createTitledBorder(CATEGORY_DETAILS_TEXT_LABEL, false)
        layout = GridBagLayout()

        add(JLabel(CATEGORY_NAME_TEXT_LABEL), constraintsLeft(0, 0))
        add(nameTextField, constraintsRight(1, 0))

        nameTextField.addTextChangeListener { if (!listenersBlocked) onNameTextChanged?.invoke(it) }
    }

    override fun performRender(model: SettingsView.SettingsUiModel) {
        val selectedCategory = model.selectedCategory
        nameTextField.updateText(selectedCategory?.name ?: "")
        isEnabled = selectedCategory != null
        components.forEach { it.isEnabled = selectedCategory != null }
    }
}
