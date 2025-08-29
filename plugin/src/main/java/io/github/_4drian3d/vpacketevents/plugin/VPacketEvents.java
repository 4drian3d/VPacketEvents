package io.github._4drian3d.vpacketevents.plugin;

import com.google.inject.Inject;
import com.velocitypowered.api.event.*;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.proxy.connection.client.ConnectedPlayer;
import com.velocitypowered.proxy.network.Connections;
import io.netty.channel.Channel;
import org.slf4j.Logger;

@SuppressWarnings("ClassCanBeRecord")
@Plugin(
        id = "vpacketevents",
        name = "VPacketEvents",
        description = "VelocityPacketEvents",
        version = Constants.VERSION,
        authors = {"4drian3d"},
        // LimboAPI replaces the Velocity EventManager instance and makes the original instance unusable,
        // so VPacketEvents is required to be initialized after LimboAPI
        dependencies = {@Dependency(id = "limboapi", optional = true)}
)
public final class VPacketEvents {
    private static final String KEY = "vpacketevents";
    private final EventManager eventManager;
    private final Logger logger;

    @Inject
    public VPacketEvents(final EventManager eventManager, final Logger logger) {
        this.logger = logger;
        this.eventManager = eventManager;
    }

    @Subscribe
    public void onProxyInitialization(final ProxyInitializeEvent event) {
		this.eventManager.register(this, PostLoginEvent.class,
				(AwaitingEventExecutor<PostLoginEvent>) postLoginEvent -> EventTask.withContinuation(continuation -> {
					injectPlayer(postLoginEvent.getPlayer());
					continuation.resume();
				}));
        this.eventManager.register(this, DisconnectEvent.class, PostOrder.LAST,
                (AwaitingEventExecutor<DisconnectEvent>) disconnectEvent ->
					disconnectEvent.getLoginStatus() == DisconnectEvent.LoginStatus.CONFLICTING_LOGIN
							? null
							: EventTask.async(() -> removePlayer(disconnectEvent.getPlayer()))
		);
    }

    private void injectPlayer(final Player player) {
        final ConnectedPlayer connectedPlayer = (ConnectedPlayer) player;
		connectedPlayer.getConnection()
                .getChannel()
                .pipeline()
                .addBefore(Connections.HANDLER, KEY, new PlayerChannelHandler(player, eventManager, logger));
    }

    private void removePlayer(final Player player) {
        final ConnectedPlayer connectedPlayer = (ConnectedPlayer) player;
        final Channel channel = connectedPlayer.getConnection().getChannel();
        channel.eventLoop().submit(() -> {
            channel.pipeline().remove(KEY);
        });
    }
}