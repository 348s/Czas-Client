package com.czasclient.config;

import com.czasclient.gui.ClickGui;
import com.czasclient.gui.Window;

public class ThemeManager {
    private static Theme currentTheme = Theme.RED_BLACK;

    public enum Theme {
        RED_BLACK("Red/Black", 0xFFCC0000, 0xFF000000, 0xFFFFFFFF, 0xFF333333),
        BLUE("Blue", 0xFF0066CC, 0xFF001133, 0xFFFFFFFF, 0xFF003366),
        GREEN("Green", 0xFF00CC66, 0xFF001133, 0xFFFFFFFF, 0xFF003333);

        private final String name;
        private final int primaryColor;
        private final int secondaryColor;
        private final int textColor;
        private final int borderColor;

        Theme(String name, int primaryColor, int secondaryColor, int textColor, int borderColor) {
            this.name = name;
            this.primaryColor = primaryColor;
            this.secondaryColor = secondaryColor;
            this.textColor = textColor;
            this.borderColor = borderColor;
        }

        public String getName() { return name; }
        public int getPrimaryColor() { return primaryColor; }
        public int getSecondaryColor() { return secondaryColor; }
        public int getTextColor() { return textColor; }
        public int getBorderColor() { return borderColor; }
    }

    public static void setTheme(Theme theme) {
        currentTheme = theme;
        applyThemeToGui();
    }

    public static Theme getCurrentTheme() {
        return currentTheme;
    }

    private static void applyThemeToGui() {
        ClickGui clickGui = com.czasclient.gui.GuiManager.getInstance().getClickGui();
        if (clickGui != null) {
            for (Window window : clickGui.windows) {
                window.setColors(
                    currentTheme.getPrimaryColor(),
                    currentTheme.getSecondaryColor(),
                    currentTheme.getTextColor(),
                    currentTheme.getBorderColor()
                );
            }
        }
    }

    public static Theme[] getAvailableThemes() {
        return Theme.values();
    }

    public static String getThemeName(Theme theme) {
        return theme.getName();
    }
}
