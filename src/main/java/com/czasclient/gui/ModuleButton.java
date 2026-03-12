package com.czasclient.gui;

import com.czasclient.core.Module;
import net.minecraft.client.gui.screen.Screen;
import org.lwjgl.glfw.GLFW;

public class ModuleButton extends Button {
    private final Module module;
    private final Window parentWindow;
    private boolean binding = false;

    public ModuleButton(Module module, Window parentWindow) {
        super(module.getName(), 0, 0, 0, 0, null);
        this.module = module;
        this.parentWindow = parentWindow;
    }

    @Override
    public void render(Screen screen, int mouseX, int mouseY) {
        boolean hover = isMouseOver(mouseX, mouseY);
        int bgColor = module.isEnabled() ? 
            (parentWindow.getPrimaryColor() & 0xFFFFFF) | 0x88000000 :
            hover ? (parentWindow.getSecondaryColor() & 0xFFFFFF) | 0x44000000 :
            parentWindow.getSecondaryColor();
        
        screen.fillGradientRect(x, y, x + width, y + height, bgColor, bgColor);
        
        String displayText = binding ? "Binding..." : module.getName();
        if (module.isEnabled()) {
            displayText = "§a" + displayText;
        }
        
        screen.drawString(screen.fontRenderer, displayText, x + 5, y + 5, 
            module.isEnabled() ? 0xFF00FF00 : parentWindow.getTextColor());
        
        if (!binding) {
            String keyText = getKeyText();
            screen.drawString(screen.fontRenderer, keyText, 
                x + width - screen.fontRenderer.getStringWidth(keyText) - 5, 
                y + 5, 0x88888888);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!isMouseOver(mouseX, mouseY)) return false;

        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            module.toggle();
            return true;
        } else if (button == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
            binding = true;
            return true;
        }

        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (binding) {
            if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
                binding = false;
                return true;
            } else {
                module.setKey(keyCode);
                binding = false;
                return true;
            }
        }
        return false;
    }

    @Override
    public void onClose() {
        binding = false;
    }

    private String getKeyText() {
        int key = module.getKey();
        if (key == GLFW.GLFW_KEY_UNKNOWN) return "None";
        
        return switch (key) {
            case GLFW.GLFW_KEY_SPACE -> "SPACE";
            case GLFW.GLFW_KEY_RIGHT_SHIFT -> "RSHIFT";
            case GLFW.GLFW_KEY_LEFT_SHIFT -> "LSHIFT";
            case GLFW.GLFW_KEY_RIGHT_CONTROL -> "RCTRL";
            case GLFW.GLFW_KEY_LEFT_CONTROL -> "LCTRL";
            case GLFW.GLFW_KEY_RIGHT_ALT -> "RALT";
            case GLFW.GLFW_KEY_LEFT_ALT -> "LALT";
            case GLFW.GLFW_KEY_ENTER -> "ENTER";
            case GLFW.GLFW_KEY_BACKSPACE -> "BACKSPACE";
            case GLFW.GLFW_KEY_TAB -> "TAB";
            default -> {
                if (key >= GLFW.GLFW_KEY_A && key <= GLFW.GLFW_KEY_Z) {
                    yield Character.toString((char) ('A' + key - GLFW.GLFW_KEY_A));
                } else if (key >= GLFW.GLFW_KEY_0 && key <= GLFW.GLFW_KEY_9) {
                    yield Character.toString((char) ('0' + key - GLFW.GLFW_KEY_0));
                } else {
                    yield "KEY" + key;
                }
            }
        };
    }

    public Module getModule() {
        return module;
    }

    public boolean isBinding() {
        return binding;
    }
}
