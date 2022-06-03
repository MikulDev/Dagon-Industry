package net.dagonmomo.dagonindustry.core.util.maps;

import com.mojang.datafixers.util.Pair;
import net.dagonmomo.dagonindustry.common.item.MetalPlateItem;
import net.dagonmomo.dagonindustry.core.init.ItemInit;
import net.dagonmomo.dagonindustry.core.util.registries.ModItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.antlr.v4.runtime.misc.Triple;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class Recipes
{
    public static final Map<Item, Item> METAL_PLATES = new HashMap<>();
    public static final Map<Item, Pair<Item, Integer>> CRUSHING = new HashMap<>();
    public static final Map<Pair<ItemStack, ItemStack>, ItemStack> ALLOYS = new HashMap<>();
    public static final Map<Item, Item> LATHING = new HashMap<>();
    public static final Map<Triple<ItemStack, ItemStack, Double>, ItemStack> ARC_ALLOYS = new HashMap<>();
    public static final Map<Item, Integer> FUEL_BURN_TIMES = new HashMap<>();
    public static final Map<Item, Triple<List<Item>, Item, Double>> REFINING = new HashMap<>();
    public static final Map<Pair<List<Item>, Integer>, Item> WELDING = new HashMap<>();
    public static final Map<Pair<List<ItemStack>, Integer>, ItemStack> WORKBENCH = new HashMap<>();
    public static final Map<Triple<ItemStack, ItemStack, ItemStack>, ItemStack> BALLISTICS = new HashMap<>();

    @SubscribeEvent
    public static void setupRecipes(FMLCommonSetupEvent event)
    {
        // Metal plates
        for (RegistryObject<Item> object : ItemInit.ITEMS.getEntries())
        {
            if (object.get() instanceof MetalPlateItem)
            {
                METAL_PLATES.put(
                        ((MetalPlateItem) object.get()).getIngotItem(), // Ingot item
                        object.get()); // Plate item
            }
        }

        // Alloy Furnace
        ALLOYS.put(Pair.of(new ItemStack(Items.IRON_INGOT, 1), new ItemStack(Items.COAL, 3)),       new ItemStack(ModItems.STEEL_INGOT, 1));
        ALLOYS.put(Pair.of(new ItemStack(Items.IRON_INGOT, 1), new ItemStack(Items.CHARCOAL, 5)),   new ItemStack(ModItems.STEEL_INGOT, 1));

        // Arc Furnace
        ARC_ALLOYS.put(new Triple<>(new ItemStack(Items.IRON_INGOT, 1), new ItemStack(Items.COAL, 2), 0.1), new ItemStack(ModItems.STEEL_INGOT, 1));
        ARC_ALLOYS.put(new Triple<>(new ItemStack(ItemInit.COBALT_ORE.get(), 1), ItemStack.EMPTY, 0.2),     new ItemStack(ModItems.COBALT_PLATE, 1));

        // Crusher
        CRUSHING.put(Items.IRON_INGOT, Pair.of(Items.IRON_NUGGET, 12));
        CRUSHING.put(Items.GOLD_INGOT, Pair.of(Items.GOLD_NUGGET, 12));
        CRUSHING.put(ItemInit.SULFUR_ORE.get(), Pair.of(ItemInit.SULFUR_POWDER.get(), 4));

        // Lathing
        LATHING.put(Items.IRON_INGOT, ItemInit.IRON_ROD.get());
        LATHING.put(ItemInit.COBALT_PLATE.get(), ItemInit.COBALT_ROD.get());
        LATHING.put(ItemInit.STEEL_INGOT.get(), ItemInit.STEEL_ROD.get());
        LATHING.put(Items.NETHERITE_INGOT, ItemInit.NETHERITE_ROD.get());

        // Fuel Burn Times
        FUEL_BURN_TIMES.put(ItemInit.BIODIESEL_BUCKET.get(), 30);
        FUEL_BURN_TIMES.put(ItemInit.DIESEL_BUCKET.get(), 60);

        // Refining
        REFINING.put(ItemInit.OIL_BUCKET.get(), new Triple<>(
                Arrays.asList(ItemInit.DIESEL_BUCKET.get(), ItemInit.GASOLINE_BUCKET.get(), ItemInit.PROPANE_BUCKET.get()),
                ModItems.SLAG, 0.5));

        // Workbench
        WORKBENCH.put(Pair.of(Arrays.asList(new ItemStack(ModItems.STEEL_PLATE, 15),
                                            new ItemStack(ItemInit.STEEL_ROD.get(), 5),
                                            new ItemStack(ItemInit.COBALT_PLATE.get(), 2)), 300), new ItemStack(ItemInit.CRUDE_PISTOL.get(), 1));
        WORKBENCH.put(Pair.of(Arrays.asList(new ItemStack(ModItems.STEEL_PLATE, 10),
                                            new ItemStack(ItemInit.STEEL_ROD.get(), 10),
                                            new ItemStack(ItemInit.COBALT_PLATE.get(), 4)), 300), new ItemStack(ItemInit.CRUDE_RIFLE.get(), 1));
        WORKBENCH.put(Pair.of(Arrays.asList(new ItemStack(ModItems.STEEL_PLATE, 20),
                                            new ItemStack(ItemInit.STEEL_ROD.get(), 5),
                                            new ItemStack(ItemInit.COBALT_PLATE.get(), 5)), 300), new ItemStack(ItemInit.CRUDE_SNIPER.get(), 1));
        WORKBENCH.put(Pair.of(Arrays.asList(new ItemStack(ModItems.STEEL_PLATE, 5),
                                            new ItemStack(ItemInit.STEEL_ROD.get(), 20),
                                            new ItemStack(ItemInit.COBALT_PLATE.get(), 4)), 300), new ItemStack(ItemInit.CRUDE_SHOTGUN.get(), 1));

        WORKBENCH.put(Pair.of(Arrays.asList(new ItemStack(ModItems.STEEL_PLATE, 20),
                                            new ItemStack(ItemInit.COBALT_PLATE.get(), 5)), 300), new ItemStack(ItemInit.STEEL_PICKAXE.get(), 1));

        WORKBENCH.put(Pair.of(Arrays.asList(new ItemStack(ModItems.STEEL_PLATE, 10),
                                            new ItemStack(ItemInit.COBALT_PLATE.get(), 2)), 300), new ItemStack(ItemInit.STEEL_HELMET.get(), 1));
        WORKBENCH.put(Pair.of(Arrays.asList(new ItemStack(ModItems.STEEL_PLATE, 16),
                                            new ItemStack(ItemInit.COBALT_PLATE.get(), 4)), 300), new ItemStack(ItemInit.STEEL_CHESTPLATE.get(), 1));
        WORKBENCH.put(Pair.of(Arrays.asList(new ItemStack(ModItems.STEEL_PLATE, 14),
                                            new ItemStack(ItemInit.COBALT_PLATE.get(), 3)), 300), new ItemStack(ItemInit.STEEL_LEGGINGS.get(), 1));
        WORKBENCH.put(Pair.of(Arrays.asList(new ItemStack(ModItems.STEEL_PLATE, 8),
                                            new ItemStack(ItemInit.COBALT_PLATE.get(), 2)), 300), new ItemStack(ItemInit.STEEL_BOOTS.get(), 1));

        // Ammo Bench
        BALLISTICS.put(new Triple<>(new ItemStack(Items.GUNPOWDER, 1), new ItemStack(ItemInit.IRON_PLATE.get(), 1), new ItemStack(ItemInit.IRON_ROD.get(), 1)),
                       new ItemStack(com.mrcrayfish.guns.init.ModItems.BASIC_BULLET.get(), 10));
    }
}
