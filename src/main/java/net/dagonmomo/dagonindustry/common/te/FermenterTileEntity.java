package net.dagonmomo.dagonindustry.common.te;

import net.dagonmomo.dagonindustry.common.container.FermenterContainer;
import net.dagonmomo.dagonindustry.common.item.BatteryItem;
import net.dagonmomo.dagonindustry.core.util.DIMath;
import net.dagonmomo.dagonindustry.core.util.registries.ModItems;
import net.dagonmomo.dagonindustry.core.util.registries.ModTileEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.ComposterBlock;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.IntNBT;
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

public class FermenterTileEntity extends AbstractMultiblockTileEntity
{
    public static final int SLOTS = 7;
    public static int MAX_PROGRESS = 1200;

    boolean shouldUseBattery = false;

    protected FermenterTileEntity(TileEntityType<?> typeIn)
    {
        super(typeIn);
        for (int i = 0; i < SLOTS - 2; i++)
        {
            this.getTileData().getList("progress", 3).add(IntNBT.valueOf(0));
        }
    }

    @Override
    public List<BlockPos> getStructure(World world, Direction direction)
    {
        return Arrays.asList(
                new BlockPos(0, -1, 0),
                new BlockPos(0, 1, 0)
        );
    }

    public FermenterTileEntity()
    {
        this(ModTileEntities.FERMENTER);
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
        return new TranslationTextComponent("container.dagon_industry.fermenter");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player)
    {
        return new FermenterContainer(id, player, this);
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
        boolean hasBattery = batteryItem.getItem() instanceof BatteryItem && BatteryItem.getCharge(batteryItem) > 0;

        if (world != null && !world.isRemote)
        {
            if (this.ticksExisted % 5 == 0)
            {
                shouldUseBattery = false;
                ItemStack output = this.getItemInSlot(6);
                int fermentFactor = hasBattery ? 5 : 1;
                int outputCount = 0;

                for (int i = 1; i < 6; i++)
                {
                    ItemStack stack = this.getItemInSlot(i);
                    if (!stack.isEmpty() && isFermentableItem(stack))
                    {
                        int value = getFermentValue(stack);
                        if (output.isEmpty() || output.getItem() == ModItems.BIOMASS && output.getCount() <= 64 - outputCount - value)
                        {
                            shouldUseBattery = true;
                            this.setProgress(i, this.getProgress(i) + fermentFactor);
                            if (this.getProgress(i) >= MAX_PROGRESS)
                            {
                                outputCount += value;

                                this.setProgress(i, 0);
                                this.getItemInSlot(i).shrink(1);
                            }
                        }
                    }
                    else
                    {
                        this.setProgress(i, 0);
                    }
                }

                if (output.isEmpty())
                    this.setItemInSlot(6, new ItemStack(ModItems.BIOMASS, outputCount));
                else
                    output.grow(outputCount);

                if (this.ticksExisted % 20 == 0 && hasBattery && shouldUseBattery)
                {
                    BatteryItem.setCharge(batteryItem, BatteryItem.getCharge(batteryItem) - 1);
                }
                this.getTileData().putBoolean("active", hasBattery && shouldUseBattery);
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

    public int getProgress(int slot)
    {
        if (!DIMath.isBetween(slot, 1, 5)) return 0;

        return this.getTileData().getInt("progress_" + slot);
    }

    public void setProgress(int slot, int progress)
    {
        if (!DIMath.isBetween(slot, 1, 5)) return;

        this.getTileData().putInt("progress_" + slot, progress);
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt)
    {
        super.read(state, nbt);
        for (int i = 1; i < 6; i++)
        {
            this.setProgress(i, nbt.getInt("progress_" + i));
        }
        this.inventory = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(nbt, this.inventory);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound)
    {
        super.write(compound);
        for (int i = 1; i < 6; i++)
        {
            compound.putInt("progress_" + i, this.getProgress(i));
        }
        ItemStackHelper.saveAllItems(compound, inventory);
        return compound;
    }

    public boolean isFermentableItem(ItemStack stack)
    {
        return ComposterBlock.CHANCES.containsKey(stack.getItem()) || stack.isFood();
    }

    public int getFermentValue(ItemStack stack)
    {
        if (stack.isFood())
        {
            return (int) Math.ceil(stack.getItem().getFood().getHealing() / 2d);
        }
        else if (ComposterBlock.CHANCES.containsKey(stack.getItem()))
        {
            return (int) Math.ceil(ComposterBlock.CHANCES.get(stack.getItem()) * 3);
        }
        return 0;
    }
}
