package dev.chocobo.progress

import com.intellij.openapi.application.ApplicationManager
import java.awt.Graphics
import java.awt.Graphics2D
import javax.swing.JComponent
import javax.swing.plaf.basic.BasicProgressBarUI
import javax.swing.Timer

class ChocoboProgressBarUI : BasicProgressBarUI() {
    private var animation: Timer? = null
    private var chosenColor: String? = null

    override fun installUI(c: JComponent) {
        super.installUI(c)
        chosenColor = null
        animation = Timer(110) {
            if (progressBar.isShowing) progressBar.repaint()
        }.also { it.start() }
    }

    override fun uninstallUI(c: JComponent) {
        animation?.stop()
        animation = null
        chosenColor = null
        super.uninstallUI(c)
    }

    override fun paint(g: Graphics, c: JComponent) {
        val app = ApplicationManager.getApplication()
        if (app == null || app.isDisposed) {
            super.paint(g, c)
            return
        }
        val copy = g.create() as Graphics2D
        try {
            val settings = app.getService(ChocoboSettings::class.java)
            // Pick once per progress bar, not once per animation frame.
            val selected = chosenColor ?: settings.selectedColor().also { chosenColor = it }
            ChocoboPainter.paint(copy, progressBar, selected)
        } finally { copy.dispose() }
    }
}
