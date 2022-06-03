package net.dagonmomo.dagonindustry.common.container;

import net.dagonmomo.dagonindustry.common.item.BatteryItem;
import net.dagonmomo.dagonindustry.common.te.RefineryTileEntity;
import net.dagonmomo.dagonindustry.core.init.ContainerInit;
import net.dagonmomo.dagonindustry.core.util.DIMath;
import net.dagonmomo.dagonindustry.core.util.maps.Recipes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.Tags;

import java.util.Objects;

public class RefineryContainer extends AbstractMachineContainer
{
    public RefineryContainer(final int windowId, final PlayerInventory playerInv, final RefineryTileEntity te)
    {
        super(ContainerInit.REFINERY_CONTAINER.get(), windowId, te);

        // Battery Slot
        this.addSlot(new Slot(te, 0, 17, 41)
        {
            @Override
            public boolean isItemValid(ItemStack stack)
            {
                return stack.getItem() instanceof BatteryItem;
            }
        });

        // Input Slot
        this.addSlot(new Slot(te, 1, 80, 61)
        {
            @Override
            public boolean isItemValid(ItemStack stack)
            {
                return Recipes.REFINING.containsKey(stack.getItem());
            }
        });

        // Output Slots
        for (int i = 0; i < 3; i++)
        {
            this.addSlot(new Slot(te, i + 2, 62 + i * 18, 32)
            {
                @Override
                public boolean isItemValid(final ItemStack stack)
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

        // Slag slot
        this.addSlot(new Slot(te, 5, 116, 61));

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

    public RefineryContainer(final int windowId, final PlayerInventory playerInv, final PacketBuffer data)
    {
        this(windowId, playerInv, getTileEntity(playerInv, data));
    }

    private static RefineryTileEntity getTileEntity(final PlayerInventory playerInv, final PacketBuffer data)
    {
        Objects.requireNonNull(playerInv, "Player inventory cannot be null");
        Objects.requireNonNull(data, "PacketBuffer cannot be null");
        final TileEntity te = playerInv.player.world.getTileEntity(data.readBlockPos());
        if (te instanceof RefineryTileEntity)
        {
            return (RefineryTileEntity) te;
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

            if (DIMath.isBetween(index, 0, 2))
            {
                if (!this.mergeItemStack(itemstack1, 3, 39, false))
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
                else if (Recipes.REFINING.keySet().stream().anyMatch(item -> item == itemstack1.getItem()))
                {
                    if (!this.mergeItemStack(itemstack1, 1, 2, false))
                    {
                        slot.onSlotChange(itemstack1, itemstack);
                        return ItemStack.EMPTY;
                    }
                }
                else if (itemstack1.getItem() == Items.BUCKET)
                {
                    if (!this.mergeItemStack(itemstack1, 2, 5, false))
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
                    if (!this.mergeItemStack(itemstack1, 6, 33, false))
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
