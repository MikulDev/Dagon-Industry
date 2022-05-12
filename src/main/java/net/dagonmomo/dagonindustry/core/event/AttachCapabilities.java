package net.dagonmomo.dagonindustry.core.event;

import net.dagonmomo.dagonindustry.DagonIndustry;
import net.dagonmomo.dagonindustry.common.capability.ChunkOilCapability;
import net.dagonmomo.dagonindustry.common.capability.IChunkOilCap;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class AttachCapabilities
{
    @SubscribeEvent
    public static void attachCapabilities(AttachCapabilitiesEvent<Chunk> event)
    {
        if (event.getObject() == null) return;

        ChunkOilCapability backend = new ChunkOilCapability(event.getObject());
        LazyOptional<IChunkOilCap> optionalStorage = LazyOptional.of(() -> backend);
        Capability<IChunkOilCap> capability = ChunkOilCapability.CHUNK_OIL;

        ICapabilityProvider provider = new ICapabilitySerializable<CompoundNBT>()
        {
            @Override
            public CompoundNBT serializeNBT()
            {
                return (CompoundNBT) capability.getStorage().writeNBT(capability, backend, null);
            }

            @Override
            public void deserializeNBT(CompoundNBT nbt)
            {
                capability.getStorage().readNBT(capability, backend, null, nbt);
            }

            @Nonnull
            @Override
            public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
            {
                if (cap == ChunkOilCapability.CHUNK_OIL) {
                    return optionalStorage.cast();
                }
                return LazyOptional.empty();
            }
        };

        event.addCapability(new ResourceLocation(DagonIndustry.MOD_ID, "chunk_oil"), provider);
        event.addListener(optionalStorage::invalidate);
    }
}
