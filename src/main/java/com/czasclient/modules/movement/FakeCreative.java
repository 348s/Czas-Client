package com.czasclient.modules.movement;

import com.czasclient.core.Module;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.lwjgl.glfw.GLFW;

public class FakeCreative extends Module {
    private boolean allowFlight = true;
    private boolean instantBreak = true;

    public FakeCreative() {
        super("Fake Creative", "Grants creative-like abilities in survival", GLFW.GLFW_KEY_C, Module.Category.MOVEMENT);
    }

    @Override
    public void onEnable() {
        if (mc.player != null) {
            mc.player.abilities.allowFlying = true;
            mc.player.abilities.setFlySpeed(0.05f);
            CzasClient.LOGGER.info("Fake Creative enabled");
        }
    }

    @Override
    public void onDisable() {
        if (mc.player != null && !mc.player.isCreative()) {
            mc.player.abilities.allowFlying = false;
            mc.player.abilities.isFlying = false;
            CzasClient.LOGGER.info("Fake Creative disabled");
        }
    }

    @Override
    public void onTick() {
        if (mc.player == null || mc.world == null) return;

        ClientPlayerEntity player = mc.player;
        
        if (allowFlight && !player.isCreative()) {
            player.abilities.allowFlying = true;
            
            if (player.abilities.isFlying) {
                player.fallDistance = 0;
                
                if (mc.gameSettings.keyBindJump.isKeyDown()) {
                    player.setMotion(player.getMotion().add(0, 0.5, 0));
                }
                if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                    player.setMotion(player.getMotion().add(0, -0.5, 0));
                }
            }
        }

        if (instantBreak && !player.isCreative()) {
            ItemStack stack = player.getHeldItemMainhand();
            if (stack.getItem() == Items.AIR) {
                playerController.setDestroySpeed(player.getBlockPosBelowThatAffectsMyMovement(), 100.0f);
            }
        }
    }

    public void setAllowFlight(boolean allowFlight) {
        this.allowFlight = allowFlight;
    }

    public void setInstantBreak(boolean instantBreak) {
        this.instantBreak = instantBreak;
    }

    public boolean isAllowFlight() {
        return allowFlight;
    }

    public boolean isInstantBreak() {
        return instantBreak;
    }
}
