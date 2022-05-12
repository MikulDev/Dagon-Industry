package net.dagonmomo.dagonindustry.common.te;

import net.dagonmomo.dagonindustry.common.container.RefineryContainer;
import net.dagonmomo.dagonindustry.common.item.BatteryItem;
import net.dagonmomo.dagonindustry.core.init.TileEntityInit;
import net.dagonmomo.dagonindustry.core.util.maps.Recipes;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.Item;
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
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.antlr.v4.runtime.misc.Triple;

import java.util.Arrays;
import java.util.List;

public class RefineryTileEntity extends AbstractMultiblockTileEntity
{
    public static final int SLOTS = 6;
    public static int MAX_PROGRESS = 2400;

    protected RefineryTileEntity(TileEntityType<?> typeIn)
    {
        super(typeIn);
    }

    public RefineryTileEntity()
    {
        this(TileEntityInit.REFINERY.get());
    }

    @Override
    public List<BlockPos> getStructure(World world, Direction direction)
    {
        return Arrays.asList(
                new BlockPos(0, -1, 0),
                new BlockPos(0, 1, 0),
                new BlockPos(direction.getDirectionVec()),
                new BlockPos(direction.getDirectionVec()).up(),
                new BlockPos(direction.getOpposite().getDirectionVec()).down(),
                new BlockPos(direction.rotateYCCW().getDirectionVec()).down(),
                new BlockPos(direction.rotateYCCW().getDirectionVec())
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
        return new TranslationTextComponent("container.dagon_industry.refinery");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player)
    {
        return new RefineryContainer(id, player, this);
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
            ItemStack inputItem = this.getItemInSlot(1);

            if (Recipes.REFINING.containsKey(inputItem.getItem())
            && batteryItem.getItem() instanceof BatteryItem && BatteryItem.getCharge(batteryItem) > 0)
            {
                Triple<List<Item>, Item, Double> recipe = Recipes.REFINING.get(inputItem.getItem());
                List<Item> outputItems = recipe.a;
                List<ItemStack> stacksInOutput = this.inventory.subList(2, 5);

                if (stacksInOutput.stream().allMatch(stack -> stack.getItem() == Items.BUCKET))
                {
                    this.getTileData().putBoolean("active", true);

                    if (this.ticksExisted % 20 == 0)
                    {
                        BatteryItem.setCharge(batteryItem, BatteryItem.getCharge(batteryItem) - 1);
                    }

                    this.setProgress(this.getProgress() + 1);

                    if (this.getProgress() >= MAX_PROGRESS)
                    {
                        this.setItemInSlot(1, inputItem.getContainerItem());

                        if (Math.random() < recipe.c)
                        {
                            this.setItemInSlot(5, recipe.b.getDefaultInstance());
                        }
                        else
                        {
                            for (int i = 0; i < 3; i++)
                            {
                                this.setItemInSlot(i + 2, new ItemStack(outputItems.get(i), 2));
                            }
                        }
                        this.setProgress(0);
                    }
                }
                else
                {
                    if (this.getProgress() > 0)
                        this.setProgress(Math.max(0, this.getProgress() - 10));
                    this.getTileData().putBoolean("active", false);
                }
            }
            else
            {
                if (this.getProgress() > 0)
                    this.setProgress(Math.max(0, this.getProgress() - 10));
                this.getTileData().putBoolean("active", false);
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
        this.getTileData().putBoolean("active", nbt.getBoolean("active"));
        this.inventory = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(nbt, this.inventory);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound)
    {
        super.write(compound);
        compound.putInt("progress", this.getProgress());
        compound.putBoolean("active", this.getTileData().getBoolean("active"));
        ItemStackHelper.saveAllItems(compound, inventory);
        return compound;
    }
}
