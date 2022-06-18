package net.dagonmomo.dagonindustry;

import net.dagonmomo.dagonindustry.common.capability.ChunkOilCapability;
import net.dagonmomo.dagonindustry.common.capability.ChunkOilStorage;
import net.dagonmomo.dagonindustry.common.capability.IChunkOilCap;
import net.dagonmomo.dagonindustry.core.init.*;
import net.dagonmomo.dagonindustry.core.network.PacketHandler;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("dagon_industry")
public class DagonIndustry
{
    private static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "dagon_industry";

    public DagonIndustry()
    {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        MinecraftForge.EVENT_BUS.register(this);

        bus.addListener(this::clientEvents);
        bus.addListener(this::commonEvents);

        // Register packets/handler
        PacketHandler.init();

        // Register everything here
        BlockInit.BLOCKS.register(bus);
        TileEntityInit.TILE_ENTITIES.register(bus);
        ContainerInit.CONTAINERS.register(bus);
        ItemInit.ITEMS.register(bus);
        FluidInit.FLUIDS.register(bus);
    }

    private void clientEvents(FMLClientSetupEvent event)
    {
        // Sets the transparency of fluids from this mod
        event.enqueueWork(() ->
        {
            RenderTypeLookup.setRenderLayer(FluidInit.OIL_FLUID.get(),   RenderType.getCutout());
            RenderTypeLookup.setRenderLayer(FluidInit.OIL_FLOWING.get(), RenderType.getCutout());
            RenderTypeLookup.setRenderLayer(BlockInit.OIL_BLOCK.get(),   RenderType.getCutout());
            RenderTypeLookup.setRenderLayer(FluidInit.GASOLINE_FLUID.get(), RenderType.getTranslucent());
            RenderTypeLookup.setRenderLayer(FluidInit.DIESEL_FLOWING.get(), RenderType.getTranslucent());
            RenderTypeLookup.setRenderLayer(BlockInit.GASOLINE_BLOCK.get(), RenderType.getTranslucent());
        });
    }

    private void commonEvents(FMLCommonSetupEvent event)
    {
        // Register capability for chunk oil
        CapabilityManager.INSTANCE.register(IChunkOilCap.class, new ChunkOilStorage(), ChunkOilCapability::new);
    }
}
