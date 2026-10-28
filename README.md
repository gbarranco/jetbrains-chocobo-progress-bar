# Chocobo Progress — IntelliJ IDEA 2026.2.1

Experimental Kotlin plugin that customizes **Swing `JProgressBar`** widgets. It does not replace every JetBrains custom-painted progress indicator.

## Java compatibility

- **JDK 26 toolchain** for Gradle/Kotlin compilation.
- **Java 25 bytecode** for the runtime embedded in IntelliJ IDEA 2026.2.x. A plugin compiled to Java 26 bytecode cannot run on a Java 25 IDE runtime.
- Gradle **9.x** is required. Install a recent Gradle 9.x, or generate a wrapper using `gradle wrapper --gradle-version 9.2.1`.

## Sprites

Copy transparent 32×32 PNGs into `src/main/resources/chocobos/`:

```
chocobo_blue_right_01.png
chocobo_blue_right_02.png
...
chocobo_blue_right_05.png
chocobo_blue_left_01.png
...
chocobo_orange_left_05.png
```

Colors: `blue`, `green`, `yellow`, `purple`, `red`, `orange`.
Directions: `right`, `left`.
Frames: `01`–`05`.

If you only have one pose for each direction, name it `..._01.png`. The plugin will use that pose as a fallback for missing animation frames.

The six Gysahl PNGs may also be stored here as `gysahl_blue.png`, etc., but **are not used by this first version**.

## Build

```bash
gradle wrapper --gradle-version 9.2.1
./gradlew buildPlugin
```

Note: the second line above should be `./gradlew buildPlugin` (without the leading `a `).

Output: `build/distributions/chocobo-progress-0.1.0.zip`.

Install via Settings > Plugins > gear > Install Plugin from Disk. Restart IDEA, open a project, then Settings > Chocobo Progress.

## Known limitations

- Not built or run in a local IDEA 2026.2.1 environment; API and Gradle compatibility still require validation.
- The global Swing `UIManager` override is a **proof of concept**. It may be overwritten when the IDE theme changes, and it affects only Swing `JProgressBar` widgets.
- The UIManager `ClassLoader` override is global and may conflict with other look-and-feel customizations; test in a disposable IDE profile.
- The animation uses Swing timers for each progress bar. Optimize before production use.
- Restart the IDE after disabling the plugin, as this experimental UI override does not yet implement safe dynamic unload.

## Color settings

Choose a fixed Chocobo color, or select `random` to pick a color independently for each progress bar. The chosen color stays fixed during that progress bar animation. The previous numeric seed setting has been removed from the UI.
