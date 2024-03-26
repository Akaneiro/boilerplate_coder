package ru.akaneiro.boilerplatecoder.newscreen.ui

import com.intellij.openapi.ui.ComboBox
import com.intellij.ui.IdeBorderFactory
import org.jdesktop.swingx.VerticalLayout
import ru.akaneiro.boilerplatecoder.model.Category
import ru.akaneiro.boilerplatecoder.model.ScreenElement
import ru.akaneiro.boilerplatecoder.widget.BasePanel
import ru.akaneiro.boilerplatecoder.widget.addTextChangeListener
import ru.akaneiro.boilerplatecoder.widget.updateText
import java.awt.Dimension
import java.awt.GridLayout
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.*

class NewScreenPanel : BasePanel<NewScreenView.NewScreenUiModel>() {

    companion object {
        private const val NAME_LABEL_TEXT = "Screen Name:"
        private const val CATEGORY_LABEL_TEXT = "Group:"
        private const val ELEMENTS_LABEL_TEXT = "Elements:"
    }

    private val nameTextField = JTextField()
    private val categoryComboBox = ComboBox<Category>()
    private val screenElementsList = JList<CheckboxListItem>()
    var onCategoryIndexChanged: ((Int) -> Unit)? = null
    var onScreenNameTextChanged: ((String) -> Unit)? = null
    var onScreenElementClick: ((ScreenElement) -> Unit)? = null

    private val viewFields = listOf<Pair<JComponent, JComponent>>(
        JLabel(NAME_LABEL_TEXT) to nameTextField,
        JLabel(CATEGORY_LABEL_TEXT) to categoryComboBox,
    )

    init {
        layout = VerticalLayout()
        viewFields.forEach {
            add(createDetailComponent(it.first, it.second))
        }
        add(
            JPanel().apply {
                border = IdeBorderFactory.createTitledBorder(ELEMENTS_LABEL_TEXT, false)
                layout = GridLayout(1, 1)
                add(screenElementsList)
            }
        )
        screenElementsList.apply {
            cellRenderer = CheckboxListRenderer()
            selectionMode = ListSelectionModel.SINGLE_SELECTION
            addMouseListener(object : MouseAdapter() {
                override fun mouseClicked(event: MouseEvent) {
                    super.mouseClicked(event)
                    val index = screenElementsList.locationToIndex(event.point)
                    val item = screenElementsList.model.getElementAt(index) as CheckboxListItem
                    onScreenElementClick?.invoke(item.screenElement)
                }
            })
        }
        categoryComboBox.addActionListener {
            if (!listenersBlocked) {
                onCategoryIndexChanged?.invoke(categoryComboBox.selectedIndex)
            }
        }
        nameTextField.addTextChangeListener {
            if (!listenersBlocked) {
                onScreenNameTextChanged?.invoke(nameTextField.text)
            }
        }
    }

    override fun getPreferredSize() = Dimension(350, 110)

    override fun performRender(model: NewScreenView.NewScreenUiModel) {
        categoryComboBox.removeAllItems()
        model.categoriesList.forEach {
            categoryComboBox.addItem(it)
        }
        categoryComboBox.selectedItem = model.selectedCategory
        nameTextField.updateText(model.screenName)
        val screenElements = model.screenElements.toTypedArray()
        screenElementsList.setListData(screenElements)
        screenElementsList.repaint()
    }
}
