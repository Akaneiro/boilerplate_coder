package ru.akaneiro.boilerplatecoder.model

import java.io.Serializable

private const val DEFAULT_CATEGORY_NAME = "UnnamedCategory"

data class Category(
    var id: Int = 0,
    var name: String = "",
    var screenElements: MutableList<ScreenElement> = mutableListOf(),
) : Serializable {

    override fun toString() = name

    companion object {
        fun getDefault(id: Int) = Category(
            id = id,
            name = DEFAULT_CATEGORY_NAME,
            screenElements = mutableListOf(),
        )
    }
}