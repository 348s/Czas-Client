package com.czasclient.modules.world;

import com.czasclient.core.Module;
import com.czasclient.CzasClient;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.Hand;
import org.lwjgl.glfw.GLFW;

public class Scaffold extends Module {
    private double range = 5.0;
    private boolean tower = true;
    private boolean safeWalk = true;

    public Scaffold() {
        super("Scaffold", "Automatically places blocks under the player", GLFW.GLFW_KEY_G, Module.Category.WORLD);
    }

    @Override
    public void onEnable() {
        CzasClient.LOGGER.info("Scaffold enabled");
    }

    @Override
    public void onDisable() {
        CzasClient.LOGGER.info("Scaffold disabled");
    }

    @Override
    public void onTick() {
        if (mc.player == null || mc.world == null) return;

        ClientPlayerEntity player = mc.player;

        if (safeWalk && player.onGround) {
            BlockPos underPlayer = new BlockPos(player.getPosX(), player.getPosY() - 1, player.getPosZ());
            if (mc.world.getBlockState(underPlayer).isAir()) {
                player.setMotion(player.getMotion().x, -0.2, player.getMotion().z);
            }
        }

        if (mc.gameSettings.keyBindUseItem.isKeyDown()) {
            BlockPos targetPos = getBlockToPlace(player);
            if (targetPos != null) {
                placeBlock(targetPos);
            }
        }

        if (tower && mc.gameSettings.keyBindJump.isKeyDown() && player.onGround) {
            BlockPos underPlayer = new BlockPos(player.getPosX(), player.getPosY() - 1, player.getPosZ());
            if (mc.world.getBlockState(underPlayer).getBlock() != Blocks.AIR) {
                player.setMotion(player.getMotion().x, 0.42, player.getMotion().z);
            }
        }
    }

    private BlockPos getBlockToPlace(ClientPlayerEntity player) {
        BlockPos playerPos = player.getPosition();
        BlockPos targetPos = playerPos.down();

        if (mc.world.getBlockState(targetPos).isAir()) {
            return targetPos;
        }

        for (double x = -range; x <= range; x++) {
            for (double y = -range; y <= range; y++) {
                for (double z = -range; z <= range; z++) {
                    BlockPos pos = playerPos.add(x, y, z);
                    double distance = Math.sqrt(player.getDistanceSq(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5));
                    
                    if (distance <= range && mc.world.getBlockState(pos).isAir() && 
                        !mc.world.getBlockState(pos.up()).isAir()) {
                        return pos;
                    }
                }
            }
        }

        return null;
    }

    private void placeBlock(BlockPos pos) {
        ItemStack stack = findBlockInHotbar();
        if (stack == null) return;

        int slot = player.inventory.currentItem;
        for (int i = 0; i < 9; i++) {
            ItemStack hotbarStack = player.inventory.getStackInSlot(i);
            if (hotbarStack == stack) {
                player.inventory.currentItem = i;
                break;
            }
        }

        playerController.processRightClickBlock(player, mc.world, pos, 
            player.getHorizontalFacing(), new Vec3d(0.5, 0.5, 0.5), Hand.MAIN_HAND);
        
        player.inventory.currentItem = slot;
    }

    private ItemStack findBlockInHotbar() {
        for (int i = 0; i < 9; i++) {
            ItemStack stack = player.inventory.getStackInSlot(i);
            if (!stack.isEmpty() && stack.getItem() instanceof BlockItem) {
                Block block = ((BlockItem) stack.getItem()).getBlock();
                if (block != Blocks.AIR && block.getDefaultState().isSolid()) {
                    return stack;
                }
            }
        }
        return null;
    }

    public void setRange(double range) {
        this.range = Math.max(1.0, Math.min(10.0, range));
    }

    public void setTower(boolean tower) {
        this.tower = tower;
    }

    public void setSafeWalk(boolean safeWalk) {
        this.safeWalk = safeWalk;
    }

    public double getRange() {
        return range;
    }

    public boolean isTower() {
        return tower;
    }

    public boolean isSafeWalk() {
        return safeWalk;
    }
}
