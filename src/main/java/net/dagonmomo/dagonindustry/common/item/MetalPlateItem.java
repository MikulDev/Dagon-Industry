package net.dagonmomo.dagonindustry.common.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class MetalPlateItem extends Item
{
    private final Item ingotItem;

    public MetalPlateItem(Item ingotItem)
    {
        super(new Item.Properties().group(ItemGroup.MISC));
        this.ingotItem = ingotItem;
    }

    public Item getIngotItem()
    {
        return ingotItem;
    }
}
