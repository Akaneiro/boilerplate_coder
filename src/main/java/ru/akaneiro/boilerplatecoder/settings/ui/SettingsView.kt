package ru.akaneiro.boilerplatecoder.settings.ui

import ru.akaneiro.boilerplatecoder.model.CategoryWithScreenElements
import ru.akaneiro.boilerplatecoder.model.ScreenElement

interface SettingsView {
    sealed interface SettingsAction {
        object AddCategory: SettingsAction
        object ApplySettings: SettingsAction
        object RemoveCategory: SettingsAction
        data class SelectCategory(val categoryIndex: Int): SettingsAction
        data class ChangeCategoryName(val newCategoryName: String): SettingsAction

        object AddScreenElement: SettingsAction
        object RemoveScreenElement: SettingsAction
        data class SelectScreenElement(val elementId: Int): SettingsAction
        data class RenameScreenElement(val newElementName: String) : SettingsAction
        data class ChangeScreenElementFilename(val newFilename: String): SettingsAction
        data class ChangeScreenElementSubdirectory(val subdirectory: String): SettingsAction
        data class UpdateScreenElementTemplate(val template: String): SettingsAction
    }

    data class SettingsUiModel(
        val categories: List<CategoryWithScreenElements>,
        val selectedCategoryIndex: Int?,
        val selectedElementIndex: Int?,
        val selectedCategoryWithScreenElements: CategoryWithScreenElements?,
        val selectedElement: ScreenElement?,
        val renderedFileName: String?,
        val selectedElementTemplate: String?,
        val renderedSampleCode: String?,
    )
}