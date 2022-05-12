package net.dagonmomo.dagonindustry.common.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class MetalRodItem extends Item
{
    private final Item ingotItem;

    public MetalRodItem(Item ingotItem)
    {
        super(new Properties().group(ItemGroup.MISC));
        this.ingotItem = ingotItem;
    }

    public Item getIngotItem()
    {
        return ingotItem;
    }
}
