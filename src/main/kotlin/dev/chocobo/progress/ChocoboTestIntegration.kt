package dev.chocobo.progress

import javax.swing.JProgressBar

/**
 * Optional IDE-specific test runner integration.
 * The core progress bar UI must not load classes from the Java plugin.
 */
interface ChocoboTestProvider {
    fun colorFor(bar: JProgressBar): String?
    fun isRunning(bar: JProgressBar): Boolean
}

object ChocoboTestIntegration {
    @Volatile
    var provider: ChocoboTestProvider? = null

    fun colorFor(bar: JProgressBar): String? = provider?.colorFor(bar)
    fun isRunning(bar: JProgressBar): Boolean = provider?.isRunning(bar) == true
}
