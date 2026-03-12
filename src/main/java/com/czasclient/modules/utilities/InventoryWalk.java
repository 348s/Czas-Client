package com.czasclient.modules.utilities;

import com.czasclient.core.Module;
import com.czasclient.CzasClient;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import org.lwjgl.glfw.GLFW;

public class InventoryWalk extends Module {
    private boolean allowSprint = true;
    private boolean allowJump = true;

    public InventoryWalk() {
        super("Inventory Walk", "Allows walking while inventory is open", GLFW.GLFW_KEY_I, Module.Category.UTILITIES);
    }

    @Override
    public void onEnable() {
        CzasClient.LOGGER.info("Inventory Walk enabled");
    }

    @Override
    public void onDisable() {
        CzasClient.LOGGER.info("Inventory Walk disabled");
    }

    @Override
    public void onTick() {
        if (mc.player == null || mc.currentScreen == null) return;

        if (mc.currentScreen instanceof InventoryScreen) {
            if (mc.gameSettings.keyBindForward.isKeyDown()) {
                mc.player.setSprinting(allowSprint && mc.player.getFoodStats().getFoodLevel() > 6);
            }

            if (allowJump && mc.gameSettings.keyBindJump.isKeyDown() && mc.player.onGround) {
                mc.player.jump();
            }
        }
    }

    public void setAllowSprint(boolean allowSprint) {
        this.allowSprint = allowSprint;
    }

    public void setAllowJump(boolean allowJump) {
        this.allowJump = allowJump;
    }

    public boolean isAllowSprint() {
        return allowSprint;
    }

    public boolean isAllowJump() {
        return allowJump;
    }
}
