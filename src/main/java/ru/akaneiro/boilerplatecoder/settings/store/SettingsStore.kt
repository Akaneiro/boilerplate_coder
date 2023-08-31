package ru.akaneiro.boilerplatecoder.settings.store

import ru.akaneiro.boilerplatecoder.model.Category
import ru.akaneiro.boilerplatecoder.model.ScreenElement

interface SettingsStore {

    interface Effect {
        object ShowFileChooserDialog : Effect
        object ShowFileSaverDialog : Effect
        object ShowHelpScreen: Effect
    }

    data class State(
        val isModified: Boolean = false,
        val categories: List<Category> = listOf(),
        val selectedCategoryIndex: Int? = null,
        val selectedElementIndex: Int? = null,
    ) {
        val selectedCategory: Category?
            get() = if (selectedCategoryIndex != null) {
                categories[selectedCategoryIndex]
            } else {
                null
            }

        val selectedElement: ScreenElement?
            get() = if (selectedElementIndex != null && selectedCategory != null) {
                selectedCategory!!.screenElements[selectedElementIndex]
            } else {
                null
            }
    }
}