package com.czasclient.modules.movement;

import com.czasclient.core.Module;
import com.czasclient.CzasClient;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.network.play.client.CPlayerPacket;
import org.lwjgl.glfw.GLFW;

public class NoFall extends Module {
    private Mode mode = Mode.VANILLA;

    public NoFall() {
        super("No Fall", "Prevents fall damage", GLFW.GLFW_KEY_F, Module.Category.MOVEMENT);
    }

    @Override
    public void onEnable() {
        CzasClient.LOGGER.info("No Fall enabled");
    }

    @Override
    public void onDisable() {
        CzasClient.LOGGER.info("No Fall disabled");
    }

    @Override
    public void onTick() {
        if (mc.player == null || mc.world == null) return;

        ClientPlayerEntity player = mc.player;

        if (player.fallDistance > 2.0f) {
            switch (mode) {
                case VANILLA:
                    if (player.isSneaking() || player.getMotion().y < -0.5) {
                        player.fallDistance = 0;
                    }
                    break;
                    
                case PACKET:
                    if (player.fallDistance > 3.0f) {
                        player.connection.sendPacket(new CPlayerPacket(true));
                        player.fallDistance = 0;
                    }
                    break;
                    
                case NOGROUND:
                    player.fallDistance = 0;
                    break;
            }
        }
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public Mode getMode() {
        return mode;
    }

    public enum Mode {
        VANILLA, PACKET, NOGROUND
    }
}
