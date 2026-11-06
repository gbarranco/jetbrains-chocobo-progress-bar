package dev.chocobo.progress

import java.awt.Dimension
import java.awt.Graphics
import java.awt.Graphics2D
import javax.swing.JPanel
import javax.swing.JProgressBar
import javax.swing.Timer

/**
 * A self-contained preview: it never reads or changes persisted settings.
 * Its timer is stopped when the settings UI is disposed.
 */
class ChocoboPreviewPanel : JPanel() {
    private val previewBar = JProgressBar().apply {
        isIndeterminate = true
        setSize(300, 40)
    }
    private val timer = Timer(55) { if (isShowing) repaint() }
    var chocoboColor: String = "yellow"
        set(value) { field = value; repaint() }
    var animationSpeed: AnimationSpeed = AnimationSpeed.NORMAL
        set(value) { field = value; repaint() }

    init {
        preferredSize = Dimension(320, 48)
        isOpaque = false
        timer.start()
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        previewBar.setSize(width.coerceAtLeast(1), height.coerceAtLeast(1))
        val copy = g.create() as Graphics2D
        try {
            ChocoboPainter.paint(copy, previewBar, chocoboColor, animationSpeed.frameDelayMs)
        } finally {
            copy.dispose()
        }
    }

    fun dispose() {
        timer.stop()
    }
}
