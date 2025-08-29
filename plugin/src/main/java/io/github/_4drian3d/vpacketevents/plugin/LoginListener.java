package io.github._4drian3d.vpacketevents.plugin;

import com.velocitypowered.api.event.AwaitingEventExecutor;
import com.velocitypowered.api.event.EventManager;
import com.velocitypowered.api.event.EventTask;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.proxy.connection.client.ConnectedPlayer;
import com.velocitypowered.proxy.network.Connections;
import org.slf4j.Logger;

import static io.github._4drian3d.vpacketevents.plugin.VPacketEvents.KEY;

record LoginListener(EventManager eventManager, Logger logger) implements AwaitingEventExecutor<PostLoginEvent> {
    @Override
    public EventTask executeAsync(PostLoginEvent event) {
        return EventTask.withContinuation(continuation -> {
            injectPlayer(event.getPlayer());
            continuation.resume();
        });
    }

    private void injectPlayer(final Player player) {
        final ConnectedPlayer connectedPlayer = (ConnectedPlayer) player;
        connectedPlayer.getConnection()
                .getChannel()
                .pipeline()
                .addBefore(Connections.HANDLER, KEY, new PlayerChannelHandler(player, eventManager, logger));
    }
}
