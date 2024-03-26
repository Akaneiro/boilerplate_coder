package ru.akaneiro.boilerplatecoder.newscreen.store

import ru.akaneiro.boilerplatecoder.model.Category
import ru.akaneiro.boilerplatecoder.model.Module
import ru.akaneiro.boilerplatecoder.newscreen.ui.CheckboxListItem

interface NewScreenStore {

    sealed interface Effect {
        object Close: Effect
    }

    data class State(
        val screenName: String = "",
        val selectedModule: Module? = null,
        val selectedCategory: Category? = null,
        val categoriesList: List<Category> = listOf(),
        val screenElements: List<CheckboxListItem> = listOf(),
    )
}