package ru.akaneiro.boilerplatecoder.newscreen.store

import ru.akaneiro.boilerplatecoder.model.Category
import ru.akaneiro.boilerplatecoder.model.Module

interface NewScreenStore {

    sealed interface Effect {
        object Close: Effect
    }

    data class State(
        val selectedModule: Module? = null,
        val selectedCategory: Category? = null,
        val categoriesList: List<Category> = listOf(),
    )
}