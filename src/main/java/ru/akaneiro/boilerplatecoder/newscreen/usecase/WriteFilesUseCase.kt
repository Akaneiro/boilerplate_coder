package ru.akaneiro.boilerplatecoder.newscreen.usecase

import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.project.Project
import ru.akaneiro.boilerplatecoder.data.file.FilesCreator
import ru.akaneiro.boilerplatecoder.model.Category
import ru.akaneiro.boilerplatecoder.model.Module
import javax.inject.Inject

private const val COMMAND_NAME = "Screen Generator"
private const val GROUP_ID = "SCREEN_GENERATOR_ID"

class WriteFilesUseCase @Inject constructor(
    private val project: Project,
    private val filesCreator: FilesCreator,
) {

    operator fun invoke(packageName: String, screenName: String, module: Module, category: Category) {
        WriteCommandAction.runWriteCommandAction(project, COMMAND_NAME, GROUP_ID, {
            filesCreator.createScreenFilesByCategory(packageName, screenName, module, category)
        })
    }
}