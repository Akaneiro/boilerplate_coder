package ru.akaneiro.boilerplatecoder.model

enum class DefaultVariable(val template: String) {
    PACKAGE_NAME("%PACKAGE_NAME%"),
    SCREEN_NAME("%SCREEN_NAME%");
}