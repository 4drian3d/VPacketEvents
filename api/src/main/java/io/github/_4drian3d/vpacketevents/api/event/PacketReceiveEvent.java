package io.github._4drian3d.vpacketevents.api.event;

import com.velocitypowered.api.event.ResultedEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.proxy.protocol.MinecraftPacket;
import org.jspecify.annotations.NullMarked;

import static java.util.Objects.requireNonNull;

/**
 * Event to execute when Velocity has received a registered packet
 */
@NullMarked
public final class PacketReceiveEvent
        extends PacketEvent
        implements ResultedEvent<ResultedEvent.GenericResult>
{
    private GenericResult result = GenericResult.allowed();

    /**
     * PacketReceiveEvent Constructor
     * @param packet the packet sent
     * @param player the player who sent the packet
     */
    public PacketReceiveEvent(final MinecraftPacket packet, final Player player) {
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
