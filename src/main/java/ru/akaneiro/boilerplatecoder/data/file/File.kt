package ru.akaneiro.boilerplatecoder.data.file

data class File(
    val name: String,
    val content: String,
)

object KotlinFileParams {
    const val FILE_EXTENSION = "kt"
}