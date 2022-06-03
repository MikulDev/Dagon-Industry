package net.dagonmomo.dagonindustry.common.te;

import net.dagonmomo.dagonindustry.common.container.BioRefineryContainer;
import net.dagonmomo.dagonindustry.common.item.BatteryItem;
import net.dagonmomo.dagonindustry.core.init.ItemInit;
import net.dagonmomo.dagonindustry.core.util.registries.ModItems;
import net.dagonmomo.dagonindustry.core.util.registries.ModTileEntities;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.*;
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

public class BioRefineryTileEntity extends AbstractMultiblockTileEntity
{
    public static final int SLOTS = 7;
    public static int MAX_PROGRESS = 120;
    static int BIOMASS_NEEDED = 8;

    protected BioRefineryTileEntity(TileEntityType<?> typeIn)
    {
        super(typeIn);
    }

    public BioRefineryTileEntity()
    {
        super(ModTileEntities.BIO_REFINERY);
    }

    @Override
    public List<BlockPos> getStructure(World world, Direction direction)
    {
        return Arrays.asList(
                new BlockPos(0, 1, 0),
                new BlockPos(0, -1, 0)
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
        return new TranslationTextComponent("container.dagon_industry.bio_refinery");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player)
    {
        return new BioRefineryContainer(id, player, this);
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

        if (world != null && !world.isRemote)
        {
            if (this.ticksExisted % 20 == 0)
            {
                ItemStack battery = this.getItemInSlot(0);
                if (this.hasBiomass() && battery.getItem() instanceof BatteryItem && BatteryItem.getCharge(battery) > 0)
                {
                    if (this.getProgress() < MAX_PROGRESS)
                    {
                        this.getTileData().putBoolean("active", true);
                        this.setProgress(this.getProgress() + 1);

                        BatteryItem.setCharge(battery, BatteryItem.getCharge(battery) - 1);
                    }
                    else
                    {
                        this.getTileData().putBoolean("active", false);
                    }

                    if (this.getProgress() >= MAX_PROGRESS && this.getItemInSlot(2).getItem() == Items.BUCKET)
                    {
                        // Produce biodiesel/biogas
                        if (Math.random() < 0.2)
                            this.setItemInSlot(2, new ItemStack(ItemInit.BIOGAS_BUCKET.get()));
                        else
                            this.setItemInSlot(2, new ItemStack(ItemInit.BIODIESEL_BUCKET.get()));

                        // Drain biomass
                        ItemStack biomass = inventory.get(1);
                        if (biomass.getCount() >= BIOMASS_NEEDED)
                        {
                            biomass.shrink(BIOMASS_NEEDED);
                        }

                        this.setProgress(0);
                    }
                }
                else
                {
                    this.getTileData().putBoolean("active", false);
                }
            }
        }
    }

    public boolean hasBiomass()
    {
        int itemsNeeded = BIOMASS_NEEDED;
        for (ItemStack itemStack : this.getItems().stream().limit(5).toArray(ItemStack[]::new))
        {
            if (itemStack.getItem() == ModItems.BIOMASS)
            {
                itemsNeeded -= itemStack.getCount();
                if (itemsNeeded <= 0) return true;
            }
        }
        return false;
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

    @Override
    public CompoundNBT getUpdateTag()
    {
        CompoundNBT nbt = super.getUpdateTag();
        nbt.putBoolean("active", this.getTileData().getBoolean("active"));
        return nbt;
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag)
    {
        this.getTileData().putBoolean("active", tag.getBoolean("active"));
        super.handleUpdateTag(state, tag);
    }

    @Override
    public void remove()
    {
        // drop inventory
        for (int i = 0; i < this.getSizeInventory(); ++i)
        {
            ItemStack itemstack = this.getStackInSlot(i);

            if (!itemstack.isEmpty())
            {
                this.world.addEntity(new ItemEntity(world, this.pos.getX(), this.pos.getY(), this.pos.getZ(), itemstack));
            }
        }
        super.remove();
    }
}
