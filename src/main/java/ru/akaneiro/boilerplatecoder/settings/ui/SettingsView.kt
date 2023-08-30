package ru.akaneiro.boilerplatecoder.settings.ui

import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileWrapper
import ru.akaneiro.boilerplatecoder.model.Category
import ru.akaneiro.boilerplatecoder.model.ScreenElement

interface SettingsView {
    sealed interface SettingsAction {
        object AddCategory : SettingsAction
        object ApplySettings : SettingsAction
        object RemoveCategory : SettingsAction
        data class SelectCategory(val categoryIndex: Int) : SettingsAction
        data class ChangeCategoryName(val newCategoryName: String) : SettingsAction

        object AddScreenElement : SettingsAction
        object RemoveScreenElement : SettingsAction
        data class SelectScreenElement(val elementId: Int) : SettingsAction
        data class RenameScreenElement(val newElementName: String) : SettingsAction
        data class ChangeScreenElementFilename(val newFilename: String) : SettingsAction
        data class ChangeScreenElementSubdirectory(val subdirectory: String) : SettingsAction
        data class UpdateScreenElementTemplate(val template: String) : SettingsAction

        object ImportSettingsClicked : SettingsAction
        object ExportSettingsClicked : SettingsAction
        data class ExportFileResult(val result: VirtualFileWrapper?) : SettingsAction
        data class SettingsFileChosen(val settingsFile: VirtualFile) : SettingsAction
    }

    data class SettingsUiModel(
        val categories: List<Category>,
        val selectedCategoryIndex: Int?,
        val selectedElementIndex: Int?,
        val selectedCategory: Category?,
        val selectedElement: ScreenElement?,
        val renderedFileName: String?,
        val selectedElementTemplate: String?,
        val renderedSampleCode: String?,
    )
}