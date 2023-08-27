package ru.akaneiro.boilerplatecoder.settings.usecase

import javax.inject.Inject

class LoadCategoriesUseCase @Inject constructor(): SettingsUseCaseBase() {

    fun invoke() = loadSettings().categories
}