package ru.akaneiro.boilerplatecoder.newscreen.ui

import ru.akaneiro.boilerplatecoder.model.Category
import ru.akaneiro.boilerplatecoder.model.ScreenElement

interface NewScreenView {

    sealed interface NewScreenAction {
        object OkClicked : NewScreenAction

        data class SelectCategory(val categoryIndex: Int) : NewScreenAction

        data class ScreenNameChanged(val screenName: String): NewScreenAction

        data class ScreenElementClick(val screenElement: ScreenElement): NewScreenAction
    }

    data class NewScreenUiModel(
        val selectedModuleName: String?,
        val selectedCategory: Category?,
        val categoriesList: List<Category>,
        val okButtonActive: Boolean,
        val screenName: String,
        val screenElements: List<CheckboxListItem>,
    )
}