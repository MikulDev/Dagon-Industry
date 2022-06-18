package net.dagonmomo.dagonindustry.common.te;

import com.mojang.datafixers.util.Pair;
import net.dagonmomo.dagonindustry.common.container.WorkbenchContainer;
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

import java.util.*;
import java.util.stream.Collectors;

public class WorkbenchTileEntity extends AbstractMultiblockTileEntity
{
    public static final int SLOTS = 11;

    public static List<Pair<List<ItemStack>, Integer>> ORDERED_RECIPES = Recipes.WORKBENCH.keySet().stream().sorted(
            Comparator.comparing(pair -> Recipes.WORKBENCH.get(pair).getDisplayName().getString())).collect(
            Collectors.toList());

    int craftingTime = 0;
    int progress = 0;
    int recipeIndex = 0;
    boolean isCrafting = false;

    protected WorkbenchTileEntity(TileEntityType<?> typeIn)
    {
        super(typeIn);
    }

    public WorkbenchTileEntity()
    {
        this(TileEntityInit.WORKBENCH.get());
    }

    @Override
    public List<BlockPos> getStructure(World world, Direction direction)
    {
        Direction sideways = direction.rotateY();
        return Arrays.asList(
                new BlockPos(0, -1, 0),
                new BlockPos(sideways.getDirectionVec()),
                new BlockPos(sideways.getDirectionVec()).down(),
                new BlockPos(sideways.getOpposite().getDirectionVec()),
                new BlockPos(sideways.getOpposite().getDirectionVec()).down()
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
        return new TranslationTextComponent("container.dagon_industry.workbench");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player)
    {
        return new WorkbenchContainer(id, player, this);
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
                isCrafting = false;

                // Make sure the recipe index is in range
                recipeIndex = Math.min(ORDERED_RECIPES.size() - 1, recipeIndex);
                // Get the recipe associated with the index
                Pair<List<ItemStack>, Integer> recipe = ORDERED_RECIPES.get(recipeIndex);

                if (batteryItem.getItem() instanceof BatteryItem && BatteryItem.getCharge(batteryItem) > 0
                && recipe != null)
                {
                    int matches = 0;
                    // Test if the items in the crafting grid match the recipe
                    for (ItemStack recipeStack : recipe.getFirst())
                    {
                        // Items needed for this ingredient to be satisfied
                        int itemsNeeded = recipeStack.getCount();

                        for (int i = 0; i < 9; i++)
                        {
                            // We need this many of the item to satisfy the recipe
                            ItemStack stack = this.getItemInSlot(i + 1);

                            if (recipeStack.isItemEqual(stack))
                            {
                                // Deduct this stack's count from the total required
                                itemsNeeded -= stack.getCount();
                                // If we have enough of the item, continue to the next ingredient
                                if (itemsNeeded <= 0)
                                {
                                    matches++;
                                    break;
                                }
                            }
                        }
                    }
                    /*
                     * If the items in the crafting grid satisfy the recipe, use this recipe
                     */
                    if (matches >= recipe.getFirst().size())
                    {
                        // Recipe output
                        ItemStack outputStack = Recipes.WORKBENCH.get(recipe).copy();
                        // Recipe ingredients
                        List<ItemStack> inputStacks = recipe.getFirst();
                        // Stack currently in output slot
                        ItemStack stackInOutput = getItemInSlot(10);
                        // Time it takes to craft this recipe
                        this.setCraftingTime(recipe.getSecond());

                        // If there is room in the output slot
                        if (stackInOutput.isEmpty() || stackInOutput.isItemEqual(outputStack)
                        && stackInOutput.getCount() < stackInOutput.getMaxStackSize() - outputStack.getCount())
                        {
                            // Mark conditions valid for crafting
                            isCrafting = true;

                            BatteryItem.setCharge(batteryItem, BatteryItem.getCharge(batteryItem) - 1);

                            this.setProgress(this.getProgress() + 1);

                            // If crafting is finished
                            if (this.getProgress() >= this.getCraftingTime())
                            {
                                // add the output to the output slot
                                this.getCap().ifPresent(cap -> cap.insertItem(10, outputStack, false));
                                // Remove the ingredients from the crafting grid
                                for (ItemStack stack : inputStacks)
                                {
                                    for (int i = 0; i < 9; i++)
                                    {
                                        // Get how many items need to be removed
                                        int itemsToRemove = stack.getCount();

                                        ItemStack stackInSlot = this.getItemInSlot(i + 1);
                                        // If the item in the slot is the same as the ingredient
                                        if (stack.isItemEqual(stackInSlot))
                                        {
                                            // Get the number of items in this slot
                                            int slotCount = stackInSlot.getCount();
                                            // Remove the items from the slot
                                            stackInSlot.shrink(itemsToRemove);
                                            // Decrease the number of items left to satisfy the recipe
                                            itemsToRemove -= slotCount;
                                        }
                                        // If we've removed enough items, move on to the next ingredient
                                        if (itemsToRemove <= 0) break;
                                    }
                                }

                                // Reset progress for the next cycle
                                this.setProgress(0);
                            }
                        }
                    }
                }
                // Save the state of this machine to NBT
                this.getTileData().putBoolean("active", isCrafting);

            }
            // If conditions are invalid for crafting, reverse progress
            if (!isCrafting && this.getProgress() > 0)
            {
                this.setProgress(Math.max(0, this.getProgress() - 2));
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
        return progress;
    }
    public void setProgress(int progress)
    {
        this.progress = progress;
    }

    public int getCraftingTime()
    {
        return craftingTime;
    }
    public void setCraftingTime(int craftingTime)
    {
        this.craftingTime = craftingTime;
    }

    public int getRecipeIndex()
    {
        return recipeIndex;
    }
    public void setRecipeIndex(int recipeIndex)
    {
        this.recipeIndex = recipeIndex;
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt)
    {
        super.read(state, nbt);
        this.setProgress(nbt.getInt("progress"));
        this.setCraftingTime(nbt.getInt("craftingTime"));
        this.setRecipeIndex(nbt.getInt("recipeIndex"));
        this.getTileData().putBoolean("active", nbt.getBoolean("active"));
        this.inventory = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(nbt, this.inventory);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound)
    {
        super.write(compound);
        compound.putInt("progress", this.getProgress());
        compound.putInt("craftingTime", this.getCraftingTime());
        compound.putInt("recipeIndex", this.getRecipeIndex());
        compound.putBoolean("active", this.getTileData().getBoolean("active"));
        ItemStackHelper.saveAllItems(compound, inventory);
        return compound;
    }
}
