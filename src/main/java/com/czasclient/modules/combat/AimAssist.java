package com.czasclient.modules.combat;

import com.czasclient.core.Module;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.glfw.GLFW;

public class AimAssist extends Module {
    private double range = 6.0;
    private float speed = 2.0f;
    private boolean onlyPlayers = false;
    private boolean throughWalls = false;

    public AimAssist() {
        super("Aim Assist", "Helps aim at nearby entities", GLFW.GLFW_KEY_R, Module.Category.COMBAT);
    }

    @Override
    public void onEnable() {
        CzasClient.LOGGER.info("Aim Assist enabled");
    }

    @Override
    public void onDisable() {
        CzasClient.LOGGER.info("Aim Assist disabled");
    }

    @Override
    public void onTick() {
        if (mc.player == null || mc.world == null) return;

        ClientPlayerEntity player = mc.player;
        Entity target = findTarget(player);

        if (target != null) {
            aimAtEntity(player, target);
        }
    }

    private Entity findTarget(ClientPlayerEntity player) {
        Entity closestEntity = null;
        double closestDistance = range;

        for (Entity entity : mc.world.getAllEntities()) {
            if (!isValidTarget(entity, player)) continue;

            double distance = player.getDistance(entity);
            if (distance <= closestDistance) {
                closestDistance = distance;
                closestEntity = entity;
            }
        }

        return closestEntity;
    }

    private boolean isValidTarget(Entity entity, ClientPlayerEntity player) {
        if (entity == player) return false;
        if (!(entity instanceof LivingEntity)) return false;
        if (onlyPlayers && !(entity instanceof PlayerEntity)) return false;
        if (((LivingEntity) entity).getHealth() <= 0) return false;

        if (!throughWalls && !player.canEntityBeSeen(entity)) {
            return false;
        }

        return true;
    }

    private void aimAtEntity(ClientPlayerEntity player, Entity target) {
        double dx = target.getPosX() - player.getPosX();
        double dy = target.getPosY() + target.getEyeHeight() - player.getPosY() - player.getEyeHeight();
        double dz = target.getPosZ() - player.getPosZ();
        double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);

        float yaw = (float) Math.toDegrees(Math.atan2(dz, dx)) - 90.0f;
        float pitch = (float) -Math.toDegrees(Math.atan2(dy, distance));

        yaw = MathHelper.wrapDegrees(yaw);
        pitch = MathHelper.clamp(pitch, -90.0f, 90.0f);

        float currentYaw = player.rotationYaw;
        float currentPitch = player.rotationPitch;

        float deltaYaw = MathHelper.wrapDegrees(yaw - currentYaw);
        float deltaPitch = pitch - currentPitch;

        deltaYaw = MathHelper.clamp(deltaYaw, -speed, speed);
        deltaPitch = MathHelper.clamp(deltaPitch, -speed, speed);

        player.rotationYaw = currentYaw + deltaYaw;
        player.rotationPitch = currentPitch + deltaPitch;
    }

    public void setRange(double range) {
        this.range = Math.max(1.0, Math.min(20.0, range));
    }

    public void setSpeed(float speed) {
        this.speed = Math.max(0.1f, Math.min(10.0f, speed));
    }

    public void setOnlyPlayers(boolean onlyPlayers) {
        this.onlyPlayers = onlyPlayers;
    }

    public void setThroughWalls(boolean throughWalls) {
        this.throughWalls = throughWalls;
    }

    public double getRange() {
        return range;
    }

    public float getSpeed() {
        return speed;
    }

    public boolean isOnlyPlayers() {
        return onlyPlayers;
    }

    public boolean isThroughWalls() {
        return throughWalls;
    }
}
