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
    private val mode = JComboBox(arrayOf("manual", "random"))
    private val color = JComboBox(ChocoboPainter.COLORS.toTypedArray())

    override fun getDisplayName() = "Chocobo Progress"
    override fun createComponent(): JComponent {
        val form = JPanel(GridBagLayout())
        val labels = arrayOf("Color selection:", "Chocobo color:")
        val fields = arrayOf<JComponent>(mode, color)
        for (i in fields.indices) {
            form.add(JLabel(labels[i]), GridBagConstraints().apply {
                gridx = 0; gridy = i; anchor = GridBagConstraints.LINE_START
                insets = Insets(4, 0, 4, 12)
            })
            form.add(fields[i], GridBagConstraints().apply {
                gridx = 1; gridy = i; anchor = GridBagConstraints.LINE_START
                fill = GridBagConstraints.NONE; weightx = 0.0
                insets = Insets(4, 0, 4, 0)
            })
        }
        mode.addActionListener { color.isEnabled = mode.selectedItem == "manual" }
        panel = JPanel(BorderLayout()).apply { add(form, BorderLayout.NORTH) }
        reset()
        return panel!!
    }
    override fun isModified(): Boolean {
        val s = settings.snapshot()
        return mode.selectedItem != s.mode || color.selectedItem != s.color
    }
    override fun apply() {
        settings.update(mode.selectedItem as String, color.selectedItem as String)
        ChocoboInstaller.refreshProgressBars()
    }
    override fun reset() {
        val s = settings.snapshot()
        mode.selectedItem = s.mode
        color.selectedItem = s.color
        color.isEnabled = s.mode == "manual"
    }
    override fun disposeUIResources() { panel = null }
}
