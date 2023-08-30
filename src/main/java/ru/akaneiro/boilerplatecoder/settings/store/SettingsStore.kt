package ru.akaneiro.boilerplatecoder.settings.store

import ru.akaneiro.boilerplatecoder.model.CategoryWithScreenElements
import ru.akaneiro.boilerplatecoder.model.ScreenElement

interface SettingsStore {

    interface Effect {
        object ShowFileChooserDialog: Effect
        object ShowFileSaverDialog: Effect
    }

    data class State(
        val isModified: Boolean = true,
        val categories: List<CategoryWithScreenElements> = listOf(),
        val selectedCategoryIndex: Int? = null,
        val selectedElementIndex: Int? = null,
    ) {
        val selectedCategoryWithScreenElements: CategoryWithScreenElements?
            get() = if (selectedCategoryIndex != null) {
                categories[selectedCategoryIndex]
            } else {
                null
            }

        val selectedElement: ScreenElement?
            get() = if (selectedElementIndex != null && selectedCategoryWithScreenElements != null) {
                selectedCategoryWithScreenElements!!.screenElements[selectedElementIndex]
            } else {
                null
            }
    }
}