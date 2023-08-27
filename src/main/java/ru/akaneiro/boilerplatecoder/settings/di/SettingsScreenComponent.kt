package ru.akaneiro.boilerplatecoder.settings.di

import com.intellij.openapi.project.Project
import dagger.BindsInstance
import dagger.Component
import ru.akaneiro.boilerplatecoder.settings.ui.SettingsScreenConfigurable
import javax.inject.Singleton

@Singleton
@Component()
interface SettingsScreenComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance project: Project): SettingsScreenComponent
    }

    fun inject(dialog: SettingsScreenConfigurable)
}