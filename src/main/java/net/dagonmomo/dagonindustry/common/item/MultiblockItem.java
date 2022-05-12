package net.dagonmomo.dagonindustry.common.item;

import net.dagonmomo.dagonindustry.common.block.MachineBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Collection;
import java.util.function.BiFunction;

public class MultiblockItem extends BlockItem
{
    BiFunction<World, Direction, Collection<BlockPos>> posGetter;
    Block block;
    public MultiblockItem(Block blockIn, Properties builder, BiFunction<World, Direction, Collection<BlockPos>> posGetter)
    {
        super(blockIn, builder);
        this.block = blockIn;
        this.posGetter = posGetter;
    }

    @Override
    public ActionResultType tryPlace(BlockItemUseContext context)
    {
        try
        {
            Collection<BlockPos> positions = posGetter.apply(context.getWorld(), block.getStateForPlacement(context).get(MachineBlock.FACING));

            if (positions.stream().allMatch(pos -> context.getWorld().getBlockState(context.getPos().add(pos)).isReplaceable(context)))
            {
                return super.tryPlace(context);
            }
            return ActionResultType.FAIL;
        }
        catch (Exception e)
        {
            return ActionResultType.FAIL;
        }
    }
}
