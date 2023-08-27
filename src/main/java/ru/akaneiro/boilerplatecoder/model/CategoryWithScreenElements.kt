package ru.akaneiro.boilerplatecoder.model

data class CategoryWithScreenElements(
    val category: Category,
    val screenElements: List<ScreenElement>,
) {
    companion object {
        fun getDefault(category: Category) = CategoryWithScreenElements(category, emptyList())
    }
}