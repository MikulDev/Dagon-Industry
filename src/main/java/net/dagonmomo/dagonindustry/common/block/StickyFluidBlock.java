package net.dagonmomo.dagonindustry.common.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.function.Supplier;

public class StickyFluidBlock extends FlowingFluidBlock
{
    public StickyFluidBlock(Supplier<? extends FlowingFluid> supplier, Properties properties)
    {
        super(supplier, properties);
    }

    @Override
    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn)
    {
        entityIn.setMotionMultiplier(state, new Vector3d(1.5D, 1.5D, 1.5D));
    }
}
