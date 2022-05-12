package net.dagonmomo.dagonindustry.common.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.NonNullList;

public class BatteryItem extends Item
{
    final int maxCharge;
    final int powerLevel;

    public BatteryItem(int maxCharge, int powerLevel, Item.Properties properties)
    {
        super(properties.maxStackSize(1));
        this.maxCharge = maxCharge;
        this.powerLevel = powerLevel;
    }

    @Override
    public int getRGBDurabilityForDisplay(ItemStack stack)
    {
        return 839423;
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack)
    {
        return getCharge(stack) < maxCharge;
    }

    public double getMaxCharge()
    {
        return maxCharge;
    }

    public static double getCharge(ItemStack stack)
    {
        return stack.getOrCreateTag().getDouble("charge");
    }

    public static void setCharge(ItemStack stack, double charge)
    {
        stack.getOrCreateTag().putDouble("charge", charge);
    }

    public static void addCharge(ItemStack stack, double charge)
    {
        stack.getOrCreateTag().putDouble("charge", getCharge(stack) + charge);
    }

    public int getPowerLevel()
    {
        return powerLevel;
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items)
    {
        if (this.isInGroup(group))
        {
            ItemStack stack = new ItemStack(this);
            setCharge(stack, maxCharge);
            items.add(stack);
        }
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack)
    {
        return (maxCharge - stack.getOrCreateTag().getDouble("charge")) / maxCharge;
    }
}
