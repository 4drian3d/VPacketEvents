package io.github._4drian3d.vpacketevents.plugin;

import com.google.inject.Inject;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.velocitypowered.api.event.*;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.proxy.connection.client.ConnectedPlayer;
import com.velocitypowered.proxy.network.Connections;
import io.netty.channel.Channel;
import org.slf4j.Logger;

@Plugin(
	id = "vpacketevents",
	name = "VPacketEvents",
	description = "VelocityPacketEvents",
	version = Constants.VERSION,
	authors = { "4drian3d" },
	dependencies = { @Dependency(id = "limboapi", optional = true) }
)
public final class VPacketEvents {
	private static final String KEY = "vpacketevents";
	@Inject
	private EventManager eventManager;
	@Inject
	private Logger logger;

	@Subscribe
	void onJoin(final PostLoginEvent event, final Continuation continuation) {
		injectPlayer(event.getPlayer());
		continuation.resume();
	}

	@Subscribe(order = PostOrder.LAST)
	EventTask onDisconnect(final DisconnectEvent event) {
		if (event.getLoginStatus() == DisconnectEvent.LoginStatus.CONFLICTING_LOGIN) {
			return null;
		}
		return EventTask.async(() -> removePlayer(event.getPlayer()));
	}

	private void injectPlayer(final Player player) {
		final ConnectedPlayer p = (ConnectedPlayer) player;
		p.getConnection()
				.getChannel()
				.pipeline()
				.addBefore(Connections.HANDLER, KEY, new PlayerChannelHandler(player, eventManager, logger));
	}

	private void removePlayer(final Player player) {
		final ConnectedPlayer p = (ConnectedPlayer) player;
		final Channel channel = p.getConnection().getChannel();
		channel.eventLoop().submit(() -> {
			channel.pipeline().remove(KEY);
		});

		LiteralArgumentBuilder.literal("a")
				.requiresWithContext((a, b) -> true);
	}
}