package dev.chocobo.progress

import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import java.awt.Window
import java.util.concurrent.atomic.AtomicBoolean
import javax.swing.SwingUtilities
import javax.swing.UIManager
import javax.swing.JProgressBar
import javax.swing.plaf.ComponentUI

class ChocoboStartup : ProjectActivity {
    override suspend fun execute(project: Project) {
        ChocoboInstaller.install()
    }
}

object ChocoboInstaller {
    private val installed = AtomicBoolean(false)

    fun install() {
        if (!installed.compareAndSet(false, true)) return
        SwingUtilities.invokeLater {
            UIManager.getDefaults()["ProgressBarUI"] = ChocoboProgressBarUIFactory::class.java.name
            UIManager.getDefaults()[ChocoboProgressBarUIFactory::class.java.name] = ChocoboProgressBarUIFactory::class.java
            refreshProgressBars()
        }
    }

    private fun installOnTree(component: java.awt.Component) {
        if (component is JProgressBar && component.ui !is ChocoboProgressBarUI) {
            component.setUI(ChocoboProgressBarUI())
        }
        if (component is java.awt.Container) component.components.forEach(::installOnTree)
    }

    private fun refreshTree(component: java.awt.Component) {
        if (component is JProgressBar) component.setUI(ChocoboProgressBarUI())
        if (component is java.awt.Container) component.components.forEach(::refreshTree)
    }

    fun refreshProgressBars() {
        SwingUtilities.invokeLater {
            Window.getWindows().forEach { window ->
                if (window.isDisplayable) {
                    // Reinstall UI to select a fresh random color when settings change.
                    refreshTree(window)
                }
            }
        }
    }
}
