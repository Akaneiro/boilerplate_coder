package ru.akaneiro.boilerplatecoder.settings.usecase

import ru.akaneiro.boilerplatecoder.model.Category
import javax.inject.Inject

class LoadCategoriesWithScreenElementsUseCase @Inject constructor(): SettingsUseCaseBase() {

    fun invoke(): List<Category> {
        val settings = loadSettings()
        return settings.categories
    }
}