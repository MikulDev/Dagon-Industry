package net.dagonmomo.dagonindustry.core.init;

import net.dagonmomo.dagonindustry.client.screen.*;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ScreenInit
{
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event)
    {
        ScreenManager.registerFactory(ContainerInit.STEAMHAMMER_CONTAINER.get(), SteamhammerScreen::new);
        ScreenManager.registerFactory(ContainerInit.CARBON_GENERATOR_CONTAINER.get(), CarbonGeneratorScreen::new);
        ScreenManager.registerFactory(ContainerInit.FERMENTER_CONTAINER.get(), FermenterScreen::new);
        ScreenManager.registerFactory(ContainerInit.ALLOY_FURNACE_CONTAINER.get(), AlloyFurnaceScreen::new);
        ScreenManager.registerFactory(ContainerInit.ARC_FURNACE_CONTAINER.get(), ArcFurnaceScreen::new);
        ScreenManager.registerFactory(ContainerInit.BIO_REFINERY_CONTAINER.get(), BioRefineryScreen::new);
        ScreenManager.registerFactory(ContainerInit.CRUSHER_CONTAINER.get(), CrusherScreen::new);
        ScreenManager.registerFactory(ContainerInit.LATHE_CONTAINER.get(), LatheScreen::new);
        ScreenManager.registerFactory(ContainerInit.PUMPJACK_CONTAINER.get(), PumpjackScreen::new);
        ScreenManager.registerFactory(ContainerInit.DIESEL_GENERATOR_CONTAINER.get(), DieselGeneratorScreen::new);
        ScreenManager.registerFactory(ContainerInit.REFINERY_CONTAINER.get(), RefineryScreen::new);
    }
}
