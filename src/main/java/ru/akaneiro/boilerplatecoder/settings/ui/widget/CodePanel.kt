package ru.akaneiro.boilerplatecoder.settings.ui.widget

import com.intellij.openapi.project.Project
import com.intellij.ui.IdeBorderFactory
import com.intellij.ui.LanguageTextField
import com.intellij.ui.components.JBScrollPane
import org.jetbrains.kotlin.idea.KotlinLanguage
import ru.akaneiro.boilerplatecoder.settings.ui.SettingsView
import ru.akaneiro.boilerplatecoder.widget.BasePanel
import ru.akaneiro.boilerplatecoder.widget.addTextChangeListener
import ru.akaneiro.boilerplatecoder.widget.updateText
import java.awt.Dimension
import java.awt.GridLayout
import javax.swing.BoxLayout
import javax.swing.JPanel

class CodePanel(private val project: Project) : BasePanel<SettingsView.SettingsUiModel>() {

    private companion object {
        private const val CODE_TEMPLATE_LABEL_TEXT = "Code Template"
        private const val SAMPLE_CODE_LABEL_TEXT = "Sample Code"
    }

    var onTemplateTextChanged: ((String) -> Unit)? = null
    private val templateTextField = createTemplateTextField()
    private val sampleTextField = createSampleTextField()

    init {
        layout = GridLayout(2, 1)
        val templatePanel = createTemplatePanel()
        val samplePanel = createSamplePanel()
        add(templatePanel)
        add(samplePanel)
        templateTextField.addTextChangeListener { if (!listenersBlocked) onTemplateTextChanged?.invoke(it) }
    }

    private fun createTemplatePanel() =
        JPanel().apply {
            border = IdeBorderFactory.createTitledBorder(CODE_TEMPLATE_LABEL_TEXT, false)
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            add(JBScrollPane(templateTextField))
        }

    private fun createSamplePanel() =
        JPanel().apply {
            border = IdeBorderFactory.createTitledBorder(SAMPLE_CODE_LABEL_TEXT, false)
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            add(JBScrollPane(sampleTextField))
        }

    private fun createTemplateTextField() =
        LanguageTextField(KotlinLanguage.INSTANCE, project, "", false)

    private fun createSampleTextField() =
        LanguageTextField(KotlinLanguage.INSTANCE, project, "", false).apply {
            isEnabled = false
        }

    override fun performRender(model: SettingsView.SettingsUiModel) {
        val template = model.selectedElementTemplate
        val renderedSampleCode = model.renderedSampleCode
        if (template != null && renderedSampleCode != null) {
            setEnabledAll(true)
            isVisible = true
            templateTextField.updateText(template)
            sampleTextField.updateText(renderedSampleCode)
        } else {
            isVisible = false
            setEnabledAll(false)
        }
    }

    private fun setEnabledAll(isEnabled: Boolean) {
        templateTextField.isEnabled = isEnabled
    }

    override fun getPreferredSize(): Dimension {
        val original = super.getPreferredSize()
        return Dimension(original.width, 600)
    }
}
