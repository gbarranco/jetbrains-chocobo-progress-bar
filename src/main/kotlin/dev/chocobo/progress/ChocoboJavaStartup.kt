package dev.chocobo.progress

import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity

/** Loaded only when the optional Java plugin dependency is available. */
class ChocoboJavaStartup : ProjectActivity {
    override suspend fun execute(project: Project) {
        ChocoboTestIntegration.provider = ChocoboTestStatus
        ChocoboTestStatus.attach(project)
    }
}
