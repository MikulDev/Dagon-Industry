package net.dagonmomo.dagonindustry.core.network;

import net.dagonmomo.dagonindustry.DagonIndustry;
import net.dagonmomo.dagonindustry.core.network.packet.BlockDataUpdateMessage;
import net.dagonmomo.dagonindustry.core.network.packet.SetRecipeMessage;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class PacketHandler
{
    private static final String PROTOCOL_VERSION = "1.0";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(DagonIndustry.MOD_ID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void init()
    {
        INSTANCE.registerMessage(0, BlockDataUpdateMessage.class, BlockDataUpdateMessage::encode, BlockDataUpdateMessage::decode, BlockDataUpdateMessage::handle);
        INSTANCE.registerMessage(1, SetRecipeMessage.class, SetRecipeMessage::encode, SetRecipeMessage::decode, SetRecipeMessage::handle);
    }
}
