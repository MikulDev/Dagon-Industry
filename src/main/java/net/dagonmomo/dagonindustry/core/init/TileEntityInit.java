package net.dagonmomo.dagonindustry.core.init;

import net.dagonmomo.dagonindustry.DagonIndustry;
import net.dagonmomo.dagonindustry.common.te.*;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class TileEntityInit
{
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, DagonIndustry.MOD_ID);

    public static RegistryObject<TileEntityType<SteamhammerTileEntity>> STEAMHAMMER = TILE_ENTITIES.register("steamhammer", () ->
            TileEntityType.Builder.create(SteamhammerTileEntity::new, BlockInit.STEAMHAMMER.get()).build(null));

    public static RegistryObject<TileEntityType<CarbonGeneratorTileEntity>> CARBON_GENERATOR = TILE_ENTITIES.register("carbon_generator", () ->
            TileEntityType.Builder.create(CarbonGeneratorTileEntity::new, BlockInit.CARBON_GENERATOR.get()).build(null));

    public static RegistryObject<TileEntityType<FermenterTileEntity>> FERMENTER = TILE_ENTITIES.register("fermenter", () ->
            TileEntityType.Builder.create(FermenterTileEntity::new, BlockInit.FERMENTER.get()).build(null));

    // Level 1 Steel
    public static RegistryObject<TileEntityType<AlloyFurnaceTileEntity>> ALLOY_FURNACE = TILE_ENTITIES.register("alloy_furnace", () ->
            TileEntityType.Builder.create(AlloyFurnaceTileEntity::new, BlockInit.ALLOY_FURNACE.get()).build(null));

    // Level 2 Steel
    public static RegistryObject<TileEntityType<ArcFurnaceTileEntity>> ARC_FURNACE = TILE_ENTITIES.register("arc_furnace", () ->
            TileEntityType.Builder.create(ArcFurnaceTileEntity::new, BlockInit.ARC_FURNACE.get()).build(null));

    public static RegistryObject<TileEntityType<BioRefineryTileEntity>> BIO_REFINERY = TILE_ENTITIES.register("bio_refinery", () ->
            TileEntityType.Builder.create(BioRefineryTileEntity::new, BlockInit.BIO_REFINERY.get()).build(null));

    public static RegistryObject<TileEntityType<CrusherTileEntity>> CRUSHER = TILE_ENTITIES.register("crusher", () ->
            TileEntityType.Builder.create(CrusherTileEntity::new, BlockInit.CRUSHER.get()).build(null));

    public static RegistryObject<TileEntityType<LatheTileEntity>> LATHE = TILE_ENTITIES.register("lathe", () ->
            TileEntityType.Builder.create(LatheTileEntity::new, BlockInit.LATHE.get()).build(null));

    public static RegistryObject<TileEntityType<PumpjackTileEntity>> PUMPJACK = TILE_ENTITIES.register("pumpjack", () ->
            TileEntityType.Builder.create(PumpjackTileEntity::new, BlockInit.PUMPJACK.get()).build(null));

    public static RegistryObject<TileEntityType<DieselGeneratorTileEntity>> DIESEL_GENERATOR = TILE_ENTITIES.register("diesel_generator", () ->
            TileEntityType.Builder.create(DieselGeneratorTileEntity::new, BlockInit.DIESEL_GENERATOR.get()).build(null));

    public static RegistryObject<TileEntityType<MultiBlockPieceTileEntity>> MULTIBLOCK_PIECE = TILE_ENTITIES.register("multiblock", () ->
            TileEntityType.Builder.create(MultiBlockPieceTileEntity::new, BlockInit.BIO_REFINERY.get()).build(null));

    public static RegistryObject<TileEntityType<RefineryTileEntity>> REFINERY = TILE_ENTITIES.register("refinery", () ->
            TileEntityType.Builder.create(RefineryTileEntity::new, BlockInit.REFINERY.get()).build(null));

    public static RegistryObject<TileEntityType<WorkbenchTileEntity>> WORKBENCH = TILE_ENTITIES.register("workbench", () ->
            TileEntityType.Builder.create(WorkbenchTileEntity::new, BlockInit.WORKBENCH.get()).build(null));

    public static RegistryObject<TileEntityType<AmmoBenchTileEntity>> AMMO_BENCH = TILE_ENTITIES.register("ammo_bench", () ->
            TileEntityType.Builder.create(AmmoBenchTileEntity::new, BlockInit.AMMO_BENCH.get()).build(null));
}
