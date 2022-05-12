package net.dagonmomo.dagonindustry.common.te;

import net.dagonmomo.dagonindustry.common.container.AbstractMachineContainer;
import net.dagonmomo.dagonindustry.core.network.PacketHandler;
import net.dagonmomo.dagonindustry.core.network.packet.BlockDataUpdateMessage;
import net.dagonmomo.dagonindustry.core.util.registries.ModBlocks;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.DirectionProperty;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractMultiblockTileEntity extends LockableLootTileEntity implements ITickableTileEntity
{
    NonNullList<ItemStack> inventory = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
    private List<BlockPos> structure = new ArrayList<>();
    public int ticksExisted = 0;

    protected AbstractMultiblockTileEntity(TileEntityType<?> typeIn)
    {
        super(typeIn);
    }

    @Override
    protected abstract NonNullList<ItemStack> getItems();

    @Override
    protected abstract void setItems(NonNullList<ItemStack> itemsIn);

    @Override
    protected abstract ITextComponent getDefaultName();

    @Override
    protected abstract Container createMenu(int id, PlayerInventory player);

    @Override
    public abstract int getSizeInventory();

    public abstract List<BlockPos> getStructure(World world, Direction direction);

    @Override
    public void tick()
    {
        if (world != null && !world.isRemote && this.ticksExisted % 10 == 0)
        {
            for (PlayerEntity player : getWorld().getPlayers())
            {
                if (player.openContainer instanceof AbstractMachineContainer && ((AbstractMachineContainer) player.openContainer).te == this)
                {
                    PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player),
                                                new BlockDataUpdateMessage(pos, this.write(new CompoundNBT())));
                }
            }
        }
        if (world != null && this.ticksExisted++ == 1)
        {
            try
            {
                structure = getStructure(getWorld(), (Direction) getBlockState().getProperties().stream().filter(prop -> prop instanceof DirectionProperty)
                                                 .findFirst().get().getValuePair(getBlockState()).getValue());
            }
            catch (Exception e) {}

            for (BlockPos blockPos : structure)
            {
                world.setBlockState(blockPos.add(this.pos), ModBlocks.MULTIBLOCK.getDefaultState(), 3);
                if (world.getTileEntity(blockPos.add(this.pos)) instanceof MultiBlockPieceTileEntity)
                {
                    MultiBlockPieceTileEntity fillerTile = (MultiBlockPieceTileEntity) world.getTileEntity(blockPos.add(this.pos));
                    fillerTile.ownerPos = this.pos;
                }
            }
        }
    }

    @Override
    public void remove()
    {
        for (ItemStack stack : inventory)
        {
            if (!stack.isEmpty())
            {
                InventoryHelper.spawnItemStack(getWorld(), getPos().getX(), getPos().getY(), getPos().getZ(), stack);
            }
        }
        for (BlockPos pos : structure)
        {
            getWorld().setBlockState(pos.add(this.pos), Blocks.AIR.getDefaultState(), 3);
        }
        super.remove();
    }
}
