enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
enableFeaturePreview("VERSION_CATALOGS")

rootProject.name = "VPacketEvents"

arrayOf("api", "plugin").forEach {
    include("vpacketevents-$it")
    project(":vpacketevents-$it").projectDir = file(it)
}