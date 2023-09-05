package ru.akaneiro.boilerplatecoder.newscreen.ui

import com.intellij.openapi.ui.ComboBox
import org.jdesktop.swingx.VerticalLayout
import ru.akaneiro.boilerplatecoder.model.Category
import ru.akaneiro.boilerplatecoder.widget.BasePanel
import java.awt.Dimension
import javax.swing.JComponent
import javax.swing.JLabel
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

    private val viewFields = listOf<Pair<JComponent, JComponent>>(
        JLabel(NAME_LABEL_TEXT) to nameTextField,
        JLabel(CATEGORY_LABEL_TEXT) to categoryComboBox,
        JLabel(MODULE_LABEL_TEXT) to moduleTextField,
    )

    init {
        layout = VerticalLayout()
        viewFields.forEach {
            add(createDetailComponent(it.first, it.second))
        }
        categoryComboBox.addActionListener {
            if (!listenersBlocked) {
                onCategoryIndexChanged?.invoke(categoryComboBox.selectedIndex)
            }
        }
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
