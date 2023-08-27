package ru.akaneiro.boilerplatecoder.newscreen.ui

import com.intellij.openapi.ui.ComboBox
import ru.akaneiro.boilerplatecoder.model.Category
import ru.akaneiro.boilerplatecoder.widget.constraintsLeft
import ru.akaneiro.boilerplatecoder.widget.constraintsRight
import java.awt.Dimension
import java.awt.GridBagLayout
import javax.swing.BoxLayout
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField

class NewScreenPanel : JPanel() {

    val nameTextField = JTextField()
    val moduleTextField = JTextField()
    val categoryComboBox = ComboBox<Category>()
    var onCategoryIndexChanged: ((Int) -> Unit)? = null
    private var listenersBlocked = false

    init {
        layout = BoxLayout(this, BoxLayout.Y_AXIS)
        add(
            JPanel().apply {
                layout = GridBagLayout()
                add(JLabel("Name:"), constraintsLeft(0, 0))
                add(nameTextField, constraintsRight(1, 0))
                add(JLabel("Category:"), constraintsLeft(0, 1))
                add(categoryComboBox, constraintsRight(1, 1))
                add(JLabel("Module:"), constraintsLeft(0, 2).apply { isEnabled = false })
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

    fun render(model: NewScreenView.NewScreenUiModel) {
        listenersBlocked = true

        moduleTextField.isEnabled = false
        moduleTextField.text = model.selectedModuleName
        categoryComboBox.removeAllItems()
        model.categoriesList.forEach {
            categoryComboBox.addItem(it)
        }
        categoryComboBox.selectedItem = model.selectedCategory

        listenersBlocked = false
    }
}
