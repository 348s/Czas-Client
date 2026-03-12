package com.czasclient.modules.movement;

import com.czasclient.core.Module;
import com.czasclient.CzasClient;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.util.math.vector.Vector3d;
import org.lwjgl.glfw.GLFW;

public class WhileSpeed extends Module {
    private double speed = 1.5;
    private boolean airStrafe = true;

    public WhileSpeed() {
        super("While Speed", "Increases movement speed while holding sprint", GLFW.GLFW_KEY_V, Module.Category.MOVEMENT);
    }

    @Override
    public void onEnable() {
        CzasClient.LOGGER.info("While Speed enabled");
    }

    @Override
    public void onDisable() {
        CzasClient.LOGGER.info("While Speed disabled");
    }

    @Override
    public void onTick() {
        if (mc.player == null || mc.world == null) return;

        ClientPlayerEntity player = mc.player;
        
        if (player.moveForward != 0 || player.moveStrafing != 0) {
            Vector3d motion = player.getMotion();
            
            double currentSpeed = Math.sqrt(motion.x * motion.x + motion.z * motion.z);
            double targetSpeed = speed;
            
            if (!player.onGround && !airStrafe) {
                targetSpeed = currentSpeed;
            }
            
            if (currentSpeed < targetSpeed) {
                double multiplier = targetSpeed / Math.max(currentSpeed, 0.1);
                player.setMotion(motion.x * multiplier, motion.y, motion.z * multiplier);
            }
        }
    }

    public void setSpeed(double speed) {
        this.speed = Math.max(0.1, Math.min(10.0, speed));
    }

    public void setAirStrafe(boolean airStrafe) {
        this.airStrafe = airStrafe;
    }

    public double getSpeed() {
        return speed;
    }

    public boolean isAirStrafe() {
        return airStrafe;
    }
}
