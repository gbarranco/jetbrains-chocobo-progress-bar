package dev.chocobo.progress

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.options.Configurable
import java.awt.BorderLayout
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.Insets
import javax.swing.*

class ChocoboConfigurable : Configurable {
    private val settings get() = ApplicationManager.getApplication().getService(ChocoboSettings::class.java)
    private var panel: JPanel? = null
    private var preview: ChocoboPreviewPanel? = null
    private val mode = JComboBox(arrayOf("manual", "random"))
    private val color = JComboBox(ChocoboPainter.COLORS.toTypedArray())
    private val speed = JComboBox(AnimationSpeed.entries.toTypedArray())

    override fun getDisplayName() = "Chocobo Progress"

    override fun createComponent(): JComponent {
        val form = JPanel(GridBagLayout())
        val labels = arrayOf("Color selection:", "Chocobo color:", "Animation speed:")
        val fields = arrayOf<JComponent>(mode, color, speed)
        for (i in fields.indices) {
            form.add(JLabel(labels[i]), GridBagConstraints().apply {
                gridx = 0; gridy = i; anchor = GridBagConstraints.LINE_START
                insets = Insets(4, 0, 4, 12)
            })
            form.add(fields[i], GridBagConstraints().apply {
                gridx = 1; gridy = i; anchor = GridBagConstraints.LINE_START
                fill = GridBagConstraints.NONE
                insets = Insets(4, 0, 4, 0)
            })
        }
        preview = ChocoboPreviewPanel()
        form.add(JLabel("Live preview:"), GridBagConstraints().apply {
            gridx = 0; gridy = fields.size; anchor = GridBagConstraints.LINE_START
            insets = Insets(4, 0, 4, 12)
        })
        form.add(preview, GridBagConstraints().apply {
            gridx = 1; gridy = fields.size; anchor = GridBagConstraints.LINE_START
            fill = GridBagConstraints.HORIZONTAL; weightx = 1.0
            insets = Insets(4, 0, 4, 0)
        })
        mode.addActionListener {
            color.isEnabled = mode.selectedItem == "manual"
            updatePreview()
        }
        color.addActionListener { updatePreview() }
        speed.addActionListener { updatePreview() }
        panel = JPanel(BorderLayout().apply { hgap = 8; vgap = 8 }).apply {
            add(form, BorderLayout.NORTH)
        }
        reset()
        return panel!!
    }

    private fun updatePreview() {
        preview?.chocoboColor = if (mode.selectedItem == "random") "yellow" else color.selectedItem as String
        preview?.animationSpeed = speed.selectedItem as AnimationSpeed
    }

    override fun isModified(): Boolean {
        val s = settings.snapshot()
        return mode.selectedItem != s.mode ||
            color.selectedItem != s.color ||
            (speed.selectedItem as AnimationSpeed).name != s.speed
    }

    override fun apply() {
        settings.update(mode.selectedItem as String, color.selectedItem as String, (speed.selectedItem as AnimationSpeed).name)
        ChocoboInstaller.refreshProgressBars()
    }

    override fun reset() {
        val s = settings.snapshot()
        mode.selectedItem = s.mode
        color.selectedItem = s.color
        speed.selectedItem = AnimationSpeed.valueOf(s.speed)
        color.isEnabled = s.mode == "manual"
        updatePreview()
    }

    override fun disposeUIResources() {
        preview?.dispose()
        preview = null
        panel = null
    }
}
