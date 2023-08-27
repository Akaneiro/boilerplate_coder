package ru.akaneiro.boilerplatecoder.settings.ui

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