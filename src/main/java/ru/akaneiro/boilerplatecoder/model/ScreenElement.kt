package ru.akaneiro.boilerplatecoder.model

import java.io.Serializable

const val SAMPLE_SCREEN_NAME = "Sample"
const val SAMPLE_PACKAGE_NAME = "com.sample"

const val DEFAULT_SOURCE_SET = "main"

const val DEFAULT_SCREEN_ELEMENT_NAME = "UnnamedElement"
const val DEFAULT_SCREEN_ELEMENT_FILE_NAME = "UnnamedElementFile"
val DEFAULT_SCREEN_ELEMENT_TEMPLATE = "package ${DefaultVariable.PACKAGE_NAME.template}"

data class ScreenElement(
    var name: String = "",
    var template: String = "",
    var filenameTemplate: String = "",
    var subdirectory: String = "",
) : Serializable {

    fun body(
        screenName: String,
        packageName: String,
    ): String {
        return template.replaceVariables(screenName, packageName)
    }

    fun fileName(screenName: String, packageName: String): String {
        return filenameTemplate.replaceVariables(screenName, packageName)
    }

    private fun String.replaceVariables(screenName: String, packageName: String): String {
        return this
            .replace(DefaultVariable.SCREEN_NAME.template, screenName)
            .replace(DefaultVariable.PACKAGE_NAME.template, packageName)
    }

    override fun toString(): String = name

    companion object {
        fun getDefault() = ScreenElement(
            name = DEFAULT_SCREEN_ELEMENT_NAME,
            filenameTemplate = DEFAULT_SCREEN_ELEMENT_FILE_NAME,
            template = DEFAULT_SCREEN_ELEMENT_TEMPLATE,
            subdirectory = "",
        )
    }
}

fun ScreenElement.renderSampleCode() =
    body(SAMPLE_SCREEN_NAME, SAMPLE_PACKAGE_NAME)

fun ScreenElement.renderFileName() =
    fileName(SAMPLE_SCREEN_NAME, SAMPLE_PACKAGE_NAME)