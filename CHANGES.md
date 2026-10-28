# Modifications

- Correction de l'enregistrement du delegate Swing : ajout de `createUI`, et association explicite de la classe au nom de l'UI dans `UIDefaults` sans remplacer le classloader global.
- Installation directe du delegate sur les barres déjà présentes dans les fenêtres visibles. Les barres ajoutées ultérieurement dépendent de leur initialisation Swing/Look & Feel.
- Formulaire de configuration compact avec `GridBagLayout`, aligné en haut, et activation des champs selon le mode.
- Description HTML enrichie dans `plugin.xml`.
- Les 60 sprites PNG (6 couleurs × 2 directions × 5 images) sont conservés.

## Compatibilité et limites

Le projet utilise déjà Kotlin 2.4.0, Java 24 et Gradle 9.6.0. Ces versions n'ont pas été modifiées sans validation de la compatibilité du runtime IntelliJ 2026.2.1. La JVM du plugin doit rester compatible avec celle de l'IDE cible : choisir automatiquement le JDK le plus récent serait risqué.

Le build n'a pas pu être exécuté dans cet environnement (seul JDK 21 est disponible). Tester `./gradlew clean buildPlugin` avec un JDK 24 installé, puis `./gradlew runIde` et les barres déterminées/indéterminées. Certaines barres IntelliJ ne sont pas des `JProgressBar` Swing et ne seront pas affectées.
