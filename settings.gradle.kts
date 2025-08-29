enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "VPacketEvents"

arrayOf("api", "plugin").forEach {
    include("vpacketevents-$it")
    project(":vpacketevents-$it").projectDir = file(it)
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}