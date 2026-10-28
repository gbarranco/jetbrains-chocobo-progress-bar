package dev.chocobo.progress;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicProgressBarUI;

/** Swing UIManager factory. Kept in Java to avoid Kotlin's inherited static createUI signature clash. */
public final class ChocoboProgressBarUIFactory extends BasicProgressBarUI {
    public static ComponentUI createUI(JComponent component) {
        return new ChocoboProgressBarUI();
    }
}
