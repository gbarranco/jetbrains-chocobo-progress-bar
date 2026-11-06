package dev.chocobo.progress

enum class AnimationSpeed(val frameDelayMs: Int, val displayName: String) {
    VERY_SLOW(220, "Very slow"),
    SLOW(160, "Slow"),
    NORMAL(110, "Normal"),
    FAST(80, "Fast"),
    VERY_FAST(55, "Very fast");

    override fun toString(): String = displayName
}
