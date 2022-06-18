package net.dagonmomo.dagonindustry.common.item;

import net.dagonmomo.dagonindustry.core.ModItemGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class MetalPlateItem extends Item
{
    private final Item ingotItem;

    public MetalPlateItem(Item ingotItem)
    {
        super(new Item.Properties().group(ModItemGroup.DAGON_INDUSTRY));
        this.ingotItem = ingotItem;
    }

    public Item getIngotItem()
    {
        return ingotItem;
    }
}
