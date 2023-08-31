package ru.akaneiro.boilerplatecoder.newscreen.ui

import com.intellij.openapi.ui.ComboBox
import ru.akaneiro.boilerplatecoder.model.Category
import ru.akaneiro.boilerplatecoder.widget.BasePanel
import ru.akaneiro.boilerplatecoder.widget.constraintsLeft
import ru.akaneiro.boilerplatecoder.widget.constraintsRight
import java.awt.Dimension
import java.awt.GridBagLayout
import javax.swing.BoxLayout
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField

class NewScreenPanel : BasePanel<NewScreenView.NewScreenUiModel>() {

    companion object {
        private const val NAME_LABEL_TEXT = "Screen Name:"
        private const val CATEGORY_LABEL_TEXT = "Group:"
        private const val MODULE_LABEL_TEXT = "Module:"
    }

    val nameTextField = JTextField()
    private val moduleTextField = JTextField()
    private val categoryComboBox = ComboBox<Category>()
    var onCategoryIndexChanged: ((Int) -> Unit)? = null

    init {
        layout = BoxLayout(this, BoxLayout.Y_AXIS)
        add(
            JPanel().apply {
                layout = GridBagLayout()
                add(JLabel(NAME_LABEL_TEXT), constraintsLeft(0, 0))
                add(nameTextField, constraintsRight(1, 0))
                add(JLabel(CATEGORY_LABEL_TEXT), constraintsLeft(0, 1))
                add(categoryComboBox, constraintsRight(1, 1))
                add(JLabel(MODULE_LABEL_TEXT), constraintsLeft(0, 2).apply { isEnabled = false })
                add(moduleTextField, constraintsRight(1, 2))
                categoryComboBox.addActionListener {
                    if (!listenersBlocked) {
                        onCategoryIndexChanged?.invoke(categoryComboBox.selectedIndex)
                    }
                }
            }
        )
    }

    override fun getPreferredSize() = Dimension(350, 110)

    override fun performRender(model: NewScreenView.NewScreenUiModel) {
        moduleTextField.isEnabled = false
        moduleTextField.text = model.selectedModuleName
        categoryComboBox.removeAllItems()
        model.categoriesList.forEach {
            categoryComboBox.addItem(it)
        }
        categoryComboBox.selectedItem = model.selectedCategory
    }
}
