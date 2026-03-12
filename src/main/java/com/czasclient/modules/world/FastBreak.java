package com.czasclient.modules.world;

import com.czasclient.core.Module;
import com.czasclient.CzasClient;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.glfw.GLFW;

public class FastBreak extends Module {
    private float speed = 1.5f;
    private boolean instant = false;

    public FastBreak() {
        super("Fast Break", "Increases block breaking speed", GLFW.GLFW_KEY_B, Module.Category.WORLD);
    }

    @Override
    public void onEnable() {
        CzasClient.LOGGER.info("Fast Break enabled");
    }

    @Override
    public void onDisable() {
        CzasClient.LOGGER.info("Fast Break disabled");
    }

    @Override
    public void onTick() {
        if (mc.player == null || mc.world == null) return;

        ClientPlayerEntity player = mc.player;
        BlockPos breakingPos = playerController.getCurrentBlock();

        if (breakingPos != null && playerController.isHittingBlock) {
            if (instant) {
                playerController.breakBlock(breakingPos);
            } else {
                float currentSpeed = playerController.curBlockDamageMP;
                if (currentSpeed < 1.0f) {
                    playerController.curBlockDamageMP = Math.min(1.0f, currentSpeed * speed);
                }
            }
        }
    }

    public void setSpeed(float speed) {
        this.speed = Math.max(1.0f, Math.min(10.0f, speed));
    }

    public void setInstant(boolean instant) {
        this.instant = instant;
    }

    public float getSpeed() {
        return speed;
    }

    public boolean isInstant() {
        return instant;
    }
}
