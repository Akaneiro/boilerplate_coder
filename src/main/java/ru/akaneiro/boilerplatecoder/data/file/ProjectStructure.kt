package ru.akaneiro.boilerplatecoder.data.file

import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.project.Project
import org.jetbrains.kotlin.idea.util.sourceRoots
import ru.akaneiro.boilerplatecoder.model.Module
import javax.inject.Inject

class ProjectStructure @Inject constructor(private val project: Project) {

    fun findSourceRoots(module: Module, sourceSet: String): List<SourceRoot> =
        ModuleManager.getInstance(project)
            .modules
            .filter { it.name == "${module.name}.$sourceSet" }
            .flatMap { it.sourceRoots.toList() }
            .map { SourceRoot(project, it) }

    fun getProjectPath() = project.basePath ?: ""
}