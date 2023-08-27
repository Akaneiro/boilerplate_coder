package ru.akaneiro.boilerplatecoder.settings.ui

import com.intellij.openapi.project.Project
import com.intellij.ui.JBSplitter
import ru.akaneiro.boilerplatecoder.settings.ui.widget.*
import java.awt.BorderLayout
import javax.swing.BoxLayout
import javax.swing.JPanel

class SettingsScreenPanel(project: Project): JPanel() {

    val categoriesPanel = CategoriesPanel()
    val categoryDetailsPanel = CategoryDetailsPanel()
    val screenElementsPanel = ScreenElementsPanel()
    val screenElementDetailsPanel = ScreenElementDetailsPanel()
    val codePanel = CodePanel(project)

    init {
        layout = BorderLayout()
        val mainPanel = JPanel().apply {
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            add(
                JBSplitter(0.5f).apply {
                    firstComponent = categoriesPanel
                    secondComponent = categoryDetailsPanel
                }
            )
            add(
                JBSplitter(0.5f).apply {
                    firstComponent = screenElementsPanel
                    secondComponent = screenElementDetailsPanel
                }
            )
            add(
                JPanel().apply {
                    layout = BoxLayout(this, BoxLayout.Y_AXIS)
                    add(codePanel)
                }
            )
        }
        add(mainPanel)
    }

    fun render(state: SettingsUiModel) {
        categoriesPanel.render(state)
        categoryDetailsPanel.render(state)
        screenElementsPanel.render(state)
        screenElementDetailsPanel.render(state)
        codePanel.render(state)
    }
}