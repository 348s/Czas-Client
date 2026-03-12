package com.czasclient.modules.render;

import com.czasclient.core.Module;
import com.czasclient.CzasClient;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.world.LightType;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import org.lwjgl.glfw.GLFW;

public class FullBright extends Module {
    private float brightness = 1.0f;
    private Mode mode = Mode.GAMMA;

    public FullBright() {
        super("Full Bright", "Increases world brightness", GLFW.GLFW_KEY_L, Module.Category.RENDER);
    }

    @Override
    public void onEnable() {
        CzasClient.LOGGER.info("Full Bright enabled");
    }

    @Override
    public void onDisable() {
        if (mode == Mode.GAMMA) {
            mc.gameSettings.gammaSetting = 1.0f;
        }
        CzasClient.LOGGER.info("Full Bright disabled");
    }

    @Override
    public void onTick() {
        if (mc.player == null || mc.world == null) return;

        switch (mode) {
            case GAMMA:
                mc.gameSettings.gammaSetting = brightness;
                break;
                
            case NIGHT_VISION:
                if (!mc.player.isPotionActive(net.minecraft.potion.Effects.NIGHT_VISION)) {
                    mc.player.addPotionEffect(new net.minecraft.potion.EffectInstance(
                        net.minecraft.potion.Effects.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false));
                }
                break;
                
            case WORLD:
                mc.world.getLightFor(LightType.BLOCK, mc.player.getPosition());
                break;
        }
    }

    public void setBrightness(float brightness) {
        this.brightness = Math.max(0.1f, Math.min(50.0f, brightness));
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public float getBrightness() {
        return brightness;
    }

    public Mode getMode() {
        return mode;
    }

    public enum Mode {
        GAMMA, NIGHT_VISION, WORLD
    }
}
