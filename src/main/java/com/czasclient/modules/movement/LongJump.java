package com.czasclient.modules.movement;

import com.czasclient.core.Module;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.util.math.vector.Vector3d;
import org.lwjgl.glfw.GLFW;

public class LongJump extends Module {
    private double jumpHeight = 1.0;
    private double jumpDistance = 2.0;
    private boolean boosted = false;
    private int jumpTimer = 0;

    public LongJump() {
        super("Long Jump", "Increases jump height and distance", GLFW.GLFW_KEY_J, Module.Category.MOVEMENT);
    }

    @Override
    public void onEnable() {
        CzasClient.LOGGER.info("Long Jump enabled");
        boosted = false;
        jumpTimer = 0;
    }

    @Override
    public void onDisable() {
        CzasClient.LOGGER.info("Long Jump disabled");
        boosted = false;
        jumpTimer = 0;
    }

    @Override
    public void onTick() {
        if (mc.player == null || mc.world == null) return;

        ClientPlayerEntity player = mc.player;

        if (player.onGround) {
            boosted = false;
            jumpTimer = 0;
        }

        if (mc.gameSettings.keyBindJump.isKeyDown() && player.onGround && !boosted) {
            Vector3d motion = player.getMotion();
            
            double horizontalSpeed = Math.sqrt(motion.x * motion.x + motion.z * motion.z);
            if (horizontalSpeed < 0.1) {
                horizontalSpeed = 0.3;
            }

            Vector3d lookVec = player.getLookVec();
            double jumpX = lookVec.x * jumpDistance;
            double jumpZ = lookVec.z * jumpDistance;

            player.setMotion(
                motion.x * 0.5 + jumpX,
                jumpHeight,
                motion.z * 0.5 + jumpZ
            );

            boosted = true;
            jumpTimer = 10;
        }

        if (boosted && jumpTimer > 0) {
            jumpTimer--;
            
            if (jumpTimer == 0) {
                boosted = false;
            }
        }
    }

    public void setJumpHeight(double jumpHeight) {
        this.jumpHeight = Math.max(0.5, Math.min(3.0, jumpHeight));
    }

    public void setJumpDistance(double jumpDistance) {
        this.jumpDistance = Math.max(0.5, Math.min(5.0, jumpDistance));
    }

    public double getJumpHeight() {
        return jumpHeight;
    }

    public double getJumpDistance() {
        return jumpDistance;
    }

    public boolean isBoosted() {
        return boosted;
    }
}
