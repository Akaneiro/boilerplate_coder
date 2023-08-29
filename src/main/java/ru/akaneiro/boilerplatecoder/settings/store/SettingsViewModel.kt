package ru.akaneiro.boilerplatecoder.settings.store

import kotlinx.coroutines.runBlocking
import ru.akaneiro.boilerplatecoder.base.BaseViewModel
import ru.akaneiro.boilerplatecoder.model.Category
import ru.akaneiro.boilerplatecoder.model.CategoryWithScreenElements
import ru.akaneiro.boilerplatecoder.model.ScreenElement
import ru.akaneiro.boilerplatecoder.model.Settings
import ru.akaneiro.boilerplatecoder.settings.ui.SettingsView
import ru.akaneiro.boilerplatecoder.settings.usecase.ApplySettingsUseCase
import ru.akaneiro.boilerplatecoder.settings.usecase.LoadCategoriesWithScreenElementsUseCase
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    loadCategoriesWithScreenElementsUseCase: LoadCategoriesWithScreenElementsUseCase,
    private val applySettingsUseCase: ApplySettingsUseCase,
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
        currentState.selectedCategoryWithScreenElements?.let { selectedCategoryWithScreenElements ->
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
            Settings(
                screenElements = categories.flatMap { it.screenElements }.toMutableList(),
                categories = categories.map { it.category }.toMutableList()
            )
        }
        applySettingsUseCase.invoke(newSettings)
    }

    private fun addCategory() = runBlocking {
        val newId = state.value.categories.size
        val newCategories = state.value.categories.toMutableList()
            .apply {
                add(CategoryWithScreenElements.getDefault(Category.getDefault(newId)))
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
                val newCategory = categoryData.category.copy(name = newCategoryName)
                set(
                    selectedCategoryIndex,
                    categoryData.copy(category = newCategory),
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
        currentState.selectedCategoryWithScreenElements?.let { selectedCategory ->
            val newScreenElements =
                selectedCategory.screenElements.toMutableList()
                    .apply { add(ScreenElement.getDefault(selectedCategory.category.id)) }
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
        currentState.selectedCategoryWithScreenElements?.let { selectedCategoryWithScreenElements ->
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