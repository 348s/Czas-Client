package com.czasclient.modules.combat;

import com.czasclient.core.Module;
import com.czasclient.CzasClient;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.util.Hand;
import org.lwjgl.glfw.GLFW;

public class AutoClicker extends Module {
    private int cps = 12;
    private boolean jitter = false;
    private boolean breakBlocks = true;
    private long lastClick = 0;
    private boolean clicking = false;

    public AutoClicker() {
        super("Auto Clicker", "Automatically clicks at specified CPS", GLFW.GLFW_KEY_U, Module.Category.COMBAT);
    }

    @Override
    public void onEnable() {
        CzasClient.LOGGER.info("Auto Clicker enabled with CPS: " + cps);
    }

    @Override
    public void onDisable() {
        if (clicking) {
            mc.gameSettings.keyBindAttack.setPressed(false);
            clicking = false;
        }
        CzasClient.LOGGER.info("Auto Clicker disabled");
    }

    @Override
    public void onTick() {
        if (mc.player == null || mc.world == null) return;

        long currentTime = System.currentTimeMillis();
        long delay = 1000 / cps;
        
        if (jitter) {
            delay += (Math.random() - 0.5) * 100;
        }

        if (mc.gameSettings.keyBindAttack.isKeyDown()) {
            if (currentTime - lastClick >= delay) {
                performClick();
                lastClick = currentTime;
            }
        } else if (clicking) {
            mc.gameSettings.keyBindAttack.setPressed(false);
            clicking = false;
        }

        if (jitter && clicking && Math.random() < 0.1) {
            mc.player.rotationYaw += (Math.random() - 0.5) * 2;
            mc.player.rotationPitch += (Math.random() - 0.5) * 2;
        }
    }

    private void performClick() {
        if (breakBlocks && playerController.isHittingBlock) {
            playerController.clickBlock(playerController.getCurrentBlock(), mc.player.getHorizontalFacing());
        } else {
            mc.gameSettings.keyBindAttack.setPressed(true);
            clicking = true;
            
            if (mc.objectMouseOver != null && mc.objectMouseOver.entityHit != null) {
                mc.playerController.attackEntity(mc.player, mc.objectMouseOver.entityHit);
                mc.player.swingArm(net.minecraft.util.Hand.MAIN_HAND);
            }
        }
    }

    public void setCps(int cps) {
        this.cps = Math.max(1, Math.min(20, cps));
    }

    public void setJitter(boolean jitter) {
        this.jitter = jitter;
    }

    public void setBreakBlocks(boolean breakBlocks) {
        this.breakBlocks = breakBlocks;
    }

    public int getCps() {
        return cps;
    }

    public boolean isJitter() {
        return jitter;
    }

    public boolean isBreakBlocks() {
        return breakBlocks;
    }
}
