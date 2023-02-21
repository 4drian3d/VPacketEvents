package io.github._4drian3d.vpacketevents.api.event;

import com.velocitypowered.api.event.ResultedEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.proxy.protocol.MinecraftPacket;

import static java.util.Objects.requireNonNull;

public class PacketSendEvent
        extends PacketEvent
        implements ResultedEvent<ResultedEvent.GenericResult>
{
    private GenericResult result = GenericResult.allowed();

    public PacketSendEvent(MinecraftPacket packet, Player player) {
        super(packet, player);
    }

    @Override
    public GenericResult getResult() {
        return result;
    }

    @Override
    public void setResult(GenericResult result) {
        this.result = requireNonNull(result);
    }
}
