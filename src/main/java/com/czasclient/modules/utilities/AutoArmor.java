package com.czasclient.modules.utilities;

import com.czasclient.core.Module;
import com.czasclient.CzasClient;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.inventory.container.ClickType;
import org.lwjgl.glfw.GLFW;

public class AutoArmor extends Module {
    private boolean preferDurability = true;
    private int delay = 0;
    private int maxDelay = 5;

    public AutoArmor() {
        super("Auto Armor", "Automatically equips best armor", GLFW.GLFW_KEY_A, Module.Category.UTILITIES);
    }

    @Override
    public void onEnable() {
        CzasClient.LOGGER.info("Auto Armor enabled");
    }

    @Override
    public void onDisable() {
        CzasClient.LOGGER.info("Auto Armor disabled");
    }

    @Override
    public void onTick() {
        if (mc.player == null || mc.world == null) return;

        if (delay > 0) {
            delay--;
            return;
        }

        ClientPlayerEntity player = mc.player;
        PlayerInventory inventory = player.inventory;

        for (EquipmentSlotType slot : EquipmentSlotType.values()) {
            if (slot.getSlotType() != EquipmentSlotType.Group.ARMOR) continue;

            ItemStack currentArmor = player.getItemStackFromSlot(slot);
            ItemStack bestArmor = findBestArmor(inventory, slot);

            if (shouldEquipArmor(currentArmor, bestArmor)) {
                equipArmor(bestArmor, slot);
                delay = maxDelay;
                break;
            }
        }
    }

    private ItemStack findBestArmor(PlayerInventory inventory, EquipmentSlotType slot) {
        ItemStack best = ItemStack.EMPTY;
        int bestValue = -1;

        for (int i = 0; i < 36; i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (!stack.isEmpty() && stack.getItem() instanceof ArmorItem) {
                ArmorItem armor = (ArmorItem) stack.getItem();
                if (armor.getEquipmentSlot() == slot) {
                    int value = getArmorValue(stack);
                    if (value > bestValue) {
                        bestValue = value;
                        best = stack;
                    }
                }
            }
        }

        return best;
    }

    private int getArmorValue(ItemStack stack) {
        if (!(stack.getItem() instanceof ArmorItem)) return 0;
        
        ArmorItem armor = (ArmorItem) stack.getItem();
        int protection = armor.getDamageReduceAmount();
        
        if (preferDurability) {
            int durability = stack.getMaxDamage() - stack.getDamage();
            double durabilityRatio = (double) durability / stack.getMaxDamage();
            protection = (int) (protection * durabilityRatio);
        }
        
        return protection;
    }

    private boolean shouldEquipArmor(ItemStack current, ItemStack candidate) {
        if (candidate.isEmpty()) return false;
        if (current.isEmpty()) return true;
        
        return getArmorValue(candidate) > getArmorValue(current);
    }

    private void equipArmor(ItemStack armor, EquipmentSlotType slot) {
        if (armor.isEmpty()) return;

        PlayerInventory inventory = mc.player.inventory;
        int armorSlot = getInventorySlot(armor, inventory);
        
        if (armorSlot != -1) {
            inventory.currentItem = armorSlot;
            playerController.windowClick(mc.player.container.windowId, 
                armorSlot < 9 ? armorSlot + 36 : armorSlot, 0, 
                ClickType.PICKUP, mc.player);
            
            int armorInventorySlot = 8 - slot.getIndex();
            playerController.windowClick(mc.player.container.windowId, 
                armorInventorySlot, 0, ClickType.PICKUP, mc.player);
            
            if (!mc.player.inventory.getItemStack().isEmpty()) {
                playerController.windowClick(mc.player.container.windowId, 
                    armorSlot < 9 ? armorSlot + 36 : armorSlot, 0, 
                    ClickType.PICKUP, mc.player);
            }
        }
    }

    private int getInventorySlot(ItemStack stack, PlayerInventory inventory) {
        for (int i = 0; i < 36; i++) {
            if (inventory.getStackInSlot(i) == stack) {
                return i;
            }
        }
        return -1;
    }

    public void setPreferDurability(boolean preferDurability) {
        this.preferDurability = preferDurability;
    }

    public void setMaxDelay(int maxDelay) {
        this.maxDelay = Math.max(1, Math.min(20, maxDelay));
    }

    public boolean isPreferDurability() {
        return preferDurability;
    }

    public int getMaxDelay() {
        return maxDelay;
    }
}
