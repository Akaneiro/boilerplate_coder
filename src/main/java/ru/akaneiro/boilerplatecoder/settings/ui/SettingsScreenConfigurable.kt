package ru.akaneiro.boilerplatecoder.settings.ui

import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.fileChooser.FileSaverDescriptor
import com.intellij.openapi.fileChooser.ex.FileSaverDialogImpl
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.Project
import com.intellij.packageDependencies.ui.TreeModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.akaneiro.boilerplatecoder.core.UI
import ru.akaneiro.boilerplatecoder.model.renderFileName
import ru.akaneiro.boilerplatecoder.model.renderSampleCode
import ru.akaneiro.boilerplatecoder.settings.di.DaggerSettingsScreenComponent
import ru.akaneiro.boilerplatecoder.settings.store.SettingsStore
import ru.akaneiro.boilerplatecoder.settings.store.SettingsViewModel
import javax.inject.Inject
import javax.swing.JComponent
import javax.swing.tree.DefaultMutableTreeNode
import kotlin.coroutines.CoroutineContext


class SettingsScreenConfigurable(private val project: Project) : Configurable, CoroutineScope {

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext = Dispatchers.UI + job

    private lateinit var panel: SettingsScreenPanel

    @Inject
    lateinit var viewModel: SettingsViewModel

    override fun createComponent(): JComponent {
        DaggerSettingsScreenComponent.factory().create(project).inject(this)
        panel = SettingsScreenPanel(project)

        with(panel) {
            onImportSettingsClick = { viewModel.setAction(SettingsView.SettingsAction.ImportSettingsClicked) }
            onExportSettingsClick = { viewModel.setAction(SettingsView.SettingsAction.ExportSettingsClicked) }
            onHelpClick = { viewModel.setAction(SettingsView.SettingsAction.HelpClicked) }
        }

        with(panel.categoriesPanel) {
            onAddClicked = { viewModel.setAction(SettingsView.SettingsAction.AddCategory) }
            onRemoveClicked = { viewModel.setAction(SettingsView.SettingsAction.RemoveCategory) }
            onItemSelected = { viewModel.setAction(SettingsView.SettingsAction.SelectCategory(it)) }
        }

        with(panel.categoryDetailsPanel) {
            onNameTextChanged = { viewModel.setAction(SettingsView.SettingsAction.ChangeCategoryName(it)) }
        }

        with(panel.screenElementsPanel) {
            onAddClicked = { viewModel.setAction(SettingsView.SettingsAction.AddScreenElement) }
            onItemSelected = { viewModel.setAction(SettingsView.SettingsAction.SelectScreenElement(it)) }
            onRemoveClicked = { viewModel.setAction(SettingsView.SettingsAction.RemoveScreenElement) }
        }

        with(panel.screenElementDetailsPanel) {
            onNameTextChanged = { viewModel.setAction(SettingsView.SettingsAction.RenameScreenElement(it)) }
            onSubdirectoryTextChanged =
                { viewModel.setAction(SettingsView.SettingsAction.ChangeScreenElementSubdirectory(it)) }
            onFileNameTextChanged = { viewModel.setAction(SettingsView.SettingsAction.ChangeScreenElementFilename(it)) }
        }

        with(panel.codePanel) {
            onTemplateTextChanged = { viewModel.setAction(SettingsView.SettingsAction.UpdateScreenElementTemplate(it)) }
        }

        launch {
            viewModel.state.map { it.toModel() }.collect {
                panel.render(it)
            }
        }
        launch {
            viewModel.effect.collect {
                when (it) {
                    SettingsStore.Effect.ShowFileChooserDialog -> {
                        showFileChooser()
                    }

                    SettingsStore.Effect.ShowFileSaverDialog -> {
                        showFileSaver()
                    }

                    SettingsStore.Effect.ShowHelpScreen -> {
                        showHelpDialog()
                    }
                }
            }
        }

        return panel
    }

    private fun showHelpDialog() {
        HelpDialog().show()
    }

    private fun showFileSaver() {
        val result = FileSaverDialogImpl(
            FileSaverDescriptor("Boilerplate Coder Export Settings", ""),
            project,
        ).save("boilerplate_coder_settings.json")
        launch {
            viewModel.setAction(SettingsView.SettingsAction.ExportFileResult(result))
        }
    }

    private fun showFileChooser() {
        FileChooser.chooseFile(
            FileChooserDescriptorFactory.createSingleFileDescriptor(),
            project,
            null,
        ) { virtualFile ->
            launch {
                viewModel.setAction(SettingsView.SettingsAction.SettingsFileChosen(virtualFile))
            }
        }
    }

    override fun isModified(): Boolean {
        return if (::viewModel.isInitialized) viewModel.getState().isModified else false
    }

    override fun apply() {
        launch { viewModel.setAction(SettingsView.SettingsAction.ApplySettings) }
    }

    override fun getDisplayName(): String = "Boilerplate Coder"

    private fun SettingsStore.State.toModel(): SettingsView.SettingsUiModel {
        val treeNode = DefaultMutableTreeNode("Root").apply {
            this@toModel.selectedCategory?.let { selectedCategory ->
                selectedCategory.screenElements.forEach { screenElement ->
                    var directory = this.root as DefaultMutableTreeNode
                    screenElement.subdirectory.split("/").filter { it.isNotBlank() }.forEach { subdirectory ->
                        val existingDirectory = directory.children().toList()
                            .find { (it as DefaultMutableTreeNode).userObject.equals(subdirectory) }
                        directory = if (existingDirectory != null) {
                            existingDirectory as DefaultMutableTreeNode
                        } else {
                            val newSubd = DefaultMutableTreeNode(subdirectory)
                            directory.insert(newSubd, directory.childCount)
                            newSubd
                        }
                    }
                    directory.add(DefaultMutableTreeNode(screenElement.name, false))
                }
            }
        }
        val treeModel = TreeModel(treeNode)
        sortTree(treeModel, treeNode)
        return SettingsView.SettingsUiModel(
            categories = this.categories,
            selectedCategoryIndex = this.selectedCategoryIndex,
            selectedElementIndex = this.selectedElementIndex,
            selectedCategory = this.selectedCategory,
            selectedElement = this.selectedElement,
            renderedFileName = this.selectedElement?.renderFileName(),
            selectedElementTemplate = this.selectedElement?.template,
            renderedSampleCode = this.selectedElement?.renderSampleCode(),
            structureTreeNode = treeModel,
        )
    }

    private fun sortTree(treeModel: TreeModel, rootNode: DefaultMutableTreeNode) {
        treeModel.reload(sort(rootNode))
    }

    private fun sort(node: DefaultMutableTreeNode): DefaultMutableTreeNode {
        //sort alphabetically
        for (i in 0 until node.childCount - 1) {
            val child = node.getChildAt(i) as DefaultMutableTreeNode
            val nt = child.userObject.toString()
            for (j in i + 1 until node.childCount) {
                val prevNode = node.getChildAt(j) as DefaultMutableTreeNode
                val np = prevNode.userObject.toString()
                if (nt.compareTo(np, ignoreCase = true) > 0) {
                    node.insert(child, j)
                    node.insert(prevNode, i)
                }
            }
            if (child.childCount > 0) {
                sort(child)
            }
        }

        //put folders first - normal on Windows and some flavors of Linux but not on Mac OS X.
        for (i in 0 until node.childCount - 1) {
            val child = node.getChildAt(i) as DefaultMutableTreeNode
            for (j in i + 1..node.childCount - 1) {
                val prevNode = node.getChildAt(j) as DefaultMutableTreeNode
                if (!prevNode.isLeaf && child.isLeaf) {
                    node.insert(child, j)
                    node.insert(prevNode, i)
                }
            }
        }
        return node
    }
}