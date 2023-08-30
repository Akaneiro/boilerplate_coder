package ru.akaneiro.boilerplatecoder.model

data class Settings(
    var categories: MutableList<Category> = mutableListOf()
): java.io.Serializable