package ru.akaneiro.boilerplatecoder.settings.store

import com.google.gson.Gson
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileWrapper
import kotlinx.coroutines.runBlocking
import ru.akaneiro.boilerplatecoder.base.BaseViewModel
import ru.akaneiro.boilerplatecoder.model.Category
import ru.akaneiro.boilerplatecoder.model.ScreenElement
import ru.akaneiro.boilerplatecoder.model.Settings
import ru.akaneiro.boilerplatecoder.settings.ui.SettingsView
import ru.akaneiro.boilerplatecoder.settings.usecase.ApplySettingsUseCase
import ru.akaneiro.boilerplatecoder.settings.usecase.ConvertImportSettingsFromJsonUseCase
import ru.akaneiro.boilerplatecoder.settings.usecase.LoadCategoriesWithScreenElementsUseCase
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    loadCategoriesWithScreenElementsUseCase: LoadCategoriesWithScreenElementsUseCase,
    private val applySettingsUseCase: ApplySettingsUseCase,
    private val convertImportSettingsFromJsonUseCase: ConvertImportSettingsFromJsonUseCase,
) : BaseViewModel<SettingsStore.State, SettingsStore.Effect, SettingsView.SettingsAction>() {

    init {
        val categories = loadCategoriesWithScreenElementsUseCase.invoke()
        setState {
            copy(categories = categories)
        }
    }

    override fun setInitialState(): SettingsStore.State {
        return SettingsStore.State()
    }

    override fun handleActions(action: SettingsView.SettingsAction) {
        when (action) {
            SettingsView.SettingsAction.ApplySettings -> applySettings()
            SettingsView.SettingsAction.AddCategory -> addCategory()
            SettingsView.SettingsAction.RemoveCategory -> removeCategory()
            is SettingsView.SettingsAction.SelectCategory -> selectCategory(action.categoryIndex)
            is SettingsView.SettingsAction.ChangeCategoryName -> renameCategory(action.newCategoryName)
            SettingsView.SettingsAction.AddScreenElement -> addScreenElement()
            is SettingsView.SettingsAction.SelectScreenElement -> {
                setState {
                    copy(selectedElementIndex = action.elementId)
                }
            }

            is SettingsView.SettingsAction.UpdateScreenElementTemplate -> {
                updateScreenElementTemplate(action.template)
            }

            SettingsView.SettingsAction.RemoveScreenElement -> removeScreenElement()

            is SettingsView.SettingsAction.RenameScreenElement -> {
                renameScreenElement(action.newElementName)
            }

            is SettingsView.SettingsAction.ChangeScreenElementSubdirectory -> {
                changeScreenElementSubdirectory(action.subdirectory)
            }

            is SettingsView.SettingsAction.ChangeScreenElementFilename -> {
                changeScreenElementFilename(action.newFilename)
            }

            SettingsView.SettingsAction.ImportSettingsClicked -> {
                setEffect {
                    SettingsStore.Effect.ShowFileChooserDialog
                }
            }

            SettingsView.SettingsAction.ExportSettingsClicked -> {
                setEffect {
                    SettingsStore.Effect.ShowFileSaverDialog
                }
            }

            is SettingsView.SettingsAction.SettingsFileChosen -> {
                importSettingsFromFile(action.settingsFile)
            }

            is SettingsView.SettingsAction.ExportFileResult -> {
                action.result?.run {
                    exportSettingsToFile(this)
                }
            }

            SettingsView.SettingsAction.HelpClicked -> {
                setEffect {
                    SettingsStore.Effect.ShowHelpScreen
                }
            }
        }
    }

    private fun importSettingsFromFile(file: VirtualFile) {
        runBlocking {
            val jsonString = VfsUtil.loadText(file)
            try {
                val categories = convertImportSettingsFromJsonUseCase.invoke(jsonString)
                setState {
                    copy(
                        isModified = true,
                        categories = categories,
                        selectedCategoryIndex = null,
                        selectedElementIndex = null,
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun exportSettingsToFile(result: VirtualFileWrapper) {
        runBlocking {
            with(getState()) {
                val settingsToExport = Settings(
                    categories = getState().categories.toMutableList(),
                )
                val serialized = Gson().toJson(settingsToExport)
                result.file.writeText(serialized)
            }
        }
    }

    private fun selectCategory(categoryIndex: Int) {
        setState {
            copy(
                selectedCategoryIndex = categoryIndex,
                selectedElementIndex = null,
            )
        }
    }

    private fun modifyScreenElement(screenElementModifier: ScreenElement.() -> ScreenElement) = runBlocking {
        val currentState = getState()
        currentState.selectedCategory?.let { selectedCategoryWithScreenElements ->
            val newScreenElements = selectedCategoryWithScreenElements.screenElements.toMutableList()
                .apply {
                    set(
                        currentState.selectedElementIndex!!,
                        currentState.selectedElement!!.screenElementModifier(),
                    )
                }
            val newCategories = currentState.categories.toMutableList()
                .apply {
                    set(
                        currentState.selectedCategoryIndex!!,
                        selectedCategoryWithScreenElements.copy(screenElements = newScreenElements)
                    )
                }
            setState {
                copy(
                    categories = newCategories,
                    isModified = true,
                )
            }
        }
    }

    private fun changeScreenElementFilename(newFilename: String) {
        modifyScreenElement {
            copy(filenameTemplate = newFilename)
        }
    }

    private fun changeScreenElementSubdirectory(newSubdirectory: String) {
        modifyScreenElement {
            copy(subdirectory = newSubdirectory)
        }
    }

    private fun renameScreenElement(newElementName: String) {
        modifyScreenElement {
            copy(name = newElementName)
        }
    }

    private fun updateScreenElementTemplate(newTemplate: String) {
        modifyScreenElement {
            copy(template = newTemplate)
        }
    }

    private fun applySettings() {
        val newSettings = state.value.run {
            Settings(categories = categories.toMutableList())
        }
        applySettingsUseCase.invoke(newSettings)
    }

    private fun addCategory() = runBlocking {
        val newId = state.value.categories.size
        val newCategories = state.value.categories.toMutableList()
            .apply {
                add(Category.getDefault(newId))
            }
        setState {
            copy(
                categories = newCategories,
                isModified = true,
                selectedCategoryIndex = newCategories.lastIndex,
                selectedElementIndex = null,
            )
        }
    }

    private fun removeCategory() = runBlocking {
        val currentState = getState()
        currentState.selectedCategoryIndex?.let {
            val newCategories = state.value.categories
                .toMutableList()
                .apply {
                    removeAt(it)
                }
            setState {
                copy(
                    categories = newCategories,
                    isModified = true,
                    selectedCategoryIndex = null,
                    selectedElementIndex = null,
                )
            }
        }
    }

    private fun renameCategory(newCategoryName: String) = runBlocking {
        val currentState = getState()
        val selectedCategoryIndex = currentState.selectedCategoryIndex
        val categoryData = currentState.categories[selectedCategoryIndex!!]
        val newCategories = currentState.categories.toMutableList()
            .apply {
                val newCategory = categoryData.copy(name = newCategoryName)
                set(
                    selectedCategoryIndex,
                    newCategory,
                )
            }
        setState {
            copy(
                categories = newCategories,
                isModified = true,
            )
        }
    }

    private fun addScreenElement() = runBlocking {
        val currentState = getState()
        currentState.selectedCategory?.let { selectedCategory ->
            val newScreenElements =
                selectedCategory.screenElements.toMutableList()
                    .apply { add(ScreenElement.getDefault()) }
            val newCategories = currentState.categories.toMutableList().apply {
                set(
                    currentState.selectedCategoryIndex!!,
                    selectedCategory.copy(screenElements = newScreenElements),
                )
            }
            val selectedScreenElementIndex = newScreenElements.lastIndex
            setState {
                copy(
                    categories = newCategories,
                    isModified = true,
                    selectedElementIndex = selectedScreenElementIndex,
                )
            }
        }
    }

    private fun removeScreenElement() = runBlocking {
        val currentState = getState()
        currentState.selectedCategory?.let { selectedCategoryWithScreenElements ->
            val newScreenElements = selectedCategoryWithScreenElements.screenElements
                .toMutableList()
                .apply {
                    removeAt(currentState.selectedElementIndex!!)
                }
            val newCategories = currentState.categories
                .toMutableList()
                .apply {
                    set(
                        currentState.selectedCategoryIndex!!,
                        selectedCategoryWithScreenElements.copy(screenElements = newScreenElements)
                    )
                }
            setState {
                copy(
                    isModified = true,
                    categories = newCategories,
                    selectedElementIndex = null,
                )
            }
        }
    }
}