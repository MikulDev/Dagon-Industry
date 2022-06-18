package net.dagonmomo.dagonindustry.common.item;

import net.dagonmomo.dagonindustry.core.ModItemGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class MetalRodItem extends Item
{
    private final Item ingotItem;

    public MetalRodItem(Item ingotItem)
    {
        super(new Properties().group(ModItemGroup.DAGON_INDUSTRY));
        this.ingotItem = ingotItem;
    }

    public Item getIngotItem()
    {
        return ingotItem;
    }
}
