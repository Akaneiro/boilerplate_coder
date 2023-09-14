package ru.akaneiro.boilerplatecoder.settings.ui

import com.intellij.openapi.project.Project
import com.intellij.ui.JBSplitter
import com.intellij.ui.components.labels.LinkLabel
import org.jdesktop.swingx.VerticalLayout
import ru.akaneiro.boilerplatecoder.settings.ui.widget.*
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.FlowLayout
import javax.swing.JPanel
import javax.swing.JSeparator
import javax.swing.SwingConstants

class SettingsScreenPanel(project: Project) : JPanel() {

    companion object {
        private const val IMPORT_SETTINGS_LABEL_TEXT = "Import Settings"
        private const val EXPORT_SETTINGS_LABEL_TEXT = "Export Settings"
        private const val HELP_LABEL_TEXT = "Help"
    }

    val categoriesListPanel = CategoriesListPanel()
    val categoryDetailsPanel = CategoryDetailsPanel()
    val screenElementsListPanel = ScreenElementsListPanel()
    val screenElementDetailsPanel = ScreenElementDetailsPanel()
    val codePanel = CodePanel(project)

    var onImportSettingsClick: (() -> Unit)? = null
    var onExportSettingsClick: (() -> Unit)? = null
    var onHelpClick: (() -> Unit)? = null

    init {
        layout = BorderLayout()
        val importSettingsPanel = JPanel(FlowLayout(FlowLayout.TRAILING)).apply {
            add(LinkLabel.create(IMPORT_SETTINGS_LABEL_TEXT) { onImportSettingsClick?.invoke() })
            add(LinkLabel.create(EXPORT_SETTINGS_LABEL_TEXT) { onExportSettingsClick?.invoke() })
            add(JSeparator(SwingConstants.VERTICAL))
            add(LinkLabel.create(HELP_LABEL_TEXT) { onHelpClick?.invoke() })
        }
        val mainPanel = JPanel().apply {
            layout = BorderLayout()
            val contentPanel = JPanel().apply {
                layout = VerticalLayout()
                add(
                    JBSplitter(0.5f).apply {
                        firstComponent = categoriesListPanel
                        secondComponent = categoryDetailsPanel
                    }
                )
                add(
                    JBSplitter(0.5f).apply {
                        firstComponent = screenElementsListPanel
                        secondComponent = screenElementDetailsPanel
                    }
                )
                add(codePanel)
            }
            add(contentPanel)
        }
        add(importSettingsPanel, BorderLayout.PAGE_START)
        add(mainPanel, BorderLayout.CENTER)
    }

    fun render(state: SettingsView.SettingsUiModel) {
        categoriesListPanel.render(state)
        categoryDetailsPanel.render(state)
        screenElementsListPanel.render(state)
        screenElementDetailsPanel.render(state)
        codePanel.render(state)
    }

    override fun getPreferredSize(): Dimension {
        val original = super.getPreferredSize()
        return Dimension(600, original.height)
    }
}