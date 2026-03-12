package com.czasclient.modules.combat;

import com.czasclient.core.Module;
import com.czasclient.CzasClient;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class Nuker extends Module {
    private double range = 6.0;
    private Mode mode = Mode.ALL;
    private int delay = 0;
    private int maxDelay = 2;

    public Nuker() {
        super("Nuker", "Automatically breaks blocks around the player", GLFW.GLFW_KEY_N, Module.Category.COMBAT);
    }

    @Override
    public void onEnable() {
        CzasClient.LOGGER.info("Nuker enabled");
    }

    @Override
    public void onDisable() {
        CzasClient.LOGGER.info("Nuker disabled");
    }

    @Override
    public void onTick() {
        if (mc.player == null || mc.world == null) return;

        if (delay > 0) {
            delay--;
            return;
        }

        ClientPlayerEntity player = mc.player;
        List<BlockPos> blocksToBreak = getBlocksInRange(player);

        for (BlockPos pos : blocksToBreak) {
            if (shouldBreakBlock(pos)) {
                playerController.clickBlock(pos, player.getHorizontalFacing());
                playerController.resetBlockRemoving();
                delay = maxDelay;
                break;
            }
        }
    }

    private List<BlockPos> getBlocksInRange(ClientPlayerEntity player) {
        List<BlockPos> blocks = new ArrayList<>();
        BlockPos playerPos = player.getPosition();

        int rangeInt = (int) Math.ceil(range);
        for (int x = -rangeInt; x <= rangeInt; x++) {
            for (int y = -rangeInt; y <= rangeInt; y++) {
                for (int z = -rangeInt; z <= rangeInt; z++) {
                    BlockPos pos = playerPos.add(x, y, z);
                    double distance = MathHelper.sqrt(player.getDistanceSq(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5));
                    
                    if (distance <= range) {
                        blocks.add(pos);
                    }
                }
            }
        }

        blocks.sort((a, b) -> {
            double distA = player.getDistanceSq(a.getX() + 0.5, a.getY() + 0.5, a.getZ() + 0.5);
            double distB = player.getDistanceSq(b.getX() + 0.5, b.getY() + 0.5, b.getZ() + 0.5);
            return Double.compare(distA, distB);
        });

        return blocks;
    }

    private boolean shouldBreakBlock(BlockPos pos) {
        Block block = mc.world.getBlockState(pos).getBlock();
        
        if (block == Blocks.AIR) return false;
        if (block == Blocks.BEDROCK) return false;
        if (block == Blocks.BARRIER) return false;

        switch (mode) {
            case ALL:
                return true;
            case ORES:
                return isOre(block);
            case VALUABLE:
                return isValuable(block);
            default:
                return false;
        }
    }

    private boolean isOre(Block block) {
        return block == Blocks.COAL_ORE || block == Blocks.IRON_ORE || block == Blocks.GOLD_ORE ||
               block == Blocks.DIAMOND_ORE || block == Blocks.EMERALD_ORE || block == Blocks.LAPIS_ORE ||
               block == Blocks.REDSTONE_ORE || block == Blocks.NETHER_GOLD_ORE ||
               block == Blocks.NETHER_QUARTZ_ORE || block == Blocks.ANCIENT_DEBRIS;
    }

    private boolean isValuable(Block block) {
        return isOre(block) || block == Blocks.DIAMOND_BLOCK || block == Blocks.EMERALD_BLOCK ||
               block == Blocks.GOLD_BLOCK || block == Blocks.IRON_BLOCK || block == Blocks.CHEST ||
               block == Blocks.ENDER_CHEST || block == Blocks.SPAWNER;
    }

    public void setRange(double range) {
        this.range = Math.max(1.0, Math.min(20.0, range));
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public void setMaxDelay(int maxDelay) {
        this.maxDelay = Math.max(0, Math.min(10, maxDelay));
    }

    public enum Mode {
        ALL, ORES, VALUABLE
    }
}
