package net.dagonmomo.dagonindustry.common.te;

import net.dagonmomo.dagonindustry.common.capability.ChunkOilCapability;
import net.dagonmomo.dagonindustry.common.container.LatheContainer;
import net.dagonmomo.dagonindustry.common.container.PumpjackContainer;
import net.dagonmomo.dagonindustry.common.item.BatteryItem;
import net.dagonmomo.dagonindustry.core.init.ItemInit;
import net.dagonmomo.dagonindustry.core.init.TileEntityInit;
import net.minecraft.block.BlockState;
import net.minecraft.command.impl.TimeCommand;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import java.util.Arrays;
import java.util.List;

public class PumpjackTileEntity extends AbstractMultiblockTileEntity
{
    public static final int SLOTS = 6;
    public static int MAX_OIL = 600;

    double oil = 0;
    boolean isPumping = false;

    protected PumpjackTileEntity(TileEntityType<?> typeIn)
    {
        super(typeIn);
    }

    public PumpjackTileEntity()
    {
        this(TileEntityInit.PUMPJACK.get());
    }

    @Override
    public List<BlockPos> getStructure(World world, Direction direction)
    {
        Direction sideways = direction.rotateY();
        return Arrays.asList(
                new BlockPos(0, -1, 0),
                new BlockPos(sideways.getXOffset(), 0, sideways.getZOffset()),
                new BlockPos(sideways.getXOffset(), -1, sideways.getZOffset()),
                new BlockPos(sideways.getOpposite().getXOffset(), 0, sideways.getOpposite().getZOffset()),
                new BlockPos(sideways.getOpposite().getXOffset(), -1, sideways.getOpposite().getZOffset()),
                new BlockPos(sideways.getOpposite().getXOffset(), 1, sideways.getOpposite().getZOffset())
        );
    }

    @Override
    protected NonNullList<ItemStack> getItems()
    {
        return inventory;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> itemsIn)
    {
        inventory = itemsIn;
    }

    @Override
    protected ITextComponent getDefaultName()
    {
        return new TranslationTextComponent("container.dagon_industry.pumpjack");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player)
    {
        return new PumpjackContainer(id, player, this);
    }

    @Override
    public int getSizeInventory()
    {
        return SLOTS;
    }

    @Override
    public void tick()
    {
        super.tick();
        ItemStack batteryItem = this.getItemInSlot(0);

        if (world != null && !world.isRemote)
        {
            if (this.ticksExisted % 20 == 0)
            {
                isPumping = false;
                Chunk chunk = world.getChunkProvider().getChunkNow(pos.getX() >> 4, pos.getZ() >> 4);

                if (chunk != null)
                {
                    chunk.getCapability(ChunkOilCapability.CHUNK_OIL).ifPresent(cap ->
                    {
                        double chunkOil = cap.getOil();
                        if (chunkOil > 0 && oil < MAX_OIL
                        && batteryItem.getItem() instanceof BatteryItem && BatteryItem.getCharge(batteryItem) > 0)
                        {
                            isPumping = true;

                            cap.addOil(-1);
                            setOil(oil + 1);
                            BatteryItem.addCharge(batteryItem, -0.33);

                            if (oil >= 180)
                            {
                                for (int i = 1; i < 6; i++)
                                {
                                    if (inventory.get(i).getItem() == Items.BUCKET)
                                    {
                                        setItemInSlot(i, new ItemStack(ItemInit.OIL_BUCKET.get()));
                                        oil -= 180;
                                        break;
                                    }
                                }
                            }
                        }
                    });
                }
            }
        }
    }

    public LazyOptional<IItemHandler> getCap()
    {
        return this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
    }

    public ItemStack getItemInSlot(int index)
    {
        return getCap().map(c -> c.getStackInSlot(index)).orElse(ItemStack.EMPTY);
    }

    public void setItemInSlot(int index, ItemStack stack)
    {
        getCap().ifPresent(capability ->
        {
            capability.getStackInSlot(index).shrink(capability.getStackInSlot(index).getCount());
            capability.insertItem(index, stack, false);
        });
    }

    public double getOil()
    {
        return oil;
    }

    public void setOil(double oil)
    {
        this.oil = oil;
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt)
    {
        super.read(state, nbt);
        this.setOil(nbt.getDouble("oil"));
        this.isPumping = nbt.getBoolean("isPumping");
        this.inventory = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(nbt, this.inventory);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound)
    {
        super.write(compound);
        compound.putDouble("oil", this.getOil());
        compound.putBoolean("isPumping", this.isPumping);
        ItemStackHelper.saveAllItems(compound, inventory);
        return compound;
    }
}
