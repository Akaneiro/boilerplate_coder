package ru.akaneiro.boilerplatecoder.settings.ui

import ru.akaneiro.boilerplatecoder.model.CategoryWithScreenElements
import ru.akaneiro.boilerplatecoder.model.ScreenElement

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