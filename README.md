# VPacketEvents

[![Discord](https://img.shields.io/discord/899740810956910683?color=7289da&label=Discord)](https://discord.gg/5NMMzK5mAn) ![](https://img.shields.io/maven-central/v/io.github.4drian3d/vpacketevents-api?style=flat-square)

Manage packets through Velocity's native events

```java

class PacketListener {
    @Subscribe
    public void onPacketReceive(PacketReceiveEvent event) {
        final MinecraftPacket packet = event.getPacket();
        if (packet instanceof KeyedPlayerCommand commandPacket) {
           event.setResult(GenericResult.denied());
        }
    } 
    
    @Subscribe
    public void onPacketSend(PacketSendEvent event) {
        // some stuff
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
    compileOnly("io.github.4drian3d:vpacketevents:1.0.0")
}
```


## Javadocs
https://javadoc.io/doc/io.github.4drian3d/vpacketevents-api
