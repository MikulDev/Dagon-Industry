package net.dagonmomo.dagonindustry.core.init;

import net.dagonmomo.dagonindustry.DagonIndustry;
import net.dagonmomo.dagonindustry.common.block.*;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

public class BlockInit
{
    public static DeferredRegister<Block> BLOCKS = DeferredRegister.create(Block.class, DagonIndustry.MOD_ID);

    public static RegistryObject<Block> STEAMHAMMER = BLOCKS.register("steamhammer", () ->
            new MachineBlock<>(() -> TileEntityInit.STEAMHAMMER.get(),Block.Properties.create(Material.IRON).hardnessAndResistance(2.0F, 3.0F)
            .sound(SoundType.METAL).harvestTool(ToolType.PICKAXE).harvestLevel(2).notSolid()));

    public static RegistryObject<Block> CARBON_GENERATOR = BLOCKS.register("carbon_generator", () ->
            new MachineBlock<>(() -> TileEntityInit.CARBON_GENERATOR.get(),Block.Properties.create(Material.IRON).hardnessAndResistance(2.0F, 3.0F)
            .sound(SoundType.METAL).harvestTool(ToolType.PICKAXE).harvestLevel(2).notSolid()));

    public static RegistryObject<Block> FERMENTER = BLOCKS.register("fermenter", () ->
            new MachineBlock<>(() -> TileEntityInit.FERMENTER.get(), Block.Properties.create(Material.IRON).hardnessAndResistance(2.0F, 3.0F)
                    .sound(SoundType.METAL).harvestTool(ToolType.PICKAXE).harvestLevel(2).notSolid()));

    public static RegistryObject<Block> ALLOY_FURNACE = BLOCKS.register("alloy_furnace", () ->
            new MachineBlock<>(() -> TileEntityInit.ALLOY_FURNACE.get(), Block.Properties.create(Material.IRON).hardnessAndResistance(2.0F, 3.0F)
                    .sound(SoundType.METAL).harvestTool(ToolType.PICKAXE).harvestLevel(2).notSolid()));

    public static RegistryObject<Block> ARC_FURNACE = BLOCKS.register("arc_furnace", () ->
            new MachineBlock<>(() -> TileEntityInit.ARC_FURNACE.get(), Block.Properties.create(Material.IRON).hardnessAndResistance(2.0F, 3.0F)
                    .sound(SoundType.METAL).harvestTool(ToolType.PICKAXE).harvestLevel(2).notSolid()));

    public static RegistryObject<Block> BIO_REFINERY = BLOCKS.register("bio_refinery", () ->
            new MachineBlock<>(() -> TileEntityInit.BIO_REFINERY.get(),Block.Properties.create(Material.IRON).hardnessAndResistance(2.0F, 3.0F)
                    .sound(SoundType.METAL).harvestTool(ToolType.PICKAXE).harvestLevel(2).notSolid()));

    public static RegistryObject<Block> PUMPJACK = BLOCKS.register("pumpjack", () ->
            new MachineBlock<>(() -> TileEntityInit.PUMPJACK.get(), Block.Properties.create(Material.IRON).hardnessAndResistance(2.0F, 3.0F)
                    .sound(SoundType.METAL).harvestTool(ToolType.PICKAXE).harvestLevel(2).notSolid()));

    public static RegistryObject<Block> CRUSHER = BLOCKS.register("crusher", () ->
            new MachineBlock<>(() -> TileEntityInit.CRUSHER.get(),Block.Properties.create(Material.IRON).hardnessAndResistance(2.0F, 3.0F)
                    .sound(SoundType.METAL).harvestTool(ToolType.PICKAXE).harvestLevel(2).notSolid()));

    public static RegistryObject<Block> LATHE = BLOCKS.register("lathe", () ->
            new MachineBlock<>(() -> TileEntityInit.LATHE.get(), Block.Properties.create(Material.IRON).hardnessAndResistance(2.0F, 3.0F)
                    .sound(SoundType.METAL).harvestTool(ToolType.PICKAXE).harvestLevel(2).notSolid()));

    public static RegistryObject<Block> DIESEL_GENERATOR = BLOCKS.register("diesel_generator", () ->
            new MachineBlock<>(() -> TileEntityInit.DIESEL_GENERATOR.get(), Block.Properties.create(Material.IRON).hardnessAndResistance(2.0F, 3.0F)
                    .sound(SoundType.METAL).harvestTool(ToolType.PICKAXE).harvestLevel(2).notSolid()));

    public static RegistryObject<Block> REFINERY = BLOCKS.register("refinery", () ->
            new MachineBlock<>(() -> TileEntityInit.REFINERY.get(),Block.Properties.create(Material.IRON).hardnessAndResistance(2.0F, 3.0F)
                    .sound(SoundType.METAL).harvestTool(ToolType.PICKAXE).harvestLevel(2).notSolid()));

    public static RegistryObject<Block> WORKBENCH = BLOCKS.register("workbench", () ->
            new MachineBlock<>(() -> TileEntityInit.WORKBENCH.get(), Block.Properties.create(Material.IRON).hardnessAndResistance(2.0F, 3.0F)
                    .sound(SoundType.METAL).harvestTool(ToolType.PICKAXE).harvestLevel(2).notSolid()));

    public static RegistryObject<Block> AMMO_BENCH = BLOCKS.register("ammo_bench", () ->
            new MachineBlock<>(() -> TileEntityInit.AMMO_BENCH.get(), Block.Properties.create(Material.IRON).hardnessAndResistance(2.0F, 3.0F)
                    .sound(SoundType.METAL).harvestTool(ToolType.PICKAXE).harvestLevel(2).notSolid()));

    public static RegistryObject<Block> MULTIBLOCK = BLOCKS.register("multiblock", () ->
            new Multiblock(Block.Properties.create(Material.IRON).hardnessAndResistance(2, 3)
                                          .sound(SoundType.METAL).harvestTool(ToolType.PICKAXE).harvestLevel(2).notSolid()));

    public static RegistryObject<Block> STEEL_BLOCK = BLOCKS.register("steel_block", () ->
            new Block(Block.Properties.create(Material.IRON).hardnessAndResistance(5F, 12.0F)
                                          .sound(SoundType.METAL).harvestTool(ToolType.PICKAXE).harvestLevel(2)));

    public static RegistryObject<Block> ASPHALT = BLOCKS.register("asphalt", () ->
            new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(2F, 10F)
                                          .sound(SoundType.STONE).harvestTool(ToolType.PICKAXE).harvestLevel(1)));

    public static RegistryObject<Block> COBALT_ORE = BLOCKS.register("cobalt_ore", () -> new Block(Block.Properties.from(Blocks.IRON_ORE).harvestLevel(3)));
    public static RegistryObject<Block> SULFUR_ORE = BLOCKS.register("sulfur_ore", () -> new Block(Block.Properties.from(Blocks.COAL_ORE).harvestLevel(1)));

    // Fluids
    public static final RegistryObject<FlowingFluidBlock> OIL_BLOCK = BLOCKS.register("oil",
            () -> new StickyFluidBlock(() -> FluidInit.OIL_FLUID.get(), AbstractBlock.Properties.create(Material.WATER)
                    .doesNotBlockMovement().hardnessAndResistance(100f).noDrops()));

    public static final RegistryObject<FlowingFluidBlock> GASOLINE_BLOCK = BLOCKS.register("gasoline",
            () -> new FlowingFluidBlock(() -> FluidInit.GASOLINE_FLUID.get(), AbstractBlock.Properties.create(Material.WATER)
                    .doesNotBlockMovement().hardnessAndResistance(100f).noDrops()));
}
