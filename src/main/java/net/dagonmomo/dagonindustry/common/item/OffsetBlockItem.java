package net.dagonmomo.dagonindustry.common.item;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;

import java.util.Collection;
import java.util.function.BiFunction;

public class OffsetBlockItem extends MultiblockItem
{
    Vector3i offset;

    public OffsetBlockItem(Block blockIn, Properties builder, BiFunction<World, Direction, Collection<BlockPos>> posGetter, Vector3i offset)
    {
        super(blockIn, builder, posGetter);
        this.offset = offset;
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context)
    {
        ActionResultType actionresulttype = this.tryPlace(new OffsetItemUseContext(context).offset(offset));

        return !actionresulttype.isSuccessOrConsume() && this.isFood()
                ? this.onItemRightClick(context.getWorld(), context.getPlayer(), context.getHand()).getType()
                : actionresulttype;
    }

    public static class OffsetItemUseContext extends BlockItemUseContext
    {
        private Vector3i offset;

        public OffsetItemUseContext(ItemUseContext context)
        {
            super(context);
        }

        public OffsetItemUseContext offset(Vector3i offset)
        {
            this.offset = offset;
            return this;
        }

        public OffsetItemUseContext offset(int x, int y, int z)
        {
            return this.offset(new Vector3i(x, y, z));
        }

        @Override
        public BlockPos getPos()
        {
            return super.getPos().add(offset);
        }
    }
}
