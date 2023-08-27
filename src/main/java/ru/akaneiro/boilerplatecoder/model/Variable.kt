package ru.akaneiro.boilerplatecoder.model

data class Variable(
    val template: String,
) {

    companion object {
        private const val DEFAULT_NAME = "DefaultVariable"

        fun getDefaultVariable() = "DefaultVariable"
    }
}