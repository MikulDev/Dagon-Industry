package net.dagonmomo.dagonindustry.core.init;

import net.dagonmomo.dagonindustry.DagonIndustry;
import net.dagonmomo.dagonindustry.common.container.*;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ContainerInit
{
    public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, DagonIndustry.MOD_ID);

    public static final RegistryObject<ContainerType<SteamhammerContainer>> STEAMHAMMER_CONTAINER =
            CONTAINERS.register("steamhammer", () -> IForgeContainerType.create(SteamhammerContainer::new));

    public static final RegistryObject<ContainerType<CarbonGeneratorContainer>> CARBON_GENERATOR_CONTAINER =
            CONTAINERS.register("carbon_generator", () -> IForgeContainerType.create(CarbonGeneratorContainer::new));

    public static final RegistryObject<ContainerType<FermenterContainer>> FERMENTER_CONTAINER =
            CONTAINERS.register("fermenter", () -> IForgeContainerType.create(FermenterContainer::new));

    public static final RegistryObject<ContainerType<AlloyFurnaceContainer>> ALLOY_FURNACE_CONTAINER =
            CONTAINERS.register("alloy_furnace", () -> IForgeContainerType.create(AlloyFurnaceContainer::new));

    public static final RegistryObject<ContainerType<ArcFurnaceContainer>> ARC_FURNACE_CONTAINER =
            CONTAINERS.register("arc_furnace", () -> IForgeContainerType.create(ArcFurnaceContainer::new));

    public static final RegistryObject<ContainerType<BioRefineryContainer>> BIO_REFINERY_CONTAINER =
            CONTAINERS.register("bio_generator", () -> IForgeContainerType.create(BioRefineryContainer::new));

    public static final RegistryObject<ContainerType<CrusherContainer>> CRUSHER_CONTAINER =
            CONTAINERS.register("crusher", () -> IForgeContainerType.create(CrusherContainer::new));

    public static final RegistryObject<ContainerType<LatheContainer>> LATHE_CONTAINER =
            CONTAINERS.register("lathe", () -> IForgeContainerType.create(LatheContainer::new));

    public static final RegistryObject<ContainerType<PumpjackContainer>> PUMPJACK_CONTAINER =
            CONTAINERS.register("pumpjack", () -> IForgeContainerType.create(PumpjackContainer::new));

    public static final RegistryObject<ContainerType<DieselGeneratorContainer>> DIESEL_GENERATOR_CONTAINER =
            CONTAINERS.register("diesel_generator", () -> IForgeContainerType.create(DieselGeneratorContainer::new));

    public static final RegistryObject<ContainerType<RefineryContainer>> REFINERY_CONTAINER =
            CONTAINERS.register("refinery", () -> IForgeContainerType.create(RefineryContainer::new));
}
