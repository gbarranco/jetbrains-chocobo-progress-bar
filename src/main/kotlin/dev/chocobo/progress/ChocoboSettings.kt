package dev.chocobo.progress

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

@Service(Service.Level.APP)
@State(name = "ChocoboProgressSettings", storages = [Storage("chocobo-progress.xml")])
class ChocoboSettings : PersistentStateComponent<ChocoboSettings.Data> {
    // Legacy mode/seed are retained only to read existing persisted settings.
    data class Data(var mode: String = "manual", var color: String = "yellow", var seed: Long = 42L)
    private var data = Data()
    override fun getState(): Data = data
    override fun loadState(state: Data) {
        data = Data(mode = if (state.mode == "random") "random" else "manual",
                    color = state.color.takeIf { it in ChocoboPainter.COLORS } ?: "yellow")
    }
    fun snapshot(): Data = data.copy()
    fun update(mode: String, color: String) {
        data = Data(mode = if (mode == "random") "random" else "manual",
                    color = color.takeIf { it in ChocoboPainter.COLORS } ?: "yellow")
    }
    fun selectedColor(): String =
        if (data.mode == "random") ChocoboPainter.COLORS.random() else data.color
}
