package net.dagonmomo.dagonindustry.core;

import net.dagonmomo.dagonindustry.core.util.registries.ModItems;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ModItemGroup extends ItemGroup
{
    public static final ModItemGroup DAGON_INDUSTRY = new ModItemGroup("dagon_industry");

    public ModItemGroup(String name) {
        super(name);
    }

    @Override
    public ItemStack createIcon()
    {
        return ModItems.STEEL_INGOT.getDefaultInstance();
    }
}
