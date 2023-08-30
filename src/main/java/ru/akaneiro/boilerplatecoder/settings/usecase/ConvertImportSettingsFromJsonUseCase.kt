package ru.akaneiro.boilerplatecoder.settings.usecase

import com.google.gson.Gson
import ru.akaneiro.boilerplatecoder.model.Category
import ru.akaneiro.boilerplatecoder.model.Settings
import javax.inject.Inject

class ConvertImportSettingsFromJsonUseCase @Inject constructor() {

    fun invoke(jsonString: String): List<Category> {
        val settingsToImport = Gson().fromJson(jsonString, Settings::class.java)
        return settingsToImport.categories
    }
}