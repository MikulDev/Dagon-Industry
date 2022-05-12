package net.dagonmomo.dagonindustry.common.item;

import net.dagonmomo.dagonindustry.core.util.registries.ModItems;
import net.minecraft.item.IItemTier;
import net.minecraft.item.crafting.Ingredient;

public class SteelItemTier implements IItemTier
{
    @Override
    public int getMaxUses() {
        return 500;
    }

    @Override
    public float getEfficiency() {
        return 7f;
    }

    @Override
    public float getAttackDamage() {
        return 2f;
    }

    @Override
    public int getHarvestLevel() {
        return 2;
    }

    @Override
    public int getEnchantability() {
        return 18;
    }

    @Override
    public Ingredient getRepairMaterial() {
        return Ingredient.fromItems(ModItems.STEEL_INGOT);
    }
}
