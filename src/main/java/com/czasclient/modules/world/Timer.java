package com.czasclient.modules.world;

import com.czasclient.core.Module;
import com.czasclient.CzasClient;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import org.lwjgl.glfw.GLFW;

public class Timer extends Module {
    private float timerSpeed = 1.0f;
    private float defaultTimer = 1.0f;

    public Timer() {
        super("Timer", "Modifies game speed", GLFW.GLFW_KEY_T, Module.Category.WORLD);
    }

    @Override
    public void onEnable() {
        CzasClient.LOGGER.info("Timer enabled with speed: " + timerSpeed);
    }

    @Override
    public void onDisable() {
        mc.timer.tickLength = defaultTimer;
        CzasClient.LOGGER.info("Timer disabled");
    }

    @Override
    public void onTick() {
        if (mc.player == null || mc.world == null) return;

        if (timerSpeed != defaultTimer) {
            mc.timer.tickLength = 50.0f / timerSpeed;
        }
    }

    public void setTimerSpeed(float timerSpeed) {
        this.timerSpeed = Math.max(0.1f, Math.min(10.0f, timerSpeed));
    }

    public float getTimerSpeed() {
        return timerSpeed;
    }

    public void setDefaultTimer(float defaultTimer) {
        this.defaultTimer = defaultTimer;
    }

    public float getDefaultTimer() {
        return defaultTimer;
    }
}
