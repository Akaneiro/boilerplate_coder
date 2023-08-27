package ru.akaneiro.boilerplatecoder.settings.ui.widget

import com.intellij.openapi.project.Project
import com.intellij.ui.EditorTextField
import com.intellij.ui.IdeBorderFactory
import com.intellij.ui.LanguageTextField
import com.intellij.ui.components.JBScrollPane
import org.jetbrains.kotlin.idea.KotlinLanguage
import ru.akaneiro.boilerplatecoder.settings.ui.SettingsUiModel
import ru.akaneiro.boilerplatecoder.widget.addTextChangeListener
import ru.akaneiro.boilerplatecoder.widget.updateText
import java.awt.Dimension
import java.awt.GridLayout
import javax.swing.BoxLayout
import javax.swing.JPanel

class CodePanel(
    private val project: Project,
) : JPanel() {

    var onTemplateTextChanged: ((String) -> Unit)? = null
    private val templateTextField = createTemplateTextField()
    private val sampleTextField = createSampleTextField()
    private var listenersBlocked = false

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
            border = IdeBorderFactory.createTitledBorder("Code Template", false)
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            add(JBScrollPane(templateTextField))
        }

    private fun createSamplePanel() =
        JPanel().apply {
            border = IdeBorderFactory.createTitledBorder("Sample Code", false)
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            add(JBScrollPane(sampleTextField))
        }

    private fun createTemplateTextField() =
        LanguageTextField(KotlinLanguage.INSTANCE, project, "", false)

    private fun createSampleTextField() =
        EditorTextField().apply { isEnabled = false }

    fun render(model: SettingsUiModel) {
        listenersBlocked = true
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
        listenersBlocked = false
    }

    private fun setEnabledAll(isEnabled: Boolean) {
        templateTextField.isEnabled = isEnabled
    }

    override fun getPreferredSize(): Dimension {
        val original = super.getPreferredSize()
        return Dimension(original.width, 600)
    }
}
