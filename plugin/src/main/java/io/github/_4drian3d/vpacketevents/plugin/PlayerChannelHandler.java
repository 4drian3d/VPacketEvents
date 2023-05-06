package io.github._4drian3d.vpacketevents.plugin;

import com.velocitypowered.api.event.EventManager;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.proxy.protocol.MinecraftPacket;
import io.github._4drian3d.vpacketevents.api.event.PacketReceiveEvent;
import io.github._4drian3d.vpacketevents.api.event.PacketSendEvent;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

final class PlayerChannelHandler extends ChannelDuplexHandler {
    private final Player player;
    private final EventManager eventManager;
    private final Logger logger;

    PlayerChannelHandler(
            final Player player,
            final EventManager eventManager,
            final Logger logger
    ) {
        this.player = player;
        this.eventManager = eventManager;
        this.logger = logger;
    }

    @Override
    public void channelRead(
            final @NotNull ChannelHandlerContext ctx,
            final @NotNull Object packet
    ) throws Exception {
        if (!(packet instanceof final MinecraftPacket minecraftPacket)) {
            super.channelRead(ctx, packet);
            return;
        }

        final boolean allowed = eventManager.fire(new PacketReceiveEvent(minecraftPacket, player))
                .handle((event, ex) -> {
                    if (ex != null) {
                        logger.error("An error has occurred while reading packet {}", packet, ex);
                        return false;
                    } else {
                        return event.getResult().isAllowed();
                    }
                })
                .join();

        if (allowed) {
            super.channelRead(ctx, packet);
        }
    }

    @Override
    public void write(
            final ChannelHandlerContext ctx,
            final Object packet,
            final ChannelPromise promise
    ) throws Exception {
        if (!(packet instanceof final MinecraftPacket minecraftPacket)) {
            super.write(ctx, packet, promise);
            return;
        }

        final boolean allowed = eventManager.fire(new PacketSendEvent(minecraftPacket, player))
                .handle((event, ex) -> {
                    if (ex != null) {
                        logger.error("An error has occurred while sending packet {}", packet, ex);
                        return false;
                    } else {
                        return event.getResult().isAllowed();
                    }
                })
                .join();

        if (allowed) {
            super.write(ctx, packet, promise);
        }
    }
}
