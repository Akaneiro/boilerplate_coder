package ru.akaneiro.boilerplatecoder.settings.usecase

import ru.akaneiro.boilerplatecoder.data.ScreenGeneratorApplicationComponent

open class SettingsUseCaseBase {

    protected fun loadSettings() = ScreenGeneratorApplicationComponent.getInstance().settings
}