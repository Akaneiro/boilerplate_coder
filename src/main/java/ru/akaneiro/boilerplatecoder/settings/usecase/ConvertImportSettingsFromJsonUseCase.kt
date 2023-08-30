package ru.akaneiro.boilerplatecoder.settings.usecase

import com.google.gson.Gson
import ru.akaneiro.boilerplatecoder.model.CategoryWithScreenElements
import ru.akaneiro.boilerplatecoder.model.Settings
import javax.inject.Inject

class ConvertImportSettingsFromJsonUseCase @Inject constructor(){

    fun invoke(jsonString: String): List<CategoryWithScreenElements> {
        val settingsToImport = Gson().fromJson(jsonString, Settings::class.java)
        return settingsToImport.categories.map { category ->
            CategoryWithScreenElements(
                category,
                settingsToImport.screenElements.filter { it.categoryId == category.id }
            )
        }
    }
}