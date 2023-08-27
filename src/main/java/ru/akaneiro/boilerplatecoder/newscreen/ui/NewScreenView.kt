package ru.akaneiro.boilerplatecoder.newscreen.ui

import ru.akaneiro.boilerplatecoder.model.Category

interface NewScreenView {

    sealed interface NewScreenAction {
        data class OkClicked(val screenName: String) : NewScreenAction

        data class SelectCategory(val categoryIndex: Int) : NewScreenAction
    }

    data class NewScreenUiModel(
        val selectedModuleName: String?,
        val selectedCategory: Category?,
        val categoriesList: List<Category>,
    )
}