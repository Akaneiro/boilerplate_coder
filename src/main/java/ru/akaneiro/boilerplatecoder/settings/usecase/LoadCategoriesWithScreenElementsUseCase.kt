package ru.akaneiro.boilerplatecoder.settings.usecase

import ru.akaneiro.boilerplatecoder.model.CategoryWithScreenElements
import javax.inject.Inject

class LoadCategoriesWithScreenElementsUseCase @Inject constructor(): SettingsUseCaseBase() {

    fun invoke(): List<CategoryWithScreenElements> {
        val settings = loadSettings()
        return settings.categories.map { category ->
            CategoryWithScreenElements(
                category,
                settings.screenElements.filter { it.categoryId == category.id }
            )
        }
    }
}