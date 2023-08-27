package ru.akaneiro.boilerplatecoder.data

import ru.akaneiro.boilerplatecoder.data.file.ProjectStructure
import ru.akaneiro.boilerplatecoder.data.file.SourceRoot
import ru.akaneiro.boilerplatecoder.model.DEFAULT_SOURCE_SET
import ru.akaneiro.boilerplatecoder.model.Module
import javax.inject.Inject

class SourceRootRepository @Inject constructor(
    private val projectStructure: ProjectStructure,
) {

    fun findCodeSourceRoot(module: Module, sourceSet: String = DEFAULT_SOURCE_SET): SourceRoot? {
        val allSourceRoots = projectStructure.findSourceRoots(module, sourceSet).filter {
            val pathTrimmed = it.path.removeModulePathPrefix(module)
            pathTrimmed.contains("src", true) &&
                    pathTrimmed.contains(sourceSet) &&
                    !pathTrimmed.contains("/assets", true) &&
                    !pathTrimmed.contains("/res", true)
        }
        return allSourceRoots.firstOrNull()
    }

    private fun String.removeModulePathPrefix(module: Module) =
        removePrefix(projectStructure.getProjectPath() + "/" + module.nameWithoutPrefix.replace(".", "/"))
}