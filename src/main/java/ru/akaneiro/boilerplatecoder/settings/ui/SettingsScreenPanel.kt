package ru.akaneiro.boilerplatecoder.settings.ui

import com.intellij.openapi.project.Project
import com.intellij.ui.JBSplitter
import com.intellij.ui.components.labels.LinkLabel
import ru.akaneiro.boilerplatecoder.settings.ui.widget.*
import java.awt.BorderLayout
import java.awt.FlowLayout
import javax.swing.BoxLayout
import javax.swing.JPanel

class SettingsScreenPanel(project: Project): JPanel() {

    companion object {
        private const val IMPORT_SETTINGS_LABEL_TEXT = "Import Settings"
        private const val EXPORT_SETTINGS_LABEL_TEXT = "Export Settings"
    }

    val categoriesPanel = CategoriesPanel()
    val categoryDetailsPanel = CategoryDetailsPanel()
    val screenElementsPanel = ScreenElementsPanel()
    val screenElementDetailsPanel = ScreenElementDetailsPanel()
    val codePanel = CodePanel(project)

    var onImportSettingsClick: (() -> Unit)? = null
    var onExportSettingsClick: (() -> Unit)? = null

    init {
        layout = BorderLayout()
        val importSettingsPanel = JPanel(FlowLayout(FlowLayout.TRAILING)).apply {
            add(LinkLabel.create(IMPORT_SETTINGS_LABEL_TEXT) { onImportSettingsClick?.invoke() })
            add(LinkLabel.create(EXPORT_SETTINGS_LABEL_TEXT) { onExportSettingsClick?.invoke() })
        }
        val mainPanel = JPanel().apply {
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            add(
                JBSplitter(0.3f).apply {
                    firstComponent = categoriesPanel
                    secondComponent = categoryDetailsPanel
                }
            )
            add(
                JBSplitter(0.3f).apply {
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
        add(importSettingsPanel, BorderLayout.PAGE_START)
        add(mainPanel, BorderLayout.CENTER)
    }

    fun render(state: SettingsView.SettingsUiModel) {
        categoriesPanel.render(state)
        categoryDetailsPanel.render(state)
        screenElementsPanel.render(state)
        screenElementDetailsPanel.render(state)
        codePanel.render(state)
    }
}