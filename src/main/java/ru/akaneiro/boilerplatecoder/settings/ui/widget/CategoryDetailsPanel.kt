package ru.akaneiro.boilerplatecoder.settings.ui.widget

import com.intellij.ui.IdeBorderFactory
import com.intellij.ui.treeStructure.Tree
import org.jdesktop.swingx.VerticalLayout
import ru.akaneiro.boilerplatecoder.settings.ui.SettingsView
import ru.akaneiro.boilerplatecoder.widget.BasePanel
import ru.akaneiro.boilerplatecoder.widget.addTextChangeListener
import ru.akaneiro.boilerplatecoder.widget.updateText
import java.util.*
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JTextField
import javax.swing.JTree
import javax.swing.tree.DefaultTreeCellRenderer
import javax.swing.tree.TreeNode
import javax.swing.tree.TreePath

class CategoryDetailsPanel : BasePanel<SettingsView.SettingsUiModel>() {

    private companion object {
        private const val CATEGORY_DETAILS_TEXT_LABEL = "Group Details"
        private const val CATEGORY_NAME_TEXT_LABEL = "Group Name:"
        private const val STRUCTURE_TEXT_LABEL = "Structure:"
    }

    var onNameTextChanged: ((String) -> Unit)? = null

    private val nameTextLabel = JLabel(CATEGORY_NAME_TEXT_LABEL)
    private val nameTextField = JTextField()
    private val structureTextLabel = JLabel(STRUCTURE_TEXT_LABEL)
    private val treeView = Tree().apply {
        isRootVisible = false
        this.cellRenderer = DefaultTreeCellRenderer()
    }
    private val enableSwitchingElements = mutableListOf<JComponent>(
        nameTextLabel,
        nameTextField,
        structureTextLabel,
        treeView,
    )

    init {
        border = IdeBorderFactory.createTitledBorder(CATEGORY_DETAILS_TEXT_LABEL, false)
        layout = VerticalLayout()

        add(createDetailComponent(nameTextLabel, nameTextField))
        add(structureTextLabel)
        add(treeView)

        nameTextField.addTextChangeListener { if (!listenersBlocked) onNameTextChanged?.invoke(it) }
    }

    override fun performRender(model: SettingsView.SettingsUiModel) {
        val selectedCategory = model.selectedCategory
        nameTextField.updateText(selectedCategory?.name ?: "")
        enableSwitchingElements.forEach {
            it.isEnabled = selectedCategory != null
        }
        treeView.model = model.structureTreeNode
        treeView.expandAll(TreePath(model.structureTreeNode.root))
        components.forEach { it.isEnabled = selectedCategory != null }
    }

    private fun JTree.expandAll(parent: TreePath) {
        val node: TreeNode = parent.lastPathComponent as TreeNode
        if (node.childCount >= 0) {
            val e: Enumeration<*> = node.children()
            while (e.hasMoreElements()) {
                val n: TreeNode = e.nextElement() as TreeNode
                val path: TreePath = parent.pathByAddingChild(n)
                this.expandAll(path)
            }
        }
        this.expandPath(parent)
    }
}
