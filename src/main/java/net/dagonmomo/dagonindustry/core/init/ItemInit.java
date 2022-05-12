package net.dagonmomo.dagonindustry.core.init;

import net.dagonmomo.dagonindustry.DagonIndustry;
import net.dagonmomo.dagonindustry.common.item.*;
import net.dagonmomo.dagonindustry.common.te.*;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.*;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemInit
{
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, DagonIndustry.MOD_ID);

    public static final RegistryObject<Item> CRUDE_BATTERY = ITEMS.register("crude_battery", () ->
            new BatteryItem(600, 0, new Item.Properties().group(ItemGroup.MISC).rarity(Rarity.COMMON)));
    public static final RegistryObject<Item> DIESEL_BATTERY = ITEMS.register("diesel_battery", () ->
            new BatteryItem(1200, 1, new Item.Properties().group(ItemGroup.MISC).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> BIOMASS = ITEMS.register("biomass", () -> new Item(new Item.Properties().group(ItemGroup.MISC)));
    public static final RegistryObject<Item> SULFUR_POWDER = ITEMS.register("sulfur_powder", () -> new Item(new Item.Properties().group(ItemGroup.MISC)));
    public static final RegistryObject<Item> STEEL_INGOT = ITEMS.register("steel_ingot", () -> new Item(new Item.Properties().group(ItemGroup.MISC)));
    public static final RegistryObject<Item> COBALT_INGOT = ITEMS.register("cobalt_ingot", () -> new Item(new Item.Properties().group(ItemGroup.MISC)));
    public static final RegistryObject<Item> SLAG = ITEMS.register("slag", () -> new Item(new Item.Properties().group(ItemGroup.MISC)));

    public static final RegistryObject<Item> IRON_PLATE = ITEMS.register("iron_plate", () -> new MetalPlateItem(Items.IRON_INGOT));
    public static final RegistryObject<Item> GOLD_PLATE = ITEMS.register("gold_plate", () -> new MetalPlateItem(Items.GOLD_INGOT));
    public static final RegistryObject<Item> NETHERITE_PLATE = ITEMS.register("netherite_plate", () -> new MetalPlateItem(Items.NETHERITE_INGOT));
    public static final RegistryObject<Item> STEEL_PLATE = ITEMS.register("steel_plate", () -> new MetalPlateItem(STEEL_INGOT.get()));
    public static final RegistryObject<Item> COBALT_PLATE = ITEMS.register("cobalt_plate", () -> new MetalPlateItem(COBALT_INGOT.get()));

    public static final RegistryObject<Item> IRON_ROD = ITEMS.register("iron_rod", () -> new MetalRodItem(Items.IRON_INGOT));
    public static final RegistryObject<Item> NETHERITE_ROD = ITEMS.register("netherite_rod", () -> new MetalRodItem(Items.NETHERITE_INGOT));
    public static final RegistryObject<Item> STEEL_ROD = ITEMS.register("steel_rod", () -> new MetalRodItem(STEEL_INGOT.get()));
    public static final RegistryObject<Item> COBALT_ROD = ITEMS.register("cobalt_rod", () -> new MetalRodItem(COBALT_INGOT.get()));

    public static final RegistryObject<Item> STEEL_SWORD = ITEMS.register("steel_sword", () -> new SwordItem(new SteelItemTier(), 3, -2F, new Item.Properties().group(ItemGroup.COMBAT).maxStackSize(1).maxDamage(500)));
    public static final RegistryObject<Item> STEEL_AXE = ITEMS.register("steel_axe", () -> new AxeItem(new SteelItemTier(), 6F, -2.9F, new Item.Properties().group(ItemGroup.TOOLS).maxStackSize(1).maxDamage(500)));
    public static final RegistryObject<Item> STEEL_SHOVEL = ITEMS.register("steel_shovel", () -> new ShovelItem(new SteelItemTier(), 1.5F, -2F, new Item.Properties().group(ItemGroup.TOOLS).maxStackSize(1).maxDamage(500)));
    public static final RegistryObject<Item> STEEL_PICKAXE = ITEMS.register("steel_pickaxe", () -> new PickaxeItem(new SteelItemTier(), 1, -2.2F, new Item.Properties().group(ItemGroup.TOOLS).maxStackSize(1).maxDamage(500)));
    public static final RegistryObject<Item> STEEL_HOE = ITEMS.register("steel_hoe", () -> new HoeItem(new SteelItemTier(), -2, 0F, new Item.Properties().group(ItemGroup.TOOLS).maxStackSize(1).maxDamage(500)));

    // Gun Items
    public static RegistryObject<Item> PISTOL = ITEMS.register("crude_pistol", () -> new GunItem(new Item.Properties().group(ItemGroup.COMBAT).maxStackSize(1)));
    public static RegistryObject<Item> RIFLE = ITEMS.register("crude_rifle", () -> new GunItem(new Item.Properties().group(ItemGroup.COMBAT).maxStackSize(1)));
    public static RegistryObject<Item> SNIPER = ITEMS.register("crude_sniper", () -> new GunItem(new Item.Properties().group(ItemGroup.COMBAT).maxStackSize(1)));
    public static RegistryObject<Item> SHOTGUN = ITEMS.register("crude_shotgun", () -> new GunItem(new Item.Properties().group(ItemGroup.COMBAT).maxStackSize(1)));

    // Block Items
    public static final RegistryObject<Item> STEAMHAMMER = ITEMS.register("steamhammer", () ->
            new MultiblockItem(BlockInit.STEAMHAMMER.get(), new Item.Properties().group(ItemGroup.DECORATIONS),
                               (world, direction) -> new SteamhammerTileEntity().getStructure(world, direction)));

    public static final RegistryObject<Item> CARBON_GENERATOR = ITEMS.register("carbon_generator", () ->
            new MultiblockItem(BlockInit.CARBON_GENERATOR.get(), new Item.Properties().group(ItemGroup.DECORATIONS),
                               (world, direction) -> new CarbonGeneratorTileEntity().getStructure(world, direction)));

    public static final RegistryObject<Item> FERMENTER = ITEMS.register("fermenter", () ->
            new OffsetBlockItem(BlockInit.FERMENTER.get(), new Item.Properties().group(ItemGroup.DECORATIONS),
                               (world, direction) -> new FermenterTileEntity().getStructure(world, direction), new Vector3i(0, 1, 0)));

    public static final RegistryObject<Item> ALLOY_FURNACE = ITEMS.register("alloy_furnace", () ->
            new MultiblockItem(BlockInit.ALLOY_FURNACE.get(), new Item.Properties().group(ItemGroup.DECORATIONS),
                               (world, direction) -> new AlloyFurnaceTileEntity().getStructure(world, direction)));

    public static final RegistryObject<Item> ARC_FURNACE = ITEMS.register("arc_furnace", () ->
            new OffsetBlockItem(BlockInit.ARC_FURNACE.get(), new Item.Properties().group(ItemGroup.DECORATIONS),
                               (world, direction) -> new ArcFurnaceTileEntity().getStructure(world, direction), new Vector3i(0, 1, 0)));

    public static final RegistryObject<Item> BIO_REFINERY = ITEMS.register("bio_refinery", () ->
            new BlockItem(BlockInit.BIO_REFINERY.get(), new Item.Properties().group(ItemGroup.DECORATIONS)));

    public static final RegistryObject<Item> CRUSHER = ITEMS.register("crusher", () ->
            new MultiblockItem(BlockInit.CRUSHER.get(), new Item.Properties().group(ItemGroup.DECORATIONS),
                                (world, direction) -> new CrusherTileEntity().getStructure(world, direction)));

    public static final RegistryObject<Item> LATHE = ITEMS.register("lathe", () ->
            new MultiblockItem(BlockInit.LATHE.get(), new Item.Properties().group(ItemGroup.DECORATIONS),
                               (world, direction) -> new LatheTileEntity().getStructure(world, direction)));

    public static final RegistryObject<Item> DIESEL_GENERATOR = ITEMS.register("diesel_generator", () ->
            new MultiblockItem(BlockInit.DIESEL_GENERATOR.get(), new Item.Properties().group(ItemGroup.DECORATIONS),
                               (world, direction) -> new DieselGeneratorTileEntity().getStructure(world, direction)));

    public static final RegistryObject<Item> PUMPJACK = ITEMS.register("pumpjack", () ->
            new OffsetBlockItem(BlockInit.PUMPJACK.get(), new Item.Properties().group(ItemGroup.DECORATIONS),
                               (world, direction) -> new PumpjackTileEntity().getStructure(world, direction), new Vector3i(0, 1, 0)));

    public static final RegistryObject<Item> REFINERY = ITEMS.register("refinery", () ->
            new OffsetBlockItem(BlockInit.REFINERY.get(), new Item.Properties().group(ItemGroup.DECORATIONS),
                                (world, direction) -> new RefineryTileEntity().getStructure(world, direction), new Vector3i(0, 1, 0)));

    // Normal blocks
    public static final RegistryObject<Item> STEEL_BLOCK = ITEMS.register("steel_block", () ->
            new BlockItem(BlockInit.STEEL_BLOCK.get(), new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)));
    public static final RegistryObject<Item> COBALT_ORE = ITEMS.register("cobalt_ore", () ->
            new BlockItem(BlockInit.COBALT_ORE.get(), new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)));
    public static final RegistryObject<Item> SULFUR_ORE = ITEMS.register("sulfur_ore", () ->
            new BlockItem(BlockInit.SULFUR_ORE.get(), new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)));

    public static final RegistryObject<Item> ASPHALT = ITEMS.register("asphalt", () ->
            new BlockItem(BlockInit.ASPHALT.get(), new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)));

    // Fluid Items
    public static final RegistryObject<Item> OIL_BUCKET = ITEMS.register("oil_bucket", () ->
            new BucketItem(() -> FluidInit.OIL_FLUID.get(), new Item.Properties().group(ItemGroup.MISC).maxStackSize(1).containerItem(Items.BUCKET)));

    public static final RegistryObject<Item> DIESEL_BUCKET = ITEMS.register("diesel_bucket", () ->
            new BucketItem(() -> Fluids.EMPTY, new Item.Properties().group(ItemGroup.MISC).maxStackSize(1).containerItem(Items.BUCKET)));

    public static final RegistryObject<Item> BIODIESEL_BUCKET = ITEMS.register("biodiesel_bucket", () ->
            new BucketItem(() -> Fluids.EMPTY, new Item.Properties().group(ItemGroup.MISC).maxStackSize(1).containerItem(Items.BUCKET)));

    public static final RegistryObject<Item> GASOLINE_BUCKET = ITEMS.register("gasoline_bucket", () ->
            new BucketItem(() -> FluidInit.GASOLINE_FLUID.get(), new Item.Properties().group(ItemGroup.MISC).maxStackSize(1).containerItem(Items.BUCKET)));

    public static final RegistryObject<Item> PROPANE_BUCKET = ITEMS.register("propane_bucket", () ->
            new BucketItem(() -> Fluids.EMPTY, new Item.Properties().group(ItemGroup.MISC).maxStackSize(1).containerItem(Items.BUCKET)));

    public static final RegistryObject<Item> BIOGAS_BUCKET = ITEMS.register("biogas_bucket", () ->
            new BucketItem(() -> Fluids.EMPTY, new Item.Properties().group(ItemGroup.MISC).maxStackSize(1)));
}
