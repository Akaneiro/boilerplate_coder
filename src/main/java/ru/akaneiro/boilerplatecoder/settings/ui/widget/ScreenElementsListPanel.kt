package ru.akaneiro.boilerplatecoder.settings.ui.widget

import com.intellij.ui.CollectionListModel
import com.intellij.ui.IdeBorderFactory
import com.intellij.ui.ToolbarDecorator
import com.intellij.ui.components.JBList
import ru.akaneiro.boilerplatecoder.model.ScreenElement
import ru.akaneiro.boilerplatecoder.settings.ui.SettingsView
import ru.akaneiro.boilerplatecoder.widget.BasePanel
import java.awt.GridLayout
import javax.swing.ListSelectionModel

class ScreenElementsListPanel : BasePanel<SettingsView.SettingsUiModel>() {

    companion object {
        private const val ELEMENTS_LABEL_TEXT = "Elements"
    }

    private val listModel = CollectionListModel<ScreenElement>()
    val list = JBList(listModel).apply {
        selectionMode = ListSelectionModel.SINGLE_SELECTION
    }
    private val toolbarDecorator: ToolbarDecorator = ToolbarDecorator.createDecorator(list)

    var onAddClicked: (() -> Unit)? = null
    var onRemoveClicked: ((Int) -> Unit)? = null
    var onMoveDownClicked: ((Int) -> Unit)? = null
    var onMoveUpClicked: ((Int) -> Unit)? = null
    var onItemSelected: ((Int) -> Unit)? = null

    init {
        border = IdeBorderFactory.createTitledBorder(ELEMENTS_LABEL_TEXT, false)
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

    override fun performRender(model: SettingsView.SettingsUiModel) {
        model.selectedCategory?.screenElements?.forEachIndexed { index, screenElement ->
            if (index < listModel.size && listModel.getElementAt(index) != screenElement) {
                listModel.setElementAt(screenElement, index)
            } else if (index >= listModel.size) {
                listModel.add(screenElement)
            }
        }
        if (listModel.size > (model.selectedCategory?.screenElements?.size ?: 0)) {
            listModel.removeRange(
                model.selectedCategory?.screenElements?.size ?: 0,
                listModel.size - 1
            )
        }
        if (list.selectedIndex != (model.selectedElementIndex ?: -1)) {
            if (model.selectedElementIndex != null) {
                list.selectedIndex = model.selectedElementIndex
            } else {
                list.clearSelection()
            }
        }
    }
}