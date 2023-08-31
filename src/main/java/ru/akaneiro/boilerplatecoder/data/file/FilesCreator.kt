package ru.akaneiro.boilerplatecoder.data.file

import ru.akaneiro.boilerplatecoder.model.DEFAULT_SOURCE_SET
import ru.akaneiro.boilerplatecoder.model.Module
import ru.akaneiro.boilerplatecoder.data.SourceRootRepository
import ru.akaneiro.boilerplatecoder.model.Category
import javax.inject.Inject

class FilesCreator @Inject constructor(
    private val sourceRootRepository: SourceRootRepository,
) {

    fun createScreenFilesByCategory(
        packageName: String,
        screenName: String,
        module: Module,
        category: Category,
    ) {
        category.screenElements
            .forEach { screenElement ->
                val file = File(
                    name = screenElement.fileName(screenName, packageName),
                    content = screenElement.body(screenName, packageName),
                )

                val codeSubdirectory = findCodeSubdirectory(packageName, module)
                if (codeSubdirectory != null) {
                    addFile(codeSubdirectory, file, screenElement.subdirectory)
                }
            }
    }

    private fun addFile(directory: Directory, file: File, subdirectory: String) {
        if (subdirectory.isNotEmpty()) {
            var newSubdirectory = directory
            subdirectory.split("/").forEach { segment ->
                newSubdirectory = newSubdirectory.findSubdirectory(segment) ?: newSubdirectory.createSubdirectory(segment)
            }
            newSubdirectory.addFile(file)
        } else {
            directory.addFile(file)
        }
    }

    private fun findCodeSubdirectory(packageName: String, module: Module): Directory? =
        sourceRootRepository.findCodeSourceRoot(module, DEFAULT_SOURCE_SET)?.run {
            var subdirectory = directory
            packageName.split(".").forEach {
                subdirectory = subdirectory.findSubdirectory(it) ?: subdirectory.createSubdirectory(it)
            }
            return subdirectory
        }
}