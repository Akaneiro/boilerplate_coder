package ru.akaneiro.boilerplatecoder.newscreen.ui

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.akaneiro.boilerplatecoder.core.UI
import ru.akaneiro.boilerplatecoder.data.file.CurrentPath
import ru.akaneiro.boilerplatecoder.newscreen.di.DaggerNewScreenComponent
import ru.akaneiro.boilerplatecoder.newscreen.store.NewScreenStore
import ru.akaneiro.boilerplatecoder.newscreen.store.NewScreenViewModel
import javax.inject.Inject
import javax.swing.JComponent
import kotlin.coroutines.CoroutineContext

class NewScreenDialog(project: Project, currentPath: CurrentPath?) : DialogWrapper(true), CoroutineScope {

    @Inject
    lateinit var viewModel: NewScreenViewModel

    private val job = SupervisorJob()
    private val panel = NewScreenPanel()

    override val coroutineContext: CoroutineContext = Dispatchers.UI + job

    init {
        DaggerNewScreenComponent.factory().create(project, currentPath).inject(this)
        launch {
            viewModel.state.map { it.toUiModel() }.collect {
                panel.render(it)
            }
        }
        launch {
            viewModel.effect.collect {
                handleEffect(it)
            }
        }
        panel.onCategoryIndexChanged = {
            viewModel.setAction(NewScreenView.NewScreenAction.SelectCategory(it))
        }
        init()
    }

    private fun NewScreenStore.State.toUiModel(): NewScreenView.NewScreenUiModel {
        return NewScreenView.NewScreenUiModel(
            selectedModuleName = this.selectedModule?.nameWithoutPrefix,
            selectedCategory = this.selectedCategory,
            categoriesList = this.categoriesList,
        )
    }

    private fun handleEffect(effect: NewScreenStore.Effect) {
        when (effect) {
            NewScreenStore.Effect.Close -> close(OK_EXIT_CODE)
        }
    }

    override fun createCenterPanel(): JComponent = panel

    override fun doOKAction() {
        launch {
            viewModel.setAction(
                NewScreenView.NewScreenAction.OkClicked(
                    screenName = panel.nameTextField.text,
                )
            )
        }
    }

    override fun dispose() {
        job.cancel()
        viewModel.clear()
        super.dispose()
    }
}