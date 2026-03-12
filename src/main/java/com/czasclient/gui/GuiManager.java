package com.czasclient.gui;

import com.czasclient.CzasClient;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.glfw.GLFW;

public class GuiManager {
    private static GuiManager instance;
    private ClickGui clickGui;
    private boolean guiOpen = false;

    public GuiManager() {
        instance = this;
        clickGui = new ClickGui();
    }

    public static GuiManager getInstance() {
        return instance;
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (event.getKey() == GLFW.GLFW_KEY_RIGHT_SHIFT && event.getAction() == GLFW.GLFW_PRESS) {
            toggleGui();
        }
    }

    public void toggleGui() {
        guiOpen = !guiOpen;
        if (guiOpen) {
            Minecraft.getInstance().displayGuiScreen(clickGui);
            CzasClient.LOGGER.info("ClickGUI opened");
        } else {
            if (Minecraft.getInstance().currentScreen == clickGui) {
                Minecraft.getInstance().player.closeScreen();
            }
            CzasClient.LOGGER.info("ClickGUI closed");
        }
    }

    public boolean isGuiOpen() {
        return guiOpen;
    }

    public ClickGui getClickGui() {
        return clickGui;
    }

    public void openGui() {
        if (!guiOpen) {
            toggleGui();
        }
    }

    public void closeGui() {
        if (guiOpen) {
            toggleGui();
        }
    }
}
