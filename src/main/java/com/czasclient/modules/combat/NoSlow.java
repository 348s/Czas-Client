package com.czasclient.modules.combat;

import com.czasclient.core.Module;
import com.czasclient.CzasClient;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.lwjgl.glfw.GLFW;

public class NoSlow extends Module {
    private boolean items = true;
    private boolean soulSand = true;
    private boolean web = true;
    private boolean liquid = true;

    public NoSlow() {
        super("No Slow", "Prevents movement slowdown effects", GLFW.GLFW_KEY_S, Module.Category.COMBAT);
    }

    @Override
    public void onEnable() {
        CzasClient.LOGGER.info("No Slow enabled");
    }

    @Override
    public void onDisable() {
        CzasClient.LOGGER.info("No Slow disabled");
    }

    @Override
    public void onTick() {
        if (mc.player == null || mc.world == null) return;

        ClientPlayerEntity player = mc.player;

        if (items) {
            ItemStack mainHand = player.getHeldItemMainhand();
            ItemStack offHand = player.getHeldItemOffhand();
            
            boolean hasSlowItem = isSlowItem(mainHand) || isSlowItem(offHand);
            
            if (hasSlowItem) {
                player.setSprinting(player.moveForward > 0 && !player.isHandActive());
            }
        }

        if (soulSand && player.isInWater()) {
            player.setMotion(player.getMotion().x * 1.2, player.getMotion().y, player.getMotion().z * 1.2);
        }

        if (web && player.isInWeb) {
            player.setMotion(player.getMotion().x * 2.0, player.getMotion().y, player.getMotion().z * 2.0);
        }

        if (liquid && (player.isInWater() || player.isInLava())) {
            if (player.moveForward != 0 || player.moveStrafing != 0) {
                player.setMotion(player.getMotion().x * 1.1, player.getMotion().y, player.getMotion().z * 1.1);
            }
        }
    }

    private boolean isSlowItem(ItemStack stack) {
        if (stack.isEmpty()) return false;
        
        return stack.getItem() == Items.BOW || 
               stack.getItem() == Items.CROSSBOW ||
               stack.getItem() == Items.TRIDENT ||
               stack.getItem() == Items.SHIELD ||
               stack.getItem() == Items.FOOD;
    }

    public void setItems(boolean items) {
        this.items = items;
    }

    public void setSoulSand(boolean soulSand) {
        this.soulSand = soulSand;
    }

    public void setWeb(boolean web) {
        this.web = web;
    }

    public void setLiquid(boolean liquid) {
        this.liquid = liquid;
    }

    public boolean isItems() {
        return items;
    }

    public boolean isSoulSand() {
        return soulSand;
    }

    public boolean isWeb() {
        return web;
    }

    public boolean isLiquid() {
        return liquid;
    }
}
