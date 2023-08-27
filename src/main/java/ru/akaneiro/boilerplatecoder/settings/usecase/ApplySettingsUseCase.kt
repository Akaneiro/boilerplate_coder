package ru.akaneiro.boilerplatecoder.settings.usecase

import ru.akaneiro.boilerplatecoder.data.ScreenGeneratorApplicationComponent
import ru.akaneiro.boilerplatecoder.model.Settings
import javax.inject.Inject

class ApplySettingsUseCase @Inject constructor() {

    fun invoke(newSettings: Settings) {
        ScreenGeneratorApplicationComponent.getInstance().run {
            this.settings = newSettings
        }
    }
}