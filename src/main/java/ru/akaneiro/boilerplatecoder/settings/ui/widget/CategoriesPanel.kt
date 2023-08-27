package ru.akaneiro.boilerplatecoder.settings.ui.widget

import com.intellij.ui.CollectionListModel
import com.intellij.ui.IdeBorderFactory
import com.intellij.ui.ToolbarDecorator
import com.intellij.ui.components.JBList
import ru.akaneiro.boilerplatecoder.model.Category
import ru.akaneiro.boilerplatecoder.settings.ui.SettingsUiModel
import java.awt.GridLayout
import javax.swing.JPanel
import javax.swing.ListSelectionModel

class CategoriesPanel : JPanel() {

    private val listModel = CollectionListModel<Category>()
    val list = JBList(listModel).apply {
        selectionMode = ListSelectionModel.SINGLE_SELECTION
    }
    private val toolbarDecorator: ToolbarDecorator = ToolbarDecorator.createDecorator(list)

    var onAddClicked: (() -> Unit)? = null
    var onRemoveClicked: ((Int) -> Unit)? = null
    var onMoveDownClicked: ((Int) -> Unit)? = null
    var onMoveUpClicked: ((Int) -> Unit)? = null
    var onItemSelected: ((Int) -> Unit)? = null

    private var listenersBlocked = false

    init {
        border = IdeBorderFactory.createTitledBorder("Category", false)
        layout = GridLayout(1, 1)
        toolbarDecorator.apply {
            setAddAction { onAddClicked?.invoke() }
            setRemoveAction { onRemoveClicked?.invoke(list.selectedIndex) }
            setMoveDownAction { onMoveDownClicked?.invoke(list.selectedIndex) }
            setMoveUpAction { onMoveUpClicked?.invoke(list.selectedIndex) }
            add(createPanel())
        }
        list.addListSelectionListener { if (!it.valueIsAdjusting && !listenersBlocked) onItemSelected?.invoke(list.selectedIndex) }
    }

    fun render(model: SettingsUiModel) {
        listenersBlocked = true
        model.categories.forEachIndexed { index, categoryWithScreenElements ->
            if (index < listModel.size && listModel.getElementAt(index) != categoryWithScreenElements.category) {
                listModel.setElementAt(categoryWithScreenElements.category, index)
            } else if (index >= listModel.size) {
                listModel.add(categoryWithScreenElements.category)
            }
        }
        if (listModel.size > model.categories.size) {
            listModel.removeRange(model.categories.size, listModel.size - 1)
        }
        if (list.selectedIndex != (model.selectedCategoryIndex ?: -1)) {
            if (model.selectedCategoryIndex != null) {
                list.selectedIndex = model.selectedCategoryIndex
            } else {
                list.clearSelection()
            }
        }
        listenersBlocked = false
    }
}
