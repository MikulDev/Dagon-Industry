package net.dagonmomo.dagonindustry.common.container;

import net.dagonmomo.dagonindustry.common.item.BatteryItem;
import net.dagonmomo.dagonindustry.common.te.BioRefineryTileEntity;
import net.dagonmomo.dagonindustry.core.init.ContainerInit;
import net.dagonmomo.dagonindustry.core.util.DIMath;
import net.dagonmomo.dagonindustry.core.util.registries.ModItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.tileentity.TileEntity;

import java.util.Objects;

public class BioRefineryContainer extends AbstractMachineContainer
{

    public BioRefineryContainer(final int windowId, final PlayerInventory playerInv, final BioRefineryTileEntity te)
    {
        super(ContainerInit.BIO_REFINERY_CONTAINER.get(), windowId, te);

        // Biomass Slots
        for (int i = 0; i < 5; i++)
        {
            this.addSlot(new Slot(te, i, 44 + i * 18, 28)
            {
                @Override
                public boolean isItemValid(ItemStack stack)
                {
                    return stack.getItem() == ModItems.BIOMASS;
                }
            });
        }

        // Bucket Slot
        this.addSlot(new Slot(te, 5, 80, 62)
        {
            @Override
            public boolean isItemValid(ItemStack stack)
            {
                return stack.getItem() == Items.BUCKET;
            }

            @Override
            public int getSlotStackLimit()
            {
                return 1;
            }
        });

        // Battery Slot
        this.addSlot(new Slot(te, 6, 14, 28)
        {
            @Override
            public boolean isItemValid(ItemStack stack)
            {
                return stack.getItem() instanceof BatteryItem;
            }
        });

        int xStart = 8;
        int yStart = 84;

        // Main player inventory
        for (int row = 0; row < 3; row++)
        {
            for (int col = 0; col < 9; col++)
            {
                addSlot(new Slot(playerInv, col + (9 * row) + 9, xStart + col * 18, yStart + (row * 18)));
            }
        }

        // Player Hotbar
        for (int col = 0; col < 9; col++)
        {
            addSlot(new Slot(playerInv, col, xStart + col * 18, yStart + 58));
        }
    }

    public BioRefineryContainer(final int windowId, final PlayerInventory playerInv, final PacketBuffer data)
    {
        this(windowId, playerInv, getTileEntity(playerInv, data));
    }

    private static BioRefineryTileEntity getTileEntity(final PlayerInventory playerInv, final PacketBuffer data)
    {
        Objects.requireNonNull(playerInv, "Player inventory cannot be null");
        Objects.requireNonNull(data, "PacketBuffer cannot be null");
        final TileEntity te = playerInv.player.world.getTileEntity(data.readBlockPos());
        if (te instanceof BioRefineryTileEntity)
        {
            return (BioRefineryTileEntity) te;
        }
        throw new IllegalStateException("Tile Entity is not correct");
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn)
    {
        return playerIn.getDistanceSq(te.getPos().getX() + 0.5D, te.getPos().getY() + 0.5D, te.getPos().getZ() + 0.5D) <= 64.0D;
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (DIMath.isBetween(index, 0, 3))
            {
                if (!this.mergeItemStack(itemstack1, 4, 40, true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else
            {
                if (itemstack.getItem() instanceof BatteryItem)
                {
                    if (!this.mergeItemStack(itemstack1, 0, 3, false))
                    {
                        slot.onSlotChange(itemstack1, itemstack);
                        return ItemStack.EMPTY;
                    }
                }
                else if (AbstractFurnaceTileEntity.isFuel(itemstack))
                {
                    if (!this.mergeItemStack(itemstack1, 3, 4, false))
                    {
                        slot.onSlotChange(itemstack1, itemstack);
                        return ItemStack.EMPTY;
                    }
                }
                else if (DIMath.isBetween(index, 4, 30))
                {
                    if (!this.mergeItemStack(itemstack1, 31, 40, false))
                    {
                        slot.onSlotChange(itemstack1, itemstack);
                        return ItemStack.EMPTY;
                    }
                }
                else if (DIMath.isBetween(index, 31, 40))
                {
                    if (!this.mergeItemStack(itemstack1, 4, 30, false))
                    {
                        slot.onSlotChange(itemstack1, itemstack);
                        return ItemStack.EMPTY;
                    }
                }

                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty())
            {
                slot.putStack(ItemStack.EMPTY);
            }
            else
            {
                slot.onSlotChanged();
            }

            slot.onTake(playerIn, itemstack1);
        }
        return itemstack;
    }
}
