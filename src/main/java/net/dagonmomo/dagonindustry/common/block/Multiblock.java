package net.dagonmomo.dagonindustry.common.block;

import net.dagonmomo.dagonindustry.common.te.MultiBlockPieceTileEntity;
import net.dagonmomo.dagonindustry.core.util.registries.ModTileEntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class Multiblock extends Block
{
    public Multiblock(Properties properties)
    {
        super(properties);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos)
    {
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    public float getAmbientOcclusionLightValue(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return 1.0F;
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
    {
        BlockPos ownerPos = ((MultiBlockPieceTileEntity)worldIn.getTileEntity(pos)).getOwnerPos();
        return worldIn.getBlockState(ownerPos).getBlock().onBlockActivated(state, worldIn, ownerPos, player, handIn, hit);
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return ModTileEntities.MULTIBLOCK.create();
    }

    @Override
    public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player)
    {
        if (world.getTileEntity(pos) instanceof MultiBlockPieceTileEntity)
        {
            TileEntity ownerTileEntity = world.getTileEntity(((MultiBlockPieceTileEntity) world.getTileEntity(pos)).getOwnerPos());
            return ownerTileEntity.getBlockState().getBlock().getPickBlock(state, target, world, pos, player);
        }
        else return ItemStack.EMPTY;
    }
}
