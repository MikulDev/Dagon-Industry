package net.dagonmomo.dagonindustry.common.te;

import net.dagonmomo.dagonindustry.common.container.ArcFurnaceContainer;
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
import org.antlr.v4.runtime.misc.Triple;

import java.util.*;

public class ArcFurnaceTileEntity extends AbstractMultiblockTileEntity
{
    public static final int slots = 5;
    public static int MAX_PROGRESS = 2400;

    protected ArcFurnaceTileEntity(TileEntityType<?> typeIn)
    {
        super(typeIn);
    }

    public List<BlockPos> getStructure(World world, Direction direction)
    {
        List<BlockPos> structure = new ArrayList<>();

        // Generate bounding box
        for (int x = -1; x < 2; x++)
        {
            for (int y = -1; y < 2; y++)
            {
                for (int z = -1; z < 2; z++)
                {
                    if (x == 0 && y == 0 && z == 0)
                        continue;

                    structure.add(new BlockPos(x, y, z));
                }
            }
        }
        return structure;
    }

    public ArcFurnaceTileEntity()
    {
        this(ModTileEntities.ARC_FURNACE);
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
        return new TranslationTextComponent("container.dagon_industry.arc_furnace");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player)
    {
        return new ArcFurnaceContainer(id, player, this);
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

        if (world != null && !world.isRemote)
        {
            if (batteryItem.getItem() instanceof BatteryItem && ((BatteryItem) batteryItem.getItem()).getPowerLevel() >= 1)
            {
                double charge = BatteryItem.getCharge(batteryItem);

                if (charge > 0)
                {
                    ItemStack input1Item = this.getItemInSlot(1);
                    ItemStack input2Item = this.getItemInSlot(2);
                    ItemStack outputItem = this.getItemInSlot(3);

                    // Get the resulting item from this combination (sensitive to the item order)
                    Triple<ItemStack, ItemStack, Double> recipe = Recipes.ARC_ALLOYS.keySet().stream().filter(triple ->
                    triple.a.getItem() == input1Item.getItem() && triple.b.getItem() == input2Item.getItem())
                    .findFirst().orElse(null);

                    if (recipe != null)
                    {
                        ItemStack recipeResult = Recipes.ARC_ALLOYS.get(recipe);

                        if (recipeResult != null && (outputItem.isEmpty() || outputItem.getItem() == recipeResult.getItem())
                        && outputItem.getCount() < outputItem.getMaxStackSize())
                        {
                            isMaking = true;
                            this.getTileData().putBoolean("active", true);

                            this.setProgress(this.getProgress() + 1);

                            if (this.ticksExisted % 20 == 0)
                            {
                                BatteryItem.setCharge(batteryItem, BatteryItem.getCharge(batteryItem) - 0.5);
                            }

                            if (this.getProgress() >= MAX_PROGRESS)
                            {
                                this.setProgress(0);
                                input2Item.shrink(recipe.b.getCount());

                                if (Math.random() < recipe.c)
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
                                    input1Item.shrink(1);

                                    if (outputItem.isEmpty())
                                        this.setItemInSlot(3, recipeResult);
                                    else if (outputItem.getItem() == recipeResult.getItem())
                                        outputItem.grow(1);
                                }
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
                    this.setProgress(this.getProgress() - 1);
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
