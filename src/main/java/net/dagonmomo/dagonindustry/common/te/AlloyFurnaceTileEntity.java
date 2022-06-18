package net.dagonmomo.dagonindustry.common.te;

import com.mojang.datafixers.util.Pair;
import net.dagonmomo.dagonindustry.common.container.AlloyFurnaceContainer;
import net.dagonmomo.dagonindustry.common.item.BatteryItem;
import net.dagonmomo.dagonindustry.core.util.maps.Recipes;
import net.dagonmomo.dagonindustry.core.util.registries.ModItems;
import net.dagonmomo.dagonindustry.core.util.registries.ModTileEntities;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
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
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import java.util.Arrays;
import java.util.List;

public class AlloyFurnaceTileEntity extends AbstractMultiblockTileEntity
{
    public static final int SLOTS = 5;
    public static int MAX_PROGRESS = 2400;

    protected AlloyFurnaceTileEntity(TileEntityType<?> typeIn)
    {
        super(typeIn);
    }

    public AlloyFurnaceTileEntity()
    {
        this(ModTileEntities.ALLOY_FURNACE);
    }

    @Override
    public List<BlockPos> getStructure(World world, Direction direction)
    {
        return Arrays.asList(
                new BlockPos(direction.getDirectionVec())
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
        return new TranslationTextComponent("container.dagon_industry.alloy_furnace");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player)
    {
        return new AlloyFurnaceContainer(id, player, this);
    }

    @Override
    public int getSizeInventory()
    {
        return 5;
    }


    @Override
    public void tick()
    {
        super.tick();

        // Is this block currently processing materials
        boolean isMaking = false;

        ItemStack batteryItem = this.getItemInSlot(0);

        if (world != null && !world.isRemote)
        {
            if (batteryItem.getItem() instanceof BatteryItem)
            {
                double charge = BatteryItem.getCharge(batteryItem);

                if (charge > 0)
                {
                    ItemStack input1Item = this.getItemInSlot(1);
                    ItemStack input2Item = this.getItemInSlot(2);
                    ItemStack outputItem = this.getItemInSlot(3);

                    // Get the resulting item from this combination (sensitive to the item order)
                    Pair<ItemStack, ItemStack> recipe = Recipes.ALLOYS.keySet().stream().filter(pair ->
                    pair.getFirst().getItem() == input1Item.getItem() && pair.getSecond().getItem() == input2Item.getItem())
                            .findFirst().orElse(null);

                    if (recipe != null)
                    {
                        ItemStack recipeResult = Recipes.ALLOYS.get(recipe);

                        if (recipeResult != null && (outputItem.isEmpty() || outputItem.getItem() == recipeResult.getItem())
                        && outputItem.getCount() < outputItem.getMaxStackSize())
                        {
                            this.getTileData().putBoolean("active", true);
                            isMaking = true;

                            this.setProgress(this.getProgress() + 1);

                            if (this.ticksExisted % 20 == 0)
                            {
                                BatteryItem.setCharge(batteryItem, BatteryItem.getCharge(batteryItem) - 1);
                            }

                            if (this.getProgress() >= MAX_PROGRESS)
                            {
                                this.setProgress(0);
                                input1Item.shrink(recipe.getFirst().getCount());
                                input2Item.shrink(recipe.getSecond().getCount());

                                if (Math.random() < 0.67)
                                {
                                    ItemStack slagStack = this.getItemInSlot(4);

                                    if (slagStack.isEmpty())
                                        this.setItemInSlot(4, new ItemStack(ModItems.SLAG));
                                    else if (slagStack.getItem() == ModItems.SLAG)
                                    {
                                        if (slagStack.getCount() < 64)
                                            slagStack.grow(1);
                                        else
                                        {
                                            ItemEntity item = new ItemEntity(this.world, this.pos.getX() + 0.5, this.pos.getY() + 1, this.pos.getZ() + 0.5,
                                                                             new ItemStack(ModItems.SLAG));
                                            item.setPosition(this.pos.getX() + 0.5, this.pos.getY() + 1, this.pos.getZ() + 0.5);
                                            item.setVelocity(Math.random() * 0.4 - 0.2, Math.random() * 0.2 + 0.2, Math.random() * 0.4 - 0.2);
                                            this.world.addEntity(item);
                                        }
                                    }
                                }
                                else
                                {
                                    if (outputItem.isEmpty())
                                        this.setItemInSlot(3, recipeResult);
                                    else if (outputItem.getItem() == recipeResult.getItem())
                                        outputItem.grow(1);
                                }
                            }
                        }
                        else
                        {
                            this.getTileData().putBoolean("active", false);
                        }
                    }
                }
            }

            if (!isMaking && this.getProgress() > 0)
            {
                this.setProgress(this.getProgress() - 1);
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

    public int getBurnTime()
    {
        return this.getTileData().getInt("burnTime");
    }
    public void setBurnTime(int burnTime)
    {
        this.getTileData().putInt("burnTime", burnTime);
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt)
    {
        super.read(state, nbt);
        this.setProgress(nbt.getInt("progress"));
        this.setBurnTime(nbt.getInt("burnTime"));
        this.inventory = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(nbt, this.inventory);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound)
    {
        super.write(compound);
        compound.putInt("progress", this.getProgress());
        compound.putInt("burnTime", this.getBurnTime());
        ItemStackHelper.saveAllItems(compound, inventory);
        return compound;
    }
}
