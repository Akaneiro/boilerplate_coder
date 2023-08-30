package ru.akaneiro.boilerplatecoder.model

enum class DefaultVariable(val template: String, val description: String) {
    PACKAGE_NAME("%PACKAGE_NAME%","Full package name"),
    SCREEN_NAME("%SCREEN_NAME%", "The name of the screen, for example, in MainNavigationFragment it is MainNavigation");
}