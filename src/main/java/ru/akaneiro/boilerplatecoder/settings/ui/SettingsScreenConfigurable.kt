package ru.akaneiro.boilerplatecoder.settings.ui

import com.intellij.ide.util.TreeFileChooserDialog
import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.util.Consumer
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

        with(panel.categoriesPanel) {
            onAddClicked = { viewModel.setAction(SettingsView.SettingsAction.AddCategory) }
            onRemoveClicked = { viewModel.setAction(SettingsView.SettingsAction.RemoveCategory) }
            onItemSelected = { viewModel.setAction(SettingsView.SettingsAction.SelectCategory(it)) }
        }

        with (panel.categoryDetailsPanel) {
            onNameTextChanged = { viewModel.setAction(SettingsView.SettingsAction.ChangeCategoryName(it)) }
        }

        with(panel.screenElementsPanel) {
            onAddClicked = { viewModel.setAction(SettingsView.SettingsAction.AddScreenElement) }
            onItemSelected = { viewModel.setAction(SettingsView.SettingsAction.SelectScreenElement(it)) }
            onRemoveClicked = { viewModel.setAction(SettingsView.SettingsAction.RemoveScreenElement) }
        }

        with(panel.screenElementDetailsPanel) {
            onNameTextChanged = { viewModel.setAction(SettingsView.SettingsAction.RenameScreenElement(it)) }
            onSubdirectoryTextChanged = { viewModel.setAction(SettingsView.SettingsAction.ChangeScreenElementSubdirectory(it)) }
            onFileNameTextChanged = { viewModel.setAction(SettingsView.SettingsAction.ChangeScreenElementFilename(it)) }
        }

        with (panel.codePanel) {
            onTemplateTextChanged = { viewModel.setAction(SettingsView.SettingsAction.UpdateScreenElementTemplate(it)) }
        }

        launch {
            viewModel.state.map { it.toModel() }.collect {
                panel.render(it)
            }
        }

        return panel
    }

    override fun isModified(): Boolean {
        return if (::viewModel.isInitialized) viewModel.state.value.isModified else false
    }

    override fun apply() {
        launch { viewModel.setAction(SettingsView.SettingsAction.ApplySettings) }
    }

    override fun getDisplayName(): String = "Boilerplate Coder"

    private fun SettingsStore.State.toModel(): SettingsView.SettingsUiModel {
        return SettingsView.SettingsUiModel(
            categories = this.categories,
            selectedCategoryIndex = this.selectedCategoryIndex,
            selectedElementIndex = this.selectedElementIndex,
            selectedCategoryWithScreenElements = this.selectedCategoryWithScreenElements,
            selectedElement = this.selectedElement,
            renderedFileName = this.selectedElement?.renderFileName(),
            selectedElementTemplate = this.selectedElement?.template,
            renderedSampleCode = this.selectedElement?.renderSampleCode(),
        )
    }
}