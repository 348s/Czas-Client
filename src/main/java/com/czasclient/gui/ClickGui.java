package com.czasclient.gui;

import com.czasclient.core.Module;
import com.czasclient.core.ModuleManager;
import com.czasclient.CzasClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.StringTextComponent;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class ClickGui extends Screen {
    private final List<Window> windows = new ArrayList<>();
    private Window draggingWindow = null;
    private int dragOffsetX, dragOffsetY;
    private boolean showWatermark = true;

    public ClickGui() {
        super(new StringTextComponent("Czas Client"));
        initializeWindows();
    }

    private void initializeWindows() {
        windows.clear();
        
        int x = 50;
        int y = 50;
        
        for (Module.Category category : Module.Category.values()) {
            Window window = new Window(category.getDisplayName(), x, y, 100, 200);
            List<Module> modules = ModuleManager.getInstance().getModulesByCategory(category);
            
            for (Module module : modules) {
                window.addButton(new ModuleButton(module, window));
            }
            
            windows.add(window);
            x += 110;
            
            if (x > width - 150) {
                x = 50;
                y += 220;
            }
        }
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        renderBackground(0);
        
        if (showWatermark) {
            drawString(fontRenderer, "§cCzas Client §7by 348 s", 5, 5, 0xFFFFFFFF);
            drawString(fontRenderer, "§7Right Shift to toggle", 5, 15, 0xAAAAAAAA);
        }

        for (Window window : windows) {
            window.render(this, mouseX, mouseY);
        }

        super.render(mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            for (Window window : windows) {
                if (window.isMouseOver(mouseX, mouseY)) {
                    if (window.isHeaderOver(mouseX, mouseY)) {
                        draggingWindow = window;
                        dragOffsetX = (int) (mouseX - window.x);
                        dragOffsetY = (int) (mouseY - window.y);
                    } else {
                        window.mouseClicked(mouseX, mouseY, button);
                    }
                    return true;
                }
            }
        } else if (button == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
            for (Window window : windows) {
                if (window.isMouseOver(mouseX, mouseY)) {
                    window.mouseClicked(mouseX, mouseY, button);
                    return true;
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            draggingWindow = null;
        }
        
        for (Window window : windows) {
            window.mouseReleased(mouseX, mouseY, button);
        }
        
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (draggingWindow != null && button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            draggingWindow.x = (int) (mouseX - dragOffsetX);
            draggingWindow.y = (int) (mouseY - dragOffsetY);
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_RIGHT_SHIFT) {
            mc.player.closeScreen();
            return true;
        }
        
        for (Window window : windows) {
            if (window.keyPressed(keyCode, scanCode, modifiers)) {
                return true;
            }
        }
        
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void onClose() {
        for (Window window : windows) {
            window.onClose();
        }
        super.onClose();
    }

    public void setShowWatermark(boolean showWatermark) {
        this.showWatermark = showWatermark;
    }

    public boolean isShowWatermark() {
        return showWatermark;
    }
}
