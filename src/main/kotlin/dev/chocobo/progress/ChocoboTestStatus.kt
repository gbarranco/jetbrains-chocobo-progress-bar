package dev.chocobo.progress

import com.intellij.execution.testframework.sm.runner.SMTRunnerEventsAdapter
import com.intellij.execution.testframework.sm.runner.SMTRunnerEventsListener
import com.intellij.execution.testframework.sm.runner.SMTestProxy
import com.intellij.openapi.project.Project
import java.awt.Component
import java.awt.Container
import java.awt.Window
import javax.swing.JProgressBar
import javax.swing.SwingUtilities

/**
 * Tracks test runner events rather than inferring test results from progress percentages.
 * Only progress bars inside the SM test runner results UI are affected.
 */
object ChocoboTestStatus {
    private val runningRoots = mutableSetOf<SMTestProxy.SMRootTestProxy>()
    private val failedRoots = mutableSetOf<SMTestProxy.SMRootTestProxy>()
    private var lastResult: String? = null

    @Synchronized
    fun colorFor(bar: JProgressBar): String? {
        if (!isTestRunnerBar(bar)) return null
        return if (runningRoots.isNotEmpty()) null else lastResult
    }

    @Synchronized
    fun isRunning(bar: JProgressBar): Boolean = isTestRunnerBar(bar) && runningRoots.isNotEmpty()

    private fun isTestRunnerBar(bar: JProgressBar): Boolean {
        var current: Component? = bar.parent
        while (current != null) {
            val name = current.javaClass.name
            if (name.contains("SMTestRunnerResultsForm") || name.contains("SMTRunnerResultsForm")) return true
            current = current.parent
        }
        return false
    }

    fun attach(project: Project) {
        project.messageBus.connect(project).subscribe(SMTRunnerEventsListener.TEST_STATUS, object : SMTRunnerEventsAdapter() {
            override fun onTestingStarted(testsRoot: SMTestProxy.SMRootTestProxy) {
                synchronized(this@ChocoboTestStatus) {
                    runningRoots.add(testsRoot)
                    failedRoots.remove(testsRoot)
                    lastResult = null
                }
                refresh()
            }

            override fun onTestFailed(test: SMTestProxy) {
                synchronized(this@ChocoboTestStatus) {
                    // A test failure belongs to its root test suite.
                    var root: SMTestProxy = test
                    while (root.parent != null) root = root.parent
                    if (root is SMTestProxy.SMRootTestProxy) failedRoots.add(root)
                }
            }

            override fun onTestingFinished(testsRoot: SMTestProxy.SMRootTestProxy) {
                synchronized(this@ChocoboTestStatus) {
                    runningRoots.remove(testsRoot)
                    // isDefect on a suite also includes ignored tests, so it cannot
                    // be used to decide whether a run failed.
                    val failed = failedRoots.remove(testsRoot) || hasFailedTests(testsRoot)
                    lastResult = when {
                        testsRoot.wasTerminated() -> null
                        failed -> "red"
                        hasExecutedTests(testsRoot) -> "green"
                        else -> null
                    }
                }
                refresh()
            }
        })
    }

    private fun hasFailedTests(node: SMTestProxy): Boolean {
        if (node.isSuite) return node.children.any(::hasFailedTests)
        return node.isDefect && !node.isIgnored && !node.wasTerminated()
    }

    private fun hasExecutedTests(node: SMTestProxy): Boolean {
        if (node.isSuite) return node.children.any(::hasExecutedTests)
        return node.isPassed && !node.isIgnored
    }

    private fun refresh() {
        SwingUtilities.invokeLater {
            Window.getWindows().filter { it.isDisplayable }.forEach { repaintTestBars(it) }
        }
    }

    private fun repaintTestBars(component: Component) {
        if (component is JProgressBar && isTestRunnerBar(component)) component.repaint()
        if (component is Container) component.components.forEach(::repaintTestBars)
    }
}
