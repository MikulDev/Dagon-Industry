package net.dagonmomo.dagonindustry.common.te;

import net.dagonmomo.dagonindustry.core.util.registries.ModTileEntities;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * This is a "filler block" in a multi-block TileEntity
 */
public class MultiBlockPieceTileEntity extends TileEntity
{
    BlockPos ownerPos;

    public MultiBlockPieceTileEntity(TileEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
    }

    public MultiBlockPieceTileEntity()
    {
        super(ModTileEntities.MULTIBLOCK);
    }

    @Override
    public void remove()
    {
        // If the block is removed, destroy the entire structure
        if (ownerPos != null)
            world.destroyBlock(ownerPos, true, null);
        super.remove();
    }

    public BlockPos getOwnerPos()
    {
        return ownerPos;
    }

    // Pass through the owner's capability
    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
    {
        if (this.world != null && this.world.getTileEntity(ownerPos) != null)
        {
            return this.world.getTileEntity(ownerPos).getCapability(cap, side);
        }
        else return LazyOptional.empty();
    }

    // Pass through the owner's capability
    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap)
    {
        return getCapability(cap, null);
    }
}
