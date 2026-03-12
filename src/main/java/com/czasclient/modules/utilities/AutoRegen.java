package com.czasclient.modules.utilities;

import com.czasclient.core.Module;
import com.czasclient.CzasClient;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.potion.Effects;
import org.lwjgl.glfw.GLFW;

public class AutoRegen extends Module {
    private float health = 18.0f;
    private int delay = 0;
    private int maxDelay = 10;

    public AutoRegen() {
        super("Auto Regen", "Automatically regenerates health", GLFW.GLFW_KEY_H, Module.Category.UTILITIES);
    }

    @Override
    public void onEnable() {
        CzasClient.LOGGER.info("Auto Regen enabled");
    }

    @Override
    public void onDisable() {
        CzasClient.LOGGER.info("Auto Regen disabled");
    }

    @Override
    public void onTick() {
        if (mc.player == null || mc.world == null) return;

        ClientPlayerEntity player = mc.player;

        if (delay > 0) {
            delay--;
            return;
        }

        if (player.getHealth() < health && player.getFoodStats().getFoodLevel() > 18) {
            if (player.isPotionActive(Effects.REGENERATION)) {
                player.setHealth(Math.min(player.getMaxHealth(), player.getHealth() + 1.0f));
                player.getFoodStats().addExhaustion(3.0f);
                delay = maxDelay;
            } else if (player.getFoodStats().getFoodLevel() > 18) {
                player.setHealth(Math.min(player.getMaxHealth(), player.getHealth() + 0.5f));
                player.getFoodStats().addExhaustion(6.0f);
                delay = maxDelay * 2;
            }
        }
    }

    public void setHealth(float health) {
        this.health = Math.max(1.0f, Math.min(20.0f, health));
    }

    public void setMaxDelay(int maxDelay) {
        this.maxDelay = Math.max(1, Math.min(20, maxDelay));
    }

    public float getHealth() {
        return health;
    }

    public int getMaxDelay() {
        return maxDelay;
    }
}
