package org.mod.industrialtech_ae.core.init;

import org.mod.industrialtech_ae.Industrialtech_ae;
import org.mod.industrialtech_ae.common.network.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class IAeModNetworking {
    private static SimpleChannel INSTANCE;
    private static int packetId = 0;

    private static int id() {
        return packetId++;
    }

    public static void init() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder.named(Industrialtech_ae.makeId("messages")).networkProtocolVersion(() -> "1.0").clientAcceptedVersions((s) -> true).serverAcceptedVersions((s) -> true).simpleChannel();
        INSTANCE = net;
        net.messageBuilder(FluidInteractPacket.class, id(), NetworkDirection.PLAY_TO_SERVER).decoder(FluidInteractPacket::decode).encoder(FluidInteractPacket::encode).consumerMainThread(FluidInteractPacket::handle).add();
        net.messageBuilder(FluidSyncPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT).decoder(FluidSyncPacket::decode).encoder(FluidSyncPacket::encode).consumerMainThread(FluidSyncPacket::handle).add();
        net.messageBuilder(FilterInteractPacket.class, id(), NetworkDirection.PLAY_TO_SERVER).decoder(FilterInteractPacket::decode).encoder(FilterInteractPacket::encode).consumerMainThread(FilterInteractPacket::handle).add();
        net.messageBuilder(ItemSyncPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT).decoder(ItemSyncPacket::decode).encoder(ItemSyncPacket::encode).consumerMainThread(ItemSyncPacket::handle).add();
        net.messageBuilder(ItemFilterInteractPacket.class, id(), NetworkDirection.PLAY_TO_SERVER).decoder(ItemFilterInteractPacket::decode).encoder(ItemFilterInteractPacket::encode).consumerMainThread(ItemFilterInteractPacket::handle).add();
        net.messageBuilder(ItemInteractPacket.class, id(), NetworkDirection.PLAY_TO_SERVER).decoder(ItemInteractPacket::decode).encoder(ItemInteractPacket::encode).consumerMainThread(ItemInteractPacket::handle).add();
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    public static SimpleChannel getChannel() {
        return INSTANCE;
    }
}
