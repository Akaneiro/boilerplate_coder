package ru.akaneiro.boilerplatecoder.settings.ui.widget

import com.intellij.ui.IdeBorderFactory
import org.jdesktop.swingx.VerticalLayout
import ru.akaneiro.boilerplatecoder.settings.ui.SettingsView
import ru.akaneiro.boilerplatecoder.widget.BasePanel
import ru.akaneiro.boilerplatecoder.widget.addTextChangeListener
import ru.akaneiro.boilerplatecoder.widget.updateText
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JTextField

class ScreenElementDetailsPanel : BasePanel<SettingsView.SettingsUiModel>() {

    companion object {
        private const val ELEMENT_NAME_LABEL_TEXT = "Element Name:"
        private const val FILE_NAME_LABEL_TEXT = "File Name:"
        private const val FILE_NAME_PREVIEW_LABEL_TEXT = "File Name Preview:"
        private const val SUBDIRECTORY_LABEL_TEXT = "Subdirectory:"
        private const val ELEMENT_DETAILS_LABEL_TEXT = "Element Details"
    }

    var onNameTextChanged: ((String) -> Unit)? = null
    var onFileNameTextChanged: ((String) -> Unit)? = null
    var onSubdirectoryTextChanged: ((String) -> Unit)? = null

    private val nameTextField = JTextField()
    private val fileNameTextField = JTextField()
    private val screenElementNameLabel = JLabel(ELEMENT_NAME_LABEL_TEXT)
    private val fileNameLabel = JLabel(FILE_NAME_LABEL_TEXT)
    private val fileNamePreviewLabel = JLabel(FILE_NAME_PREVIEW_LABEL_TEXT).apply {
        isEnabled = false
    }
    private val fileNamePreview = JTextField().apply {
        isEnabled = false
    }

    private val subdirectoryLabel = JLabel(SUBDIRECTORY_LABEL_TEXT)
    private val subdirectoryTextField = JTextField()

    private val fieldsList: List<Pair<JComponent, JComponent>> = listOf(
        screenElementNameLabel to nameTextField,
        fileNameLabel to fileNameTextField,
        fileNamePreviewLabel to fileNamePreview,
        subdirectoryLabel to subdirectoryTextField,
    )

    init {
        border = IdeBorderFactory.createTitledBorder(ELEMENT_DETAILS_LABEL_TEXT, false)
        layout = VerticalLayout()
        fieldsList.forEach {
            add(createDetailComponent(it.first, it.second))
        }

        nameTextField.addTextChangeListener { if (!listenersBlocked) onNameTextChanged?.invoke(it) }
        fileNameTextField.addTextChangeListener { if (!listenersBlocked) onFileNameTextChanged?.invoke(it) }
        subdirectoryTextField.addTextChangeListener { if (!listenersBlocked) onSubdirectoryTextChanged?.invoke(it) }
    }

    override fun performRender(model: SettingsView.SettingsUiModel) {
        val selectedElement = model.selectedElement
        nameTextField.updateText(selectedElement?.name ?: "")
        fileNameTextField.updateText(selectedElement?.filenameTemplate ?: "")
        fileNamePreview.updateText(model.renderedFileName ?: "")
        subdirectoryTextField.updateText(selectedElement?.subdirectory ?: "")

        fieldsList.forEach {
            it.first.isEnabled = selectedElement != null
            it.second.isEnabled = selectedElement != null
        }
        fieldsList.firstOrNull { it.first == fileNamePreviewLabel }?.let {
            it.first.isEnabled = false
            it.second.isEnabled = false
        }
    }
}