package com.czasclient.modules.movement;

import com.czasclient.core.Module;
import com.czasclient.CzasClient;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.glfw.GLFW;

public class Spider extends Module {
    private double climbSpeed = 0.3;
    private boolean onWall = false;

    public Spider() {
        super("Spider", "Allows climbing walls like a spider", GLFW.GLFW_KEY_Z, Module.Category.MOVEMENT);
    }

    @Override
    public void onEnable() {
        CzasClient.LOGGER.info("Spider enabled");
    }

    @Override
    public void onDisable() {
        CzasClient.LOGGER.info("Spider disabled");
    }

    @Override
    public void onTick() {
        if (mc.player == null || mc.world == null) return;

        ClientPlayerEntity player = mc.player;

        if (isAgainstWall(player) && !player.onGround) {
            onWall = true;
            
            if (mc.gameSettings.keyBindJump.isKeyDown()) {
                player.setMotion(player.getMotion().x, climbSpeed, player.getMotion().z);
                player.fallDistance = 0;
            } else if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                player.setMotion(player.getMotion().x, -climbSpeed, player.getMotion().z);
            } else {
                player.setMotion(player.getMotion().x, 0, player.getMotion().z);
            }
        } else {
            onWall = false;
        }
    }

    private boolean isAgainstWall(ClientPlayerEntity player) {
        double expand = 0.1;
        
        for (double x = -expand; x <= expand; x += 0.1) {
            for (double y = 0; y <= player.getHeight(); y += 0.1) {
                for (double z = -expand; z <= expand; z += 0.1) {
                    if (!mc.world.getBlockState(
                        new BlockPos(player.getPosX() + x, player.getPosY() + y, player.getPosZ() + z)
                    ).isAir()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void setClimbSpeed(double climbSpeed) {
        this.climbSpeed = Math.max(0.1, Math.min(1.0, climbSpeed));
    }

    public double getClimbSpeed() {
        return climbSpeed;
    }

    public boolean isOnWall() {
        return onWall;
    }
}
