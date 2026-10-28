package dev.chocobo.progress

import com.intellij.openapi.diagnostic.Logger
import java.awt.*
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import javax.swing.JProgressBar

object ChocoboPainter {
    val COLORS = listOf("blue", "green", "yellow", "purple", "red", "orange")
    private val rgb = mapOf(
        "blue" to Color(0x00B4FF), "green" to Color(0x2ECC71),
        "yellow" to Color(0xF4D03F), "purple" to Color(0x8B5CF6),
        "red" to Color(0xE91E63), "orange" to Color(0xFF8C42)
    )
    private val images = mutableMapOf<String, BufferedImage?>()

    private val logger = Logger.getInstance(ChocoboPainter::class.java)

    @Synchronized
    private fun image(path: String): BufferedImage? =
        images.getOrPut(path) {
            val resourcePath = "/chocobos/$path"
            val resource = javaClass.getResource(resourcePath)

            if (resource == null) {
                logger.warn("Sprite Chocobo introuvable : $resourcePath")
                null
            } else {
                try {
                    ImageIO.read(resource).also {
                        if (it == null) {
                            logger.warn("Format d'image non reconnu : $resourcePath")
                        }
                    }
                } catch (e: Exception) {
                    logger.warn("Impossible de charger le sprite : $resourcePath", e)
                    null
                }
            }
        }

    fun paint(g: Graphics2D, bar: JProgressBar, color: String) {
        val w = bar.width; val h = bar.height
        if (w <= 0 || h <= 0) return
        val amount = if (bar.isIndeterminate) {
            // Ping-pong motion, independent of the progress value.
            val t = (System.currentTimeMillis() / 22L) % 200L
            (if (t < 100L) t else 200L - t) / 100.0
        } else {
            val range = (bar.maximum - bar.minimum).coerceAtLeast(1)
            ((bar.value - bar.minimum).toDouble() / range).coerceIn(0.0, 1.0)
        }
        val fill = (w * amount).toInt().coerceIn(0, w)
        g.color = Color(0, 0, 0, 30)
        g.fillRoundRect(0, h / 3, w, (h / 3).coerceAtLeast(4), 7, 7)
        g.color = rgb[color] ?: Color.YELLOW
        g.fillRoundRect(0, h / 3, fill, (h / 3).coerceAtLeast(4), 7, 7)

        val direction = if (bar.isIndeterminate && (System.currentTimeMillis() / 22L) % 200L >= 100L) "left" else "right"
        val frame = ((System.currentTimeMillis() / 110L) % 5L + 1L).toInt()
        val sprite = image("chocobo_${color}_${direction}_${frame.toString().padStart(2, '0')}.png")
            ?: image("chocobo_${color}_${direction}_01.png")
        val spriteSize = minOf(32, h.coerceAtLeast(1))
        val x = (fill - spriteSize / 2).coerceIn(0, (w - spriteSize).coerceAtLeast(0))
        val y = (h - spriteSize) / 2
        if (sprite != null) {
            g.drawImage(sprite, x, y, spriteSize, spriteSize, null)
        } else {
            // Visible fallback until PNG assets are supplied.
            g.color = rgb[color] ?: Color.YELLOW
            g.fillOval(x + 4, y + 4, spriteSize - 8, spriteSize - 8)
            g.color = Color.BLACK
            g.drawOval(x + 4, y + 4, spriteSize - 8, spriteSize - 8)
        }
        if (bar.isStringPainted) {
            g.color = bar.foreground
            val text = bar.string ?: ""
            val fm = g.fontMetrics
            g.drawString(text, (w - fm.stringWidth(text)) / 2, (h + fm.ascent - fm.descent) / 2)
        }
    }
}
