package ru.akaneiro.boilerplatecoder.newscreen.di

import com.intellij.openapi.project.Project
import dagger.BindsInstance
import dagger.Component
import ru.akaneiro.boilerplatecoder.data.file.CurrentPath
import ru.akaneiro.boilerplatecoder.newscreen.ui.NewScreenDialog
import javax.inject.Singleton

@Singleton
@Component
interface NewScreenComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance project: Project, @BindsInstance currentPath: CurrentPath?): NewScreenComponent
    }

    fun inject(dialog: NewScreenDialog)
}