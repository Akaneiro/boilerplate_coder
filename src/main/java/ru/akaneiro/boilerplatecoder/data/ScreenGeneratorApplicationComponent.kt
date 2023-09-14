package ru.akaneiro.boilerplatecoder.data

import com.intellij.openapi.components.*
import com.intellij.util.xmlb.XmlSerializerUtil
import ru.akaneiro.boilerplatecoder.model.Settings
import java.io.Serializable

const val SCREEN_GENERATOR_CONFIG_NAME = "ScreenGeneratorConfig"
const val SCREEN_GENERATOR_CONFIG_STORAGE_FILE = "screenGeneratorConfigStorage.xml"

@State(
    name = SCREEN_GENERATOR_CONFIG_NAME,
    storages = [Storage(SCREEN_GENERATOR_CONFIG_STORAGE_FILE)]
)
class ScreenGeneratorApplicationComponent :
    PersistentStateComponent<ScreenGeneratorApplicationComponent>,
    Serializable {

    companion object {
        fun getInstance(): ScreenGeneratorApplicationComponent =
            ServiceManager.getService(ScreenGeneratorApplicationComponent::class.java)
    }

    var settings: Settings = Settings()

    override fun getState(): ScreenGeneratorApplicationComponent = this

    override fun loadState(state: ScreenGeneratorApplicationComponent) {
        XmlSerializerUtil.copyBean(state, this)
    }
}