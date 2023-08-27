package ru.akaneiro.boilerplatecoder.data.file

import ru.akaneiro.boilerplatecoder.model.Module

data class CurrentPath(
    val path: String,
    val isDirectory: Boolean,
    val module: Module?
)