package ru.akaneiro.boilerplatecoder.model

data class Module(
    val name: String,
    val nameWithoutPrefix: String
) {
    override fun toString() = nameWithoutPrefix
}
