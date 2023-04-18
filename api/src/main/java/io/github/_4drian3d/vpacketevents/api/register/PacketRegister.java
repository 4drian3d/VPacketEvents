package io.github._4drian3d.vpacketevents.api.register;

import com.velocitypowered.api.network.ProtocolVersion;
import com.velocitypowered.proxy.protocol.MinecraftPacket;
import com.velocitypowered.proxy.protocol.ProtocolUtils;
import com.velocitypowered.proxy.protocol.StateRegistry;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

// Exposed registration process and converted to a builder-style from
// https://github.com/PaperMC/Velocity/blob/e364e2c7d1918ec7c20986fb640f3f6a64127bb0/proxy/src/main/java/com/velocitypowered/proxy/protocol/StateRegistry.java

/**
 * Process of registering a packet in the internal Velocity StateRegistry's PacketRegistry
 * <pre>
 *   // UpdateTeamsPacket registration
 *   PacketRegister.{@literal <UpdateTeamsPacket>}start()
 *           .direction(ProtocolUtils.Direction.CLIENTBOUND)
 *           .packetSupplier(UpdateTeamsPacket::new)
 *           .stateRegistry(StateRegistry.PLAY)
 *           .packetClass(UpdateTeamsPacket.class)
 *           .mapping(0x47, MINECRAFT_1_13, false)
 *           .mapping(0x4B, MINECRAFT_1_14, false)
 *           .mapping(0x4C, MINECRAFT_1_15, false)
 *           .mapping(0x55, MINECRAFT_1_17, false)
 *           .mapping(0x58, MINECRAFT_1_19_1, false)
 *           .mapping(0x56, MINECRAFT_1_19_3, false)
 *           .mapping(0x5A, MINECRAFT_1_19_4, false)
 *           .register();
 * </pre>
 * @param <P> the packet class
 */
@SuppressWarnings("unused")
@DefaultQualifier(NonNull.class)
public final class PacketRegister<P extends MinecraftPacket> {

    private @MonotonicNonNull Class<@NonNull P> packetClass;
    private @MonotonicNonNull Supplier<@NonNull P> packetSupplier;
    private ProtocolUtils.@MonotonicNonNull Direction direction;
    private @MonotonicNonNull StateRegistry stateRegistry;
    private final List<StateRegistry.PacketMapping> mappings = new ArrayList<>();

    /**
     * Sets the class of this Packet registration
     *
     * @param packetClass this packet class
     * @return this packet registration
     */
    public PacketRegister<P> packetClass(final Class<P> packetClass) {
        requireNonNull(packetClass);
        this.packetClass = packetClass;
        return this;
    }

    /**
     * Sets the supplier of this Packet
     * <p>This supplier will be used to generate the new packets to serialize,
     * which you can use in PacketSentEvent and PacketReceiveEvent events</p>
     * <pre>
     *     PacketRegister.{@literal <SomeMinecraftPacket>}start()
     *         .packetSupplier(SomeMinecraftPacket::new);
     *     </pre>
     *
     * @param packetSupplier this packet supplier creator
     * @return this packet registration
     * @see io.github._4drian3d.vpacketevents.api.event.PacketReceiveEvent
     * @see io.github._4drian3d.vpacketevents.api.event.PacketSendEvent
     */
    public PacketRegister<P> packetSupplier(final Supplier<P> packetSupplier) {
        requireNonNull(packetSupplier);
        this.packetSupplier = packetSupplier;
        return this;
    }

    /**
     * Sets the packet direction of this Packet registration
     * <p>Direction Types: CLIENTBOUND, SERVERBOUND</p>
     *
     * @param direction the packet direction
     * @return this packet registration
     */
    public PacketRegister<P> direction(final ProtocolUtils.Direction direction) {
        requireNonNull(direction);
        this.direction = direction;
        return this;
    }

    /**
     * Sets the state registry of this Packet registration
     * <p>State types:
     *   <a href="https://wiki.vg/Protocol#Handshaking">HANDSHAKE</a>,
     *   <a href="https://wiki.vg/Protocol#Status">STATUS</a>,
     *   <a href="https://wiki.vg/Protocol#Login">LOGIN</a>
     *   <a href="https://wiki.vg/Protocol#Play">PLAY</a>
     * </p>
     *
     * @param stateRegistry the state registry
     * @return this packet registration
     */
    public PacketRegister<P> stateRegistry(final StateRegistry stateRegistry) {
        requireNonNull(stateRegistry);
        this.stateRegistry = stateRegistry;
        return this;
    }

    /**
     *
     * Adds a PacketMapping using the provided arguments.
     *
     * @param id                       Packet Id
     * @param version                  Protocol version
     * @param encodeOnly               When true packet decoding will be disabled
     * @param lastValidProtocolVersion Last version this Mapping is valid at
     * @return this packet registration
     */
    public PacketRegister<P> mapping(
            final int id,
            final ProtocolVersion version,
            final ProtocolVersion lastValidProtocolVersion,
            final boolean encodeOnly
    ) {
        requireNonNull(version);
        try {
            final StateRegistry.PacketMapping mapping = (StateRegistry.PacketMapping) PACKET_MAPPING$mapLast.invoke(
                    id, version, lastValidProtocolVersion, encodeOnly);
            this.mappings.add(mapping);
        } catch (Throwable t) {
            throw new RuntimeException("");
        }
        return this;
    }

    /**
     * Adds a PacketMapping using the provided arguments.
     *
     * @param id         Packet Id
     * @param version    Protocol version
     * @param encodeOnly When true packet decoding will be disabled
     * @return this packet registration
     */
    public PacketRegister<P> mapping(
            final int id,
            final ProtocolVersion version,
            final boolean encodeOnly
    ) {
        try {
            final StateRegistry.PacketMapping mapping = (StateRegistry.PacketMapping) PACKET_MAPPING$map.invoke(
                    id, version, encodeOnly);
            this.mappings.add(mapping);
        } catch (Throwable t) {
            throw new RuntimeException("");
        }
        return this;
    }

    /**
     * Registers the packet in the PacketRegistry of a supplied State
     *
     * @throws NullPointerException in case the packet class,
     * packet supplier, packet direction or state registry have not been assigned
     * @throws IllegalStateException if no mappings have been assigned to this packet
     * @see #packetClass(Class)
     * @see #direction(ProtocolUtils.Direction)
     * @see #stateRegistry(StateRegistry)
     * @see #packetSupplier(Supplier)
     * @see #mapping(int, ProtocolVersion, boolean)
     */
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

    /**
     * Initiates a new packet registration process
     *
     * @return a new Packet Registration of the type P
     * @param <P> the packet class to register
     */
    public static <P extends MinecraftPacket> PacketRegister<P> start() {
        return new PacketRegister<>();
    }

    private PacketRegister() {}

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
}
