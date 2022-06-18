package net.dagonmomo.dagonindustry.common.te;

import net.dagonmomo.dagonindustry.common.container.AmmoBenchContainer;
import net.dagonmomo.dagonindustry.common.item.BatteryItem;
import net.dagonmomo.dagonindustry.core.init.TileEntityInit;
import net.dagonmomo.dagonindustry.core.util.maps.Recipes;
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
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.antlr.v4.runtime.misc.Triple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class AmmoBenchTileEntity extends AbstractMultiblockTileEntity
{
    public static final int SLOTS = 5;
    public static int MAX_PROGRESS = 60;

    protected AmmoBenchTileEntity(TileEntityType<?> typeIn)
    {
        super(typeIn);
    }

    public List<BlockPos> getStructure(World world, Direction direction)
    {
        return Arrays.asList(new BlockPos(direction.rotateYCCW().getDirectionVec()));
    }

    public AmmoBenchTileEntity()
    {
        this(TileEntityInit.AMMO_BENCH.get());
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
        return new TranslationTextComponent("container.dagon_industry.ammo_bench");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player)
    {
        return new AmmoBenchContainer(id, player, this);
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
        boolean isMaking = false;

        ItemStack batteryItem = this.getItemInSlot(0);

        if (world != null && !world.isRemote && this.ticksExisted % 20 == 0)
        {
            if (batteryItem.getItem() instanceof BatteryItem)
            {
                double charge = BatteryItem.getCharge(batteryItem);

                if (charge > 0)
                {
                    Triple<ItemStack, ItemStack, ItemStack> recipe = null;
                    ItemStack recipeResult = ItemStack.EMPTY;
                    List<ItemStack> ingredients = new ArrayList<>();

                    // Get the resulting item from this combination
                    for (Map.Entry<Triple<ItemStack, ItemStack, ItemStack>, ItemStack> entry : Recipes.BALLISTICS.entrySet())
                    {
                        int matches = 0;
                        Triple<ItemStack, ItemStack, ItemStack> triple = entry.getKey();
                        ingredients = Arrays.asList(triple.a, triple.b, triple.c);
                        // Test if the items in the crafting grid match the recipe
                        for (ItemStack ingredient : ingredients)
                        {
                            int itemsNeeded = ingredient.getCount();
                            for (int i = 0; i < 3; i++)
                            {
                                // We need this many of the item to satisfy the recipe
                                ItemStack inputStack = this.getItemInSlot(i + 1);

                                if (ingredient.isItemEqual(inputStack))
                                {
                                    // Deduct this stack's count from the total required
                                    itemsNeeded -= inputStack.getCount();
                                    // If we have enough of the item, continue to the next ingredient
                                    if (itemsNeeded <= 0)
                                    {
                                        matches++;
                                        break;
                                    }
                                }
                            }
                        }
                        if (matches >= ingredients.size())
                        {
                            recipe = triple;
                            recipeResult = entry.getValue();
                            break;
                        }
                    }

                    if (recipe != null)
                    {
                        ItemStack itemInOutput = this.getItemInSlot(4);

                        if ((itemInOutput.isEmpty() || itemInOutput.getItem() == recipeResult.getItem())
                        && itemInOutput.getCount() <= itemInOutput.getMaxStackSize() - recipeResult.getCount())
                        {
                            isMaking = true;
                            this.getTileData().putBoolean("active", true);

                            this.setProgress(this.getProgress() + 1);

                            BatteryItem.setCharge(batteryItem, BatteryItem.getCharge(batteryItem) - 1);

                            if (this.getProgress() >= MAX_PROGRESS)
                            {
                                this.setProgress(0);
                                for (ItemStack stack : ingredients)
                                {
                                    for (int i = 0; i < 3; i++)
                                    {
                                        // Get how many items need to be removed
                                        int itemsToRemove = stack.getCount();

                                        ItemStack stackInSlot = this.getItemInSlot(i + 1);
                                        // If the item in the slot is the same as the ingredient
                                        if (stack.isItemEqual(stackInSlot))
                                        {
                                            // Account for the number of items in this slot
                                            int slotCount = stackInSlot.getCount();
                                            // Remove the items from the slot
                                            stackInSlot.shrink(itemsToRemove);
                                            // Count these items for the recipe
                                            itemsToRemove -= slotCount;
                                        }
                                        // If we've removed enough items, move on to the next ingredient
                                        if (itemsToRemove <= 0) break;
                                    }
                                }

                                if (itemInOutput.isEmpty())
                                    this.setItemInSlot(4, recipeResult);
                                else if (itemInOutput.getItem() == recipeResult.getItem())
                                    itemInOutput.grow(recipeResult.getCount());
                            }
                        }
                    }
                }
            }

            if (!isMaking)
            {
                this.getTileData().putBoolean("active", false);
                if (this.getProgress() > 0)
                {
                    this.setProgress(this.getProgress() - 10);
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
            capability.insertItem(index, stack.copy(), false);
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
