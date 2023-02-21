package io.github._4drian3d.vpacketevents.api.event;


import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.proxy.protocol.MinecraftPacket;

public abstract class PacketEvent {
    private final MinecraftPacket packet;
    private final Player player;

    protected PacketEvent(MinecraftPacket packet, Player player) {
        this.packet = packet;
        this.player = player;
    }

    public MinecraftPacket getPacket() {
        return this.packet;
    }

    public Player getPlayer() {
        return this.player;
    }
}
