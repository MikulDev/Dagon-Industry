package net.dagonmomo.dagonindustry.common.te;

import net.dagonmomo.dagonindustry.common.container.CarbonGeneratorContainer;
import net.dagonmomo.dagonindustry.common.container.DieselGeneratorContainer;
import net.dagonmomo.dagonindustry.common.item.BatteryItem;
import net.dagonmomo.dagonindustry.core.init.ItemInit;
import net.dagonmomo.dagonindustry.core.init.TileEntityInit;
import net.dagonmomo.dagonindustry.core.util.maps.Recipes;
import net.dagonmomo.dagonindustry.core.util.registries.ModTileEntities;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import java.util.Arrays;
import java.util.List;

public class DieselGeneratorTileEntity extends AbstractMultiblockTileEntity
{
    public static final int SLOTS = 4;

    protected DieselGeneratorTileEntity(TileEntityType<?> typeIn)
    {
        super(typeIn);
    }

    public DieselGeneratorTileEntity()
    {
        super(TileEntityInit.DIESEL_GENERATOR.get());
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
        return new TranslationTextComponent("container.dagon_industry.diesel_generator");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player)
    {
        return new DieselGeneratorContainer(id, player, this);
    }

    @Override
    public int getSizeInventory()
    {
        return SLOTS;
    }

    @Override
    public List<BlockPos> getStructure(World world, Direction direction)
    {
        return Arrays.asList(
        );
    }

    @Override
    public void tick()
    {
        super.tick();
        List<ItemStack> batteries = Arrays.asList(getItemInSlot(0), getItemInSlot(1), getItemInSlot(2));

        if (world != null && !world.isRemote)
        {
            if (ticksExisted % 20 == 0)
            {
                if (this.getBurnTimeLeft() > 0)
                {
                    this.setBurnTimeLeft(this.getBurnTimeLeft() - 1);
                }

                if (batteries.stream().anyMatch(stack -> stack.getItem() instanceof BatteryItem
                && BatteryItem.getCharge(stack) < ((BatteryItem) stack.getItem()).getMaxCharge()))
                {
                    ItemStack fuelItem = this.getItemInSlot(3);

                    for (ItemStack battery : batteries)
                    {
                        BatteryItem.setCharge(battery, BatteryItem.getCharge(battery) + 3);
                    }

                    if (this.getBurnTimeLeft() <= 0 && Recipes.FUEL_BURN_TIMES.containsKey(fuelItem.getItem()))
                    {
                        setBurnTime(Recipes.FUEL_BURN_TIMES.get(fuelItem.getItem()));
                        this.setItemInSlot(3, fuelItem.getContainerItem());
                        this.setBurnTimeLeft(this.getBurnTime());
                    }
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

    public int getBurnTimeLeft()
    {
        return this.getTileData().getInt("burnLeft");
    }
    public void setBurnTimeLeft(int burnProgress)
    {
        this.getTileData().putInt("burnLeft", burnProgress);
    }

    public void setBurnTime(int ticks)
    {
        this.getTileData().putInt("burnTime", ticks);
    }

    public int getBurnTime()
    {
        return this.getTileData().getInt("burnTime");
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt)
    {
        super.read(state, nbt);
        this.setBurnTimeLeft(nbt.getInt("burnLeft"));
        this.setBurnTime(nbt.getInt("burnTime"));
        this.inventory = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(nbt, this.inventory);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound)
    {
        super.write(compound);
        compound.putInt("burnLeft", this.getBurnTimeLeft());
        compound.putInt("burnTime", this.getBurnTime());
        ItemStackHelper.saveAllItems(compound, inventory);
        return compound;
    }

    @Override
    public CompoundNBT getUpdateTag()
    {
        CompoundNBT nbt = super.getUpdateTag();
        return nbt.merge(this.write(new CompoundNBT()));
    }
}
