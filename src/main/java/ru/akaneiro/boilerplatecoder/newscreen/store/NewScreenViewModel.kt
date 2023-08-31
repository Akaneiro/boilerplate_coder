package ru.akaneiro.boilerplatecoder.newscreen.store

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ru.akaneiro.boilerplatecoder.base.BaseViewModel
import ru.akaneiro.boilerplatecoder.data.file.CurrentPath
import ru.akaneiro.boilerplatecoder.data.file.PackageExtractor
import ru.akaneiro.boilerplatecoder.newscreen.ui.NewScreenView
import ru.akaneiro.boilerplatecoder.newscreen.usecase.WriteFilesUseCase
import ru.akaneiro.boilerplatecoder.settings.usecase.LoadCategoriesUseCase
import javax.inject.Inject

class NewScreenViewModel @Inject constructor(
    private val currentPath: CurrentPath?,
    private val packageExtractor: PackageExtractor,
    private val writeFilesUseCase: WriteFilesUseCase,
    private val loadCategoriesUseCase: LoadCategoriesUseCase,
) : BaseViewModel<NewScreenStore.State, NewScreenStore.Effect, NewScreenView.NewScreenAction>() {

    init {
        setState {
            copy(selectedModule = currentPath?.module)
        }
        launch {
            val categories = loadCategoriesUseCase.invoke()
            setState {
                copy(
                    selectedCategory = categories.firstOrNull(),
                    categoriesList = categories,
                )
            }
        }
    }

    override fun setInitialState(): NewScreenStore.State {
        return NewScreenStore.State()
    }

    override fun handleActions(action: NewScreenView.NewScreenAction) {
        when (action) {
            is NewScreenView.NewScreenAction.OkClicked -> {
                currentPath?.module?.let { module ->
                    getState().selectedCategory?.let { selectedCategory ->
                        runBlocking {
                            writeFilesUseCase.invoke(
                                packageName = packageExtractor.extractFromCurrentPath(),
                                screenName = action.screenName,
                                module = module,
                                category = selectedCategory,
                            )
                            setEffect { NewScreenStore.Effect.Close }
                        }
                    }
                }
            }

            is NewScreenView.NewScreenAction.SelectCategory -> {
                setState {
                    copy(selectedCategory = categoriesList[action.categoryIndex])
                }
            }
        }
    }
}