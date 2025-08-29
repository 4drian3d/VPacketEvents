package io.github._4drian3d.vpacketevents.api.event;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.proxy.protocol.MinecraftPacket;
import org.jetbrains.annotations.NotNull;

sealed abstract class PacketEvent permits PacketReceiveEvent, PacketSendEvent {
    private final MinecraftPacket packet;
    private final Player player;

    protected PacketEvent(final MinecraftPacket packet, final Player player) {
        this.packet = packet;
        this.player = player;
    }

    /**
     * Get the packet involved in this event
     * @return a packet
     */
    public @NotNull MinecraftPacket getPacket() {
        return this.packet;
    }

    /**
     * Get the player involved in this event
     * @return the player
     */
    public @NotNull Player getPlayer() {
        return this.player;
    }
}
