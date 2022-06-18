package net.dagonmomo.dagonindustry.common.te;

import net.dagonmomo.dagonindustry.common.container.SteamhammerContainer;
import net.dagonmomo.dagonindustry.common.item.BatteryItem;
import net.dagonmomo.dagonindustry.core.util.maps.Recipes;
import net.dagonmomo.dagonindustry.core.util.registries.ModTileEntities;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import java.util.Arrays;
import java.util.List;

public class SteamhammerTileEntity extends AbstractMultiblockTileEntity
{
    public static final int slots = 3;
    public static int MAX_PROGRESS = 1200;

    protected SteamhammerTileEntity(TileEntityType<?> typeIn)
    {
        super(typeIn);
    }

    public SteamhammerTileEntity()
    {
        this(ModTileEntities.STEAMHAMMER);
    }

    @Override
    public List<BlockPos> getStructure(World world, Direction direction)
    {
        return Arrays.asList(
                new BlockPos(0, 1, 0),
                new BlockPos(direction.getOpposite().getXOffset(), 0, direction.getOpposite().getZOffset()),
                new BlockPos(direction.getOpposite().getXOffset(), 1, direction.getOpposite().getZOffset())
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
        return new TranslationTextComponent("container.dagon_industry.steamhammer");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player)
    {
        return new SteamhammerContainer(id, player, this);
    }

    @Override
    public int getSizeInventory()
    {
        return 3;
    }

    @Override
    public void tick()
    {
        super.tick();
        ItemStack batteryItem = this.getItemInSlot(0);

        if (world != null && !world.isRemote)
        {
            ItemStack ingotItem = this.getItemInSlot(1);

            // If the ingredient has a valid recipe associated with it, and there is a battery
            if (Recipes.METAL_PLATES.containsKey(ingotItem.getItem())
            && batteryItem.getItem() instanceof BatteryItem && BatteryItem.getCharge(batteryItem) > 0)
            {
                // The item that will be outputted
                ItemStack outputStack = Recipes.METAL_PLATES.get(ingotItem.getItem()).getDefaultInstance();
                // The item currently in the output slot
                ItemStack stackInOutput = this.getItemInSlot(2);

                // If the output stack can be combined with the current item in the output slot, or the slot is empty
                if ((outputStack.isItemEqual(stackInOutput) && stackInOutput.getCount() < stackInOutput.getMaxStackSize()) || stackInOutput.isEmpty())
                {
                    // Mark the tile entity as making progress (used for GUI)
                    this.getTileData().putBoolean("active", true);

                    // Drain the battery
                    if (this.ticksExisted % 20 == 0)
                    {
                        BatteryItem.setCharge(batteryItem, BatteryItem.getCharge(batteryItem) - 1);
                    }

                    this.setProgress(this.getProgress() + 1);

                    // If the progress is complete, output the item and reset
                    if (this.getProgress() >= MAX_PROGRESS)
                    {
                        // Remove the input item
                        this.getCap().ifPresent(c -> c.extractItem(1, 1, false));
                        // Add the output item
                        this.getCap().ifPresent(c -> c.insertItem(2, outputStack, false));
                        // Reset the progress
                        this.setProgress(0);
                    }
                }
                // If conditions for crafting suddenly become invalid, drain progress
                else if (this.getProgress() > 0)
                {
                    this.getTileData().putBoolean("active", false);
                    this.setProgress(Math.max(0, this.getProgress() - 10));
                }
            }
            // If conditions for crafting suddenly become invalid, drain progress
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
