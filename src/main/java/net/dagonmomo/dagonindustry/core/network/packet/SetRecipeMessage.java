package net.dagonmomo.dagonindustry.core.network.packet;

import net.dagonmomo.dagonindustry.common.te.WorkbenchTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.loading.FMLCommonLaunchHandler;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SetRecipeMessage
{
    BlockPos blockPos;
    int index;

    public SetRecipeMessage(BlockPos blockPos, int idnex) {
        this.blockPos = blockPos;
        this.index = idnex;
    }

    public static void encode(SetRecipeMessage message, PacketBuffer buffer)
    {
        buffer.writeBlockPos(message.blockPos);
        buffer.writeInt(message.index);
    }

    public static SetRecipeMessage decode(PacketBuffer buffer)
    {
        return new SetRecipeMessage(buffer.readBlockPos(), buffer.readInt());
    }

    public static void handle(SetRecipeMessage message, Supplier<NetworkEvent.Context> contextSupplier)
    {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isServer())
        {
            context.enqueueWork(() ->
            {
                if (context.getSender() != null)
                {
                    TileEntity te = context.getSender().world.getTileEntity(message.blockPos);
                    if (te instanceof WorkbenchTileEntity)
                    {
                        // Set the recipe to the one specified and reset the progress to prevent cheesing
                        ((WorkbenchTileEntity) te).setRecipeIndex(message.index);
                        ((WorkbenchTileEntity) te).setProgress(0);
                    }
                }
            });
        }
        context.setPacketHandled(true);
    }
}
