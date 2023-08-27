package ru.akaneiro.boilerplatecoder.data.file

import ru.akaneiro.boilerplatecoder.data.SourceRootRepository
import javax.inject.Inject

class PackageExtractor @Inject constructor(
    private val currentPath: CurrentPath?,
    private val sourceRootRepository: SourceRootRepository
) {

    fun extractFromCurrentPath(): String {
        val sourceRootPath = currentPath?.let { sourceRootRepository.findCodeSourceRoot(currentPath.module!!)?.path }
        val packageName = if (currentPath != null
            && sourceRootPath != null
            && currentPath.path != sourceRootPath
            && currentPath.path.contains(sourceRootPath)
        ) {
            currentPath.path.removePrefix("$sourceRootPath/")
                .removeFilePath(currentPath.isDirectory)
                .replace("/", ".")
        } else {
            ""
        }
        return packageName
    }

    private fun String.removeFilePath(isDirectory: Boolean) =
        if (!isDirectory) {
            removeRange(indexOfLast { it == '/' }, length)
        } else {
            this
        }
}
