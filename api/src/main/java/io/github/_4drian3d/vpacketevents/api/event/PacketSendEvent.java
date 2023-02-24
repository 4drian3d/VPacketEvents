package io.github._4drian3d.vpacketevents.api.event;

import com.velocitypowered.api.event.ResultedEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.proxy.protocol.MinecraftPacket;
import org.jetbrains.annotations.NotNull;

import static java.util.Objects.requireNonNull;

/**
 * Event to execute when a packet is sent to a player
 */
public final class PacketSendEvent
        extends PacketEvent
        implements ResultedEvent<ResultedEvent.GenericResult>
{
    private @NotNull GenericResult result = GenericResult.allowed();

    public PacketSendEvent(final @NotNull MinecraftPacket packet, final @NotNull Player player) {
        super(requireNonNull(packet), requireNonNull(player));
    }

    @Override
    @NotNull
    public GenericResult getResult() {
        return result;
    }

    @Override
    public void setResult(final @NotNull GenericResult result) {
        this.result = requireNonNull(result);
    }
}
