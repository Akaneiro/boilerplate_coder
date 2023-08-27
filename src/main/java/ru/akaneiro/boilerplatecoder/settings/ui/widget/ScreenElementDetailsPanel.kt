package ru.akaneiro.boilerplatecoder.settings.ui.widget

import com.intellij.ui.IdeBorderFactory
import ru.akaneiro.boilerplatecoder.settings.ui.SettingsUiModel
import ru.akaneiro.boilerplatecoder.widget.addTextChangeListener
import ru.akaneiro.boilerplatecoder.widget.constraintsLeft
import ru.akaneiro.boilerplatecoder.widget.constraintsRight
import ru.akaneiro.boilerplatecoder.widget.updateText
import java.awt.GridBagLayout
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField

class ScreenElementDetailsPanel : JPanel() {

    var onNameTextChanged: ((String) -> Unit)? = null
    var onFileNameTextChanged: ((String) -> Unit)? = null
    var onSubdirectoryTextChanged: ((String) -> Unit)? = null

    private val nameTextField = JTextField()
    private val fileNameTextField = JTextField()
    private val screenElementNameLabel = JLabel("Element Name:")
    private val fileNameLabel = JLabel("File Name:")
    private val fileNamePreviewLabel = JLabel("File Name Preview:").apply {
        isEnabled = false
    }
    private val fileNamePreview = JTextField().apply {
        isEnabled = false
    }

    private val subdirectoryLabel = JLabel("Subdirectory:")
    private val subdirectoryTextField = JTextField()

    private var listenersBlocked = false

    init {
        border = IdeBorderFactory.createTitledBorder("Element Details", false)
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

    fun render(model: SettingsUiModel) {
        listenersBlocked = true
        val selectedElement = model.selectedElement
        nameTextField.updateText(selectedElement?.name ?: "")
        fileNameTextField.updateText(selectedElement?.filenameTemplate ?: "")
        fileNamePreview.updateText(model.renderedFileName ?: "")
        subdirectoryTextField.updateText(selectedElement?.subdirectory ?: "")

        isEnabled = selectedElement != null
        components.filter { it != fileNamePreview && it != fileNamePreviewLabel }
            .forEach { it.isEnabled = selectedElement != null }
        listenersBlocked = false
    }
}