package net.dagonmomo.dagonindustry.common.container;

import net.dagonmomo.dagonindustry.common.item.BatteryItem;
import net.dagonmomo.dagonindustry.common.te.ArcFurnaceTileEntity;
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

import java.util.Objects;

public class ArcFurnaceContainer extends AbstractMachineContainer
{
    public ArcFurnaceContainer(final int windowId, final PlayerInventory playerInv, final ArcFurnaceTileEntity te)
    {
        super(ContainerInit.ARC_FURNACE_CONTAINER.get(), windowId, te);

        // Battery Slot
        this.addSlot(new Slot(te, 0, 17, 32)
        {
            @Override
            public boolean isItemValid(ItemStack stack)
            {
                return stack.getItem() instanceof BatteryItem && ((BatteryItem) stack.getItem()).getPowerLevel() >= 1;
            }
        });

        // Input Slot 1
        this.addSlot(new Slot(te, 1, 55, 41)
        {
            @Override
            public boolean isItemValid(ItemStack stack)
            {
                return Recipes.ARC_ALLOYS.keySet().stream().anyMatch(pair -> pair.a.getItem() == stack.getItem());
            }
        });

        // Input Slot 2
        this.addSlot(new Slot(te, 2, 55, 21)
        {
            @Override
            public boolean isItemValid(ItemStack stack)
            {
                return Recipes.ARC_ALLOYS.keySet().stream().anyMatch(pair -> pair.b.getItem() == stack.getItem());
            }
        });

        // Output Slot
        this.addSlot(new Slot(te, 3, 107, 41)
        {
            @Override
            public boolean isItemValid(final ItemStack stack)
            {
                return false;
            }
        });

        // Slag Slot
        this.addSlot(new Slot(te, 4, 80, 62)
        {
            @Override
            public boolean isItemValid(final ItemStack stack)
            {
                return false;
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

    public ArcFurnaceContainer(final int windowId, final PlayerInventory playerInv, final PacketBuffer data)
    {
        this(windowId, playerInv, getTileEntity(playerInv, data));
    }

    private static ArcFurnaceTileEntity getTileEntity(final PlayerInventory playerInv, final PacketBuffer data)
    {
        Objects.requireNonNull(playerInv, "Player inventory cannot be null");
        Objects.requireNonNull(data, "PacketBuffer cannot be null");
        final TileEntity te = playerInv.player.world.getTileEntity(data.readBlockPos());
        if (te instanceof ArcFurnaceTileEntity)
        {
            return (ArcFurnaceTileEntity) te;
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

            if (DIMath.isBetween(index, 0, 4))
            {
                if (!this.mergeItemStack(itemstack1, 5, 41, false))
                {
                    return ItemStack.EMPTY;
                }
            }
            else
            {
                if (Recipes.ARC_ALLOYS.keySet().stream().anyMatch(triple ->
                                                triple.a.getItem() == itemstack1.getItem()
                                                || triple.b.getItem() == itemstack1.getItem()))
                {
                    if (!this.mergeItemStack(itemstack1, 1, 2, false))
                    {
                        slot.onSlotChange(itemstack1, itemstack);
                        return ItemStack.EMPTY;
                    }
                }
                else if (itemstack.getItem() instanceof BatteryItem)
                {
                    if (!this.mergeItemStack(itemstack1, 0, 1, false) || !this.mergeItemStack(itemstack1, 2, 3, false))
                    {
                        slot.onSlotChange(itemstack1, itemstack);
                        return ItemStack.EMPTY;
                    }
                }
                else if (DIMath.isBetween(index, 5, 31))
                {
                    if (!this.mergeItemStack(itemstack1, 32, 41, false))
                    {
                        slot.onSlotChange(itemstack1, itemstack);
                        return ItemStack.EMPTY;
                    }
                }
                else if (DIMath.isBetween(index, 32, 41))
                {
                    if (!this.mergeItemStack(itemstack1, 5, 31, false))
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
