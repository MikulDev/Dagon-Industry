package net.dagonmomo.dagonindustry.common.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class ChunkOilStorage implements Capability.IStorage<IChunkOilCap>
{
    @Nullable
    @Override
    public INBT writeNBT(Capability<IChunkOilCap> capability, IChunkOilCap instance, Direction side)
    {
        CompoundNBT nbt = new CompoundNBT();

        nbt.putDouble("oil", instance.getOil());
        nbt.putDouble("maxOil", instance.getMaxOil());
        nbt.putLong("lastAccess", instance.getTimeLastAccessed());
        nbt.putBoolean("isNew", instance.isNew());

        return nbt;
    }

    @Override
    public void readNBT(Capability<IChunkOilCap> capability, IChunkOilCap instance, Direction side, INBT nbt)
    {
        if (nbt instanceof CompoundNBT)
        {
            CompoundNBT compoundNBT = (CompoundNBT) nbt;

            instance.setOil(compoundNBT.getDouble("oil"));
            instance.setMaxOil(compoundNBT.getDouble("maxOil"));
            instance.setTimeLastAccessed(compoundNBT.getLong("lastAccess"));
            instance.setNew(compoundNBT.getBoolean("isNew"));
        }
    }
}
