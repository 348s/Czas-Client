package com.czasclient.gui;

import net.minecraft.client.gui.screen.Screen;
import java.util.ArrayList;
import java.util.List;

public class Window {
    public String title;
    public int x, y, width, height;
    public boolean extended = true;
    private final List<Button> buttons = new ArrayList<>();
    private int scrollOffset = 0;
    private boolean dragging = false;
    
    private int primaryColor = 0xFFCC0000;
    private int secondaryColor = 0xFF000000;
    private int textColor = 0xFFFFFFFF;
    private int borderColor = 0xFF333333;

    public Window(String title, int x, int y, int width, int height) {
        this.title = title;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void addButton(Button button) {
        buttons.add(button);
    }

    public void render(Screen screen, int mouseX, int mouseY) {
        renderBackground(screen);
        renderHeader(screen, mouseX, mouseY);
        
        if (extended) {
            renderButtons(screen, mouseX, mouseY);
        }
        
        renderBorder(screen);
    }

    private void renderBackground(Screen screen) {
        int bgHeight = extended ? Math.min(height, 20 + buttons.size() * 20) : 20;
        
        screen.fillGradientRect(x, y, x + width, y + bgHeight, 
            secondaryColor, secondaryColor);
    }

    private void renderHeader(Screen screen, int mouseX, int mouseY) {
        boolean headerHover = isHeaderOver(mouseX, mouseY);
        int headerColor = headerHover ? (primaryColor & 0xFFFFFF) | 0xCC000000 : primaryColor;
        
        screen.fillGradientRect(x, y, x + width, y + 20, 
            headerColor, headerColor);
        
        screen.drawString(screen.fontRenderer, title, 
            x + 5, y + 6, textColor);
        
        String extendSymbol = extended ? "-" : "+";
        screen.drawString(screen.fontRenderer, extendSymbol, 
            x + width - 15, y + 6, textColor);
    }

    private void renderButtons(Screen screen, int mouseX, int mouseY) {
        int buttonY = y + 20 + scrollOffset;
        
        for (int i = 0; i < buttons.size(); i++) {
            if (buttonY >= y + height) break;
            if (buttonY + 20 <= y + 20) {
                buttonY += 20;
                continue;
            }
            
            Button button = buttons.get(i);
            button.x = x + 2;
            button.y = buttonY;
            button.width = width - 4;
            button.height = 18;
            
            button.render(screen, mouseX, mouseY);
            buttonY += 20;
        }
    }

    private void renderBorder(Screen screen) {
        screen.fillGradientRect(x, y, x + 1, y + height, borderColor, borderColor);
        screen.fillGradientRect(x + width - 1, y, x + width, y + height, borderColor, borderColor);
        screen.fillGradientRect(x, y, x + width, y + 1, borderColor, borderColor);
        screen.fillGradientRect(x, y + height - 1, x + width, y + height, borderColor, borderColor);
    }

    public boolean isMouseOver(double mouseX, double mouseY) {
        int currentHeight = extended ? Math.min(height, 20 + buttons.size() * 20) : 20;
        return mouseX >= x && mouseX <= x + width && 
               mouseY >= y && mouseY <= y + currentHeight;
    }

    public boolean isHeaderOver(double mouseX, double mouseY) {
        return mouseX >= x && mouseX <= x + width && 
               mouseY >= y && mouseY <= y + 20;
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isHeaderOver(mouseX, mouseY)) {
            if (mouseX >= x + width - 20 && mouseX <= x + width - 5) {
                extended = !extended;
                return true;
            }
        } else if (extended) {
            for (Button btn : buttons) {
                if (btn.isMouseOver(mouseX, mouseY)) {
                    btn.mouseClicked(mouseX, mouseY, button);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        for (Button btn : buttons) {
            btn.mouseReleased(mouseX, mouseY, button);
        }
        return false;
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        for (Button btn : buttons) {
            if (btn.keyPressed(keyCode, scanCode, modifiers)) {
                return true;
            }
        }
        return false;
    }

    public void onClose() {
        for (Button btn : buttons) {
            btn.onClose();
        }
    }

    public void setColors(int primary, int secondary, int text, int border) {
        this.primaryColor = primary;
        this.secondaryColor = secondary;
        this.textColor = text;
        this.borderColor = border;
    }

    public int getPrimaryColor() { return primaryColor; }
    public int getSecondaryColor() { return secondaryColor; }
    public int getTextColor() { return textColor; }
    public int getBorderColor() { return borderColor; }
}
