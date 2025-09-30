package io.github._4drian3d.vpacketevents.plugin;

import com.google.inject.Inject;
import com.velocitypowered.api.event.EventManager;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;

@SuppressWarnings("ClassCanBeRecord")
@Plugin(
    id = "vpacketevents",
    name = "VPacketEvents",
    description = "Manage packets through Velocity native events",
    version = Constants.VERSION,
    authors = {"4drian3d"}
)
public final class VPacketEvents {
  public static final String KEY = "vpacketevents";
  private final ProxyServer proxyServer;
  private final Logger logger;

  @Inject
  public VPacketEvents(final ProxyServer proxyServer, final Logger logger) {
    this.logger = logger;
    this.proxyServer = proxyServer;
  }

  @Subscribe
  public void onProxyInitialization(final ProxyInitializeEvent event) {
    final EventManager eventManager = proxyServer.getEventManager();
    eventManager.register(this, LoginEvent.class, new LoginListener(eventManager, logger));
    eventManager.register(this, DisconnectEvent.class, (short) -404, new DisconnectListener());
  }
}