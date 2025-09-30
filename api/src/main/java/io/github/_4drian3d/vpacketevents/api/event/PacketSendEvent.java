package io.github._4drian3d.vpacketevents.api.event;

import com.velocitypowered.api.event.ResultedEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.proxy.protocol.MinecraftPacket;
import org.jspecify.annotations.NullMarked;

import static java.util.Objects.requireNonNull;

/**
 * Event to execute when a packet is sent to a player
 */
@NullMarked
public final class PacketSendEvent
        extends PacketEvent
        implements ResultedEvent<ResultedEvent.GenericResult>
{
    private GenericResult result = GenericResult.allowed();

    /**
     * PacketSend Event Constructor
     * @param packet the packet to be sent to the player
     * @param player the player who will receive the packet
     */
    public PacketSendEvent(final MinecraftPacket packet, final Player player) {
        super(requireNonNull(packet), requireNonNull(player));
    }

    @Override
    public GenericResult getResult() {
        return result;
    }

    @Override
    public void setResult(final GenericResult result) {
        this.result = requireNonNull(result);
    }
}
