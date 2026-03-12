package com.czasclient.gui;

import net.minecraft.client.gui.screen.Screen;

public abstract class Button {
    public String text;
    public int x, y, width, height;
    public IPressable onPress;

    public Button(String text, int x, int y, int width, int height, IPressable onPress) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.onPress = onPress;
    }

    public abstract void render(Screen screen, int mouseX, int mouseY);

    public boolean isMouseOver(double mouseX, double mouseY) {
        return mouseX >= x && mouseX <= x + width && 
               mouseY >= y && mouseY <= y + height;
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isMouseOver(mouseX, mouseY) && onPress != null) {
            onPress.onPress(this);
            return true;
        }
        return false;
    }

    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return false;
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return false;
    }

    public void onClose() {}

    @FunctionalInterface
    public interface IPressable {
        void onPress(Button button);
    }
}
