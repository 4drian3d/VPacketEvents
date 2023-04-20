# VPacketEvents

[![Discord](https://img.shields.io/discord/899740810956910683?color=7289da&label=Discord)](https://discord.gg/5NMMzK5mAn) ![](https://img.shields.io/maven-central/v/io.github.4drian3d/vpacketevents-api?style=flat-square)

Manage and register packets through Velocity's native events

```java

class PacketListener {
    @Subscribe
    public void onPacketReceive(PacketReceiveEvent event) {
        final MinecraftPacket packet = event.getPacket();
        if (packet instanceof KeyedPlayerCommand commandPacket) {
            final String commandExecuted = commandPacket.getCommand();
            event.setResult(GenericResult.denied());
        }
    } 
    
    @Subscribe
    public void onPacketSend(PacketSendEvent event) {
        final MinecraftPacket packet = event.getPacket();
        if (packet instanceof ServerData serverData) {
            // some stuff
            final boolean isSecureChatEnforced = serverData.isSecureChatEnforced();
        }
    }
    
    public void registerMyPacket() {
        // UpdateTeamsPacket registration
        PacketRegistration.of(UpdateTeamsPacket.class)
                .direction(Direction.CLIENTBOUND)
                .packetSupplier(UpdateTeamsPacket::new)
                .stateRegistry(StateRegistry.PLAY)
                .mapping(0x47, MINECRAFT_1_13, false)
                .mapping(0x4B, MINECRAFT_1_14, false)
                .mapping(0x4C, MINECRAFT_1_15, false)
                .mapping(0x55, MINECRAFT_1_17, false)
                .mapping(0x58, MINECRAFT_1_19_1, false)
                .mapping(0x56, MINECRAFT_1_19_3, false)
                .mapping(0x5A, MINECRAFT_1_19_4, false)
                .register();
    }
}

```


## Installation
- Download VPacketEvents from Modrinth
- Drag and drop on your plugins folder
- Start the server


## Dev Setup

### Gradle

```kotlin

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("io.github.4drian3d:vpacketevents-api:1.1.0")
}
```


## Javadocs
https://javadoc.io/doc/io.github.4drian3d/vpacketevents-api
