package io.github._4drian3d.vpacketevents.plugin;

import com.google.inject.Inject;
import com.velocitypowered.api.event.EventManager;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
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
    public static final String KEY = "vpacketevents";
    private final EventManager eventManager;
    private final Logger logger;

    @Inject
    public VPacketEvents(final EventManager eventManager, final Logger logger) {
        this.logger = logger;
        this.eventManager = eventManager;
    }

    @Subscribe
    public void onProxyInitialization(final ProxyInitializeEvent event) {
        this.eventManager.register(this, PostLoginEvent.class, new LoginListener(eventManager, logger));
        this.eventManager.register(this, DisconnectEvent.class, PostOrder.LAST, new DisconnectListener());
    }
}