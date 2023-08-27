package ru.akaneiro.boilerplatecoder.model

data class Settings(
    var screenElements: MutableList<ScreenElement> = mutableListOf(),
    var categories: MutableList<Category> = mutableListOf()
): java.io.Serializable