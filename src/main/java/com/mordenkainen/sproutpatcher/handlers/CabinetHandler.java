package com.mordenkainen.sproutpatcher.handlers;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import rustic.common.blocks.BlockCabinet;

public final class CabinetHandler {

    private CabinetHandler() {}
    
    @SuppressWarnings("deprecation")
    public static AxisAlignedBB getRenderBoundingBox(final World world, final BlockPos pos) {
        final IBlockState state = world.getBlockState(pos);
        try {
            if (((Boolean)state.getBlock().getActualState(state, world, pos).getValue(BlockCabinet.BOTTOM)).booleanValue()) {
                return new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1.0D, pos.getY() + 2.0D, pos.getZ() + 1.0D);
            }
        } catch (IllegalArgumentException e) {}
        return new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1.0D, pos.getY() + 1.0D, pos.getZ() + 1.0D);
    }
    
}
