package io.github._4drian3d.vpacketevents.plugin;

import com.velocitypowered.api.event.AwaitingEventExecutor;
import com.velocitypowered.api.event.EventTask;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.proxy.connection.client.ConnectedPlayer;
import io.netty.channel.Channel;

import static io.github._4drian3d.vpacketevents.plugin.VPacketEvents.KEY;

final class DisconnectListener implements AwaitingEventExecutor<DisconnectEvent> {
    @Override
    public EventTask executeAsync(DisconnectEvent event) {
        return EventTask.async(() -> {
            if (event.getLoginStatus() != DisconnectEvent.LoginStatus.CONFLICTING_LOGIN) {
                removePlayer(event.getPlayer());
            }
        });
    }

    private void removePlayer(final Player player) {
        final ConnectedPlayer connectedPlayer = (ConnectedPlayer) player;
        final Channel channel = connectedPlayer.getConnection().getChannel();
        channel.eventLoop().submit(() -> {
            channel.pipeline().remove(KEY);
        });
    }
}
