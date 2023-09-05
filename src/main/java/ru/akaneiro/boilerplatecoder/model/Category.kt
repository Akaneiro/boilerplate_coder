package ru.akaneiro.boilerplatecoder.model

import java.io.Serializable

const val DEFAULT_CATEGORY_NAME = "Unnamed Group"

data class Category(
    var name: String = "",
    var screenElements: MutableList<ScreenElement> = mutableListOf(),
) : Serializable {

    override fun toString() = name

    companion object {
        fun getDefault() = Category(
            name = DEFAULT_CATEGORY_NAME,
            screenElements = mutableListOf(),
        )
    }
}