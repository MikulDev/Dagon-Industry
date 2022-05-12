package net.dagonmomo.dagonindustry.common.block;

import net.dagonmomo.dagonindustry.common.te.AlloyFurnaceTileEntity;
import net.dagonmomo.dagonindustry.core.network.PacketHandler;
import net.dagonmomo.dagonindustry.core.network.packet.BlockDataUpdateMessage;
import net.dagonmomo.dagonindustry.core.util.registries.ModTileEntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.fml.network.PacketDistributor;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class MachineBlock<TE extends LockableTileEntity> extends Block
{
    public static DirectionProperty FACING = DirectionProperty.create("facing", Direction.Plane.HORIZONTAL);
    Supplier<TileEntityType<TE>> tileEntityGetter;

    public MachineBlock(Supplier<TileEntityType<TE>> tileEntityGetter, Properties properties)
    {
        super(properties);
        this.tileEntityGetter = tileEntityGetter;
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(FACING);
    }

    @Override
    public BlockState rotate(BlockState state, IWorld world, BlockPos pos, Rotation direction)
    {
        return state.with(FACING, direction.rotate(state.get(FACING)));
    }

    @SuppressWarnings("deprecation")
    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult)
    {
        if (!worldIn.isRemote)
        {
            try
            {
                TE te = (TE) worldIn.getTileEntity(pos);

                NetworkHooks.openGui((ServerPlayerEntity) player, te, pos);
            }
            catch (Exception e) {}
        }
        return ActionResultType.SUCCESS;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return tileEntityGetter.get().create();
    }
}
