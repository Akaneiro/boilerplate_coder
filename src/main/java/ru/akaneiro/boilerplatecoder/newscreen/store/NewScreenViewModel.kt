package ru.akaneiro.boilerplatecoder.newscreen.store

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ru.akaneiro.boilerplatecoder.base.BaseViewModel
import ru.akaneiro.boilerplatecoder.data.file.CurrentPath
import ru.akaneiro.boilerplatecoder.data.file.PackageExtractor
import ru.akaneiro.boilerplatecoder.model.Category
import ru.akaneiro.boilerplatecoder.newscreen.ui.CheckboxListItem
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
            val selectedCategory = categories.firstOrNull()
            val selectedScreenElements = selectedCategory?.screenElementsAsCheckableItems() ?: listOf()
            setState {
                copy(
                    selectedCategory = categories.firstOrNull(),
                    categoriesList = categories,
                    screenElements = selectedScreenElements,
                )
            }
        }
    }

    override fun setInitialState(): NewScreenStore.State {
        return NewScreenStore.State()
    }

    override fun handleActions(action: NewScreenView.NewScreenAction) {
        when (action) {
            NewScreenView.NewScreenAction.OkClicked -> {
                val screenName = getState().screenName
                val screenElements = getState().screenElements
                currentPath?.module?.let { module ->
                    runBlocking {
                        writeFilesUseCase.invoke(
                            packageName = packageExtractor.extractFromCurrentPath(),
                            screenName = screenName,
                            module = module,
                            screenElements = screenElements.filter { it.isSelected }.map { it.screenElement },
                        )
                        setEffect { NewScreenStore.Effect.Close }
                    }
                }
            }

            is NewScreenView.NewScreenAction.SelectCategory -> {
                setState {
                    val selectedCategory = categoriesList[action.categoryIndex]
                    val screenElements = selectedCategory.screenElementsAsCheckableItems()
                    copy(selectedCategory = selectedCategory, screenElements = screenElements)
                }
            }

            is NewScreenView.NewScreenAction.ScreenNameChanged -> {
                setState {
                    copy(screenName = action.screenName)
                }
            }

            is NewScreenView.NewScreenAction.ScreenElementClick -> {
                setState {
                    val newScreenElements = screenElements.map { CheckboxListItem(it.screenElement, it.isSelected) }
                    newScreenElements.find { it.screenElement == action.screenElement }?.let {
                        it.isSelected = !it.isSelected
                    }
                    copy(screenElements = newScreenElements)
                }
            }
        }
    }

    private fun Category.screenElementsAsCheckableItems() =
        screenElements.map { CheckboxListItem(it).apply { isSelected = true } }
}