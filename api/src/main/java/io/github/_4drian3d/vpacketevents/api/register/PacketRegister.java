package io.github._4drian3d.vpacketevents.api.register;

import com.velocitypowered.api.network.ProtocolVersion;
import com.velocitypowered.proxy.protocol.MinecraftPacket;
import com.velocitypowered.proxy.protocol.ProtocolUtils;
import com.velocitypowered.proxy.protocol.StateRegistry;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

// Exposed registration process and converted to a builder-style from
// https://github.com/PaperMC/Velocity/blob/e364e2c7d1918ec7c20986fb640f3f6a64127bb0/proxy/src/main/java/com/velocitypowered/proxy/protocol/StateRegistry.java#L441
@SuppressWarnings("unused")
public final class PacketRegister<P extends MinecraftPacket> {
    private static final MethodHandle STATE_REGISTRY$clientBound;
    private static final MethodHandle STATE_REGISTRY$serverBound;
    private static final MethodHandle PACKET_REGISTRY$register;
    private static final MethodHandle PACKET_MAPPING$map;
    private static final MethodHandle PACKET_MAPPING$mapLast;

    static {
        final MethodHandles.Lookup lookup = MethodHandles.lookup();
        try {
            final MethodHandles.Lookup stateRegistryLookup = MethodHandles.privateLookupIn(StateRegistry.class, lookup);
            STATE_REGISTRY$clientBound = stateRegistryLookup.findGetter(StateRegistry.class, "clientbound", StateRegistry.PacketRegistry.class);
            STATE_REGISTRY$serverBound = stateRegistryLookup.findGetter(StateRegistry.class, "serverbound", StateRegistry.PacketRegistry.class);

            final MethodType mapType = MethodType.methodType(StateRegistry.PacketMapping.class, Integer.TYPE, ProtocolVersion.class, Boolean.TYPE);
            PACKET_MAPPING$map = stateRegistryLookup.findStatic(StateRegistry.class, "map", mapType);
            final MethodType mapLastType = MethodType.methodType(StateRegistry.PacketMapping.class, Integer.TYPE, ProtocolVersion.class, ProtocolVersion.class, Boolean.TYPE);
            PACKET_MAPPING$mapLast = stateRegistryLookup.findStatic(StateRegistry.class, "map", mapLastType);

            final MethodHandles.Lookup packetRegistryLookup = MethodHandles.privateLookupIn(StateRegistry.PacketRegistry.class, lookup);
            final MethodType registerType = MethodType.methodType(void.class, Class.class, Supplier.class, StateRegistry.PacketMapping[].class);
            PACKET_REGISTRY$register = packetRegistryLookup.findVirtual(StateRegistry.PacketRegistry.class, "register", registerType);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private PacketRegister() {}

    public static <P extends MinecraftPacket> PacketRegister<P> start() {
        return new PacketRegister<>();
    }

    private Class<P> packetClass;
    private Supplier<P> packetSupplier;
    private ProtocolUtils.Direction direction;
    private StateRegistry stateRegistry;
    private final List<StateRegistry.PacketMapping> mappings = new ArrayList<>();

    public PacketRegister<P> clazz(Class<P> packetClass) {
        this.packetClass = packetClass;
        return this;
    }

    public PacketRegister<P> packetSupplier(Supplier<P> packetSupplier) {
        this.packetSupplier = packetSupplier;
        return this;
    }

    public PacketRegister<P> direction(ProtocolUtils.Direction direction) {
        this.direction = direction;
        return this;
    }

    public PacketRegister<P> stateRegistry(StateRegistry stateRegistry) {
        this.stateRegistry = stateRegistry;
        return this;
    }

    public PacketRegister<P> mapping(int id, ProtocolVersion version, ProtocolVersion lastValidProtocolVersion, boolean encodeOnly) {
        try {
            final StateRegistry.PacketMapping mapping = (StateRegistry.PacketMapping) PACKET_MAPPING$mapLast.invoke(
                    id, version, lastValidProtocolVersion, encodeOnly);
            this.mappings.add(mapping);
        } catch (Throwable t) {
            throw new RuntimeException("");
        }
        return this;
    }

    public PacketRegister<P> mapping(int id, ProtocolVersion version, boolean encodeOnly) {
        try {
            final StateRegistry.PacketMapping mapping = (StateRegistry.PacketMapping) PACKET_MAPPING$map.invoke(
                    id, version, encodeOnly);
            this.mappings.add(mapping);
        } catch (Throwable t) {
            throw new RuntimeException("");
        }
        return this;
    }

    public void register() {
        requireNonNull(this.packetClass);
        requireNonNull(this.direction);
        requireNonNull(this.packetSupplier);
        requireNonNull(this.stateRegistry);
        if (this.mappings.isEmpty()) {
            throw new IllegalStateException("Empty mappings");
        }
        try {
            final StateRegistry.PacketRegistry packetRegistry = direction == ProtocolUtils.Direction.CLIENTBOUND
                    ? (StateRegistry.PacketRegistry) STATE_REGISTRY$clientBound.invoke(stateRegistry)
                    : (StateRegistry.PacketRegistry) STATE_REGISTRY$serverBound.invoke(stateRegistry);
            PACKET_REGISTRY$register.invoke(
                    packetRegistry,
                    packetClass,
                    packetSupplier,
                    mappings.toArray(StateRegistry.PacketMapping[]::new)
            );
        } catch (Throwable t) {
            throw new RuntimeException("");
        }
    }
}
