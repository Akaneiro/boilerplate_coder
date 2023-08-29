package ru.akaneiro.boilerplatecoder.settings.ui.widget

import com.intellij.ui.IdeBorderFactory
import ru.akaneiro.boilerplatecoder.settings.ui.SettingsView
import ru.akaneiro.boilerplatecoder.widget.*
import java.awt.GridBagLayout
import javax.swing.JLabel
import javax.swing.JPanel
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

    init {
        border = IdeBorderFactory.createTitledBorder(ELEMENT_DETAILS_LABEL_TEXT, false)
        layout = GridBagLayout()
        add(screenElementNameLabel, constraintsLeft(0, 0))
        add(nameTextField, constraintsRight(1, 0))
        add(fileNameLabel, constraintsLeft(0, 1))
        add(fileNamePreviewLabel, constraintsLeft(0, 2))
        add(fileNamePreview, constraintsRight(1, 2))
        add(fileNameTextField, constraintsRight(1, 1))
        add(subdirectoryLabel, constraintsLeft(0, 4))
        add(subdirectoryTextField, constraintsRight(1, 4))

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

        isEnabled = selectedElement != null
        components.filter { it != fileNamePreview && it != fileNamePreviewLabel }
            .forEach { it.isEnabled = selectedElement != null }
    }
}