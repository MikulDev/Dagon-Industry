package net.dagonmomo.dagonindustry.common.te;

import com.mojang.datafixers.util.Pair;
import net.dagonmomo.dagonindustry.common.container.CrusherContainer;
import net.dagonmomo.dagonindustry.common.item.BatteryItem;
import net.dagonmomo.dagonindustry.core.util.maps.Recipes;
import net.dagonmomo.dagonindustry.core.util.registries.ModTileEntities;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import java.util.Arrays;
import java.util.List;

public class CrusherTileEntity extends AbstractMultiblockTileEntity
{
    public static final int SLOTS = 3;
    public static int MAX_PROGRESS = 1200;

    protected CrusherTileEntity(TileEntityType<?> typeIn)
    {
        super(typeIn);
    }

    public CrusherTileEntity()
    {
        this(ModTileEntities.CRUSHER);
    }

    @Override
    public List<BlockPos> getStructure(World world, Direction direction)
    {
        return Arrays.asList(
                new BlockPos(direction.getXOffset(), 0, direction.getZOffset()),
                new BlockPos(direction.getOpposite().getXOffset(), 0, direction.getOpposite().getZOffset())
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
        return new TranslationTextComponent("container.dagon_industry.crusher");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player)
    {
        return new CrusherContainer(id, player, this);
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
            ItemStack inputStack = this.getItemInSlot(1);

            if (Recipes.CRUSHING.containsKey(inputStack.getItem())
            && batteryItem.getItem() instanceof BatteryItem && BatteryItem.getCharge(batteryItem) > 0)
            {
                Pair<Item, Integer> result = Recipes.CRUSHING.get(inputStack.getItem());
                ItemStack outputStack = new ItemStack(result.getFirst(), result.getSecond());
                ItemStack stackInOutput = this.getItemInSlot(2);

                if ((outputStack.isItemEqual(stackInOutput) && stackInOutput.getCount() < stackInOutput.getMaxStackSize()) || stackInOutput.isEmpty())
                {
                    this.getTileData().putBoolean("active", true);

                    if (this.ticksExisted % 20 == 0)
                    {
                        BatteryItem.setCharge(batteryItem, BatteryItem.getCharge(batteryItem) - 1);
                    }

                    this.setProgress(this.getProgress() + 1);

                    if (this.getProgress() >= MAX_PROGRESS)
                    {
                        this.getCap().ifPresent(c -> c.extractItem(1, 1, false));
                        this.getCap().ifPresent(c -> c.insertItem(2, outputStack, false));
                        this.setProgress(0);
                    }
                }
                else if (this.getProgress() > 0)
                {
                    this.getTileData().putBoolean("active", false);
                    this.setProgress(Math.max(0, this.getProgress() - 10));
                }
            }
            else if (this.getProgress() > 0)
            {
                this.getTileData().putBoolean("active", false);
                this.setProgress(Math.max(0, this.getProgress() - 10));
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

    public int getProgress()
    {
        return this.getTileData().getInt("progress");
    }

    public void setProgress(int progress)
    {
        this.getTileData().putInt("progress", progress);
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt)
    {
        super.read(state, nbt);
        this.setProgress(nbt.getInt("progress"));
        this.inventory = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(nbt, this.inventory);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound)
    {
        super.write(compound);
        compound.putInt("progress", this.getProgress());
        ItemStackHelper.saveAllItems(compound, inventory);
        return compound;
    }
}