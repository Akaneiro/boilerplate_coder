package ru.akaneiro.boilerplatecoder.settings.usecase

import ru.akaneiro.boilerplatecoder.model.ScreenElement
import javax.inject.Inject

class LoadScreenElementsUseCase @Inject constructor(): SettingsUseCaseBase() {

    fun invoke(categoryId: Int): List<ScreenElement> {
        return loadSettings().categories.find { it.id == categoryId }?.screenElements ?: listOf()
    }
}