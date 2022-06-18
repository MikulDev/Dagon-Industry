package net.dagonmomo.dagonindustry.common.container;

import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.tileentity.LockableTileEntity;

public abstract class AbstractMachineContainer extends Container
{
    public final LockableTileEntity te;

    public AbstractMachineContainer(ContainerType<?> containerType, final int windowId, final LockableTileEntity te)
    {
        super(containerType, windowId);
        this.te = te;
    }
}
