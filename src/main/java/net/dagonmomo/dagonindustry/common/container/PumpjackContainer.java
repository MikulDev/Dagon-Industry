package net.dagonmomo.dagonindustry.common.container;

import net.dagonmomo.dagonindustry.common.item.BatteryItem;
import net.dagonmomo.dagonindustry.common.te.PumpjackTileEntity;
import net.dagonmomo.dagonindustry.core.init.ContainerInit;
import net.dagonmomo.dagonindustry.core.util.DIMath;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.Tags;

import java.util.Objects;

public class PumpjackContainer extends AbstractMachineContainer
{

    public PumpjackContainer(final int windowId, final PlayerInventory playerInv, final PumpjackTileEntity te)
    {
        super(ContainerInit.PUMPJACK_CONTAINER.get(), windowId, te);

        // Battery Slot
        this.addSlot(new Slot(te, 0, 17, 41)
        {
            @Override
            public boolean isItemValid(ItemStack stack)
            {
                return stack.getItem() instanceof BatteryItem;
            }
        });

        // Bucket Slots
        for (int i = 0; i < 5; i++)
        {
            this.addSlot(new Slot(te, i + 1, 53 + i * 18, 41)
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
        }

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

    public PumpjackContainer(final int windowId, final PlayerInventory playerInv, final PacketBuffer data)
    {
        this(windowId, playerInv, getTileEntity(playerInv, data));
    }

    private static PumpjackTileEntity getTileEntity(final PlayerInventory playerInv, final PacketBuffer data)
    {
        Objects.requireNonNull(playerInv, "Player inventory cannot be null");
        Objects.requireNonNull(data, "PacketBuffer cannot be null");
        final TileEntity te = playerInv.player.world.getTileEntity(data.readBlockPos());
        if (te instanceof PumpjackTileEntity)
        {
            return (PumpjackTileEntity) te;
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

            if (DIMath.isBetween(index, 0, 5))
            {
                if (!this.mergeItemStack(itemstack1, 6, 33, false))
                {
                    return ItemStack.EMPTY;
                }
            }
            else
            {
                if (itemstack.getItem() instanceof BatteryItem)
                {
                    if (!this.mergeItemStack(itemstack1, 0, 1, false))
                    {
                        slot.onSlotChange(itemstack1, itemstack);
                        return ItemStack.EMPTY;
                    }
                }
                else if (itemstack.getItem() == Items.BUCKET)
                {
                    if (!this.mergeItemStack(itemstack1, 1, 6, false))
                    {
                        slot.onSlotChange(itemstack1, itemstack);
                        return ItemStack.EMPTY;
                    }
                }
                else if (DIMath.isBetween(index, 6, 32))
                {
                    if (!this.mergeItemStack(itemstack1, 33, 42, false))
                    {
                        slot.onSlotChange(itemstack1, itemstack);
                        return ItemStack.EMPTY;
                    }
                }
                else if (DIMath.isBetween(index, 33, 41))
                {
                    if (!this.mergeItemStack(itemstack1, 6, 32, false))
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
