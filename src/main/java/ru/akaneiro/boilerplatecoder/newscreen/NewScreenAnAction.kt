package ru.akaneiro.boilerplatecoder.newscreen

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformCoreDataKeys
import com.intellij.openapi.module.ModuleUtil
import ru.akaneiro.boilerplatecoder.data.file.CurrentPath
import ru.akaneiro.boilerplatecoder.model.Module
import ru.akaneiro.boilerplatecoder.newscreen.ui.NewScreenDialog

class NewScreenAnAction : AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val currentPath = event.getData(PlatformCoreDataKeys.VIRTUAL_FILE)?.let {
            val moduleName =
                ModuleUtil.findModuleForFile(it, event.project!!)?.name?.replaceAfterLastInclusive(".", "") ?: ""
            val module =
                Module(moduleName, moduleName.replace("${event.project!!.name}.", ""))
            CurrentPath(it.path, it.isDirectory, module)
        }
        NewScreenDialog(project = event.project!!, currentPath = currentPath).show()
    }

    private fun String.replaceAfterLastInclusive(delimiter: String, replacement: String): String {
        val index = lastIndexOf(delimiter)
        return if (index == -1) this else replaceRange(index, length, replacement)
    }
}