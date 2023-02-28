enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "VPacketEvents"

arrayOf("api", "plugin").forEach {
    include("vpacketevents-$it")
    project(":vpacketevents-$it").projectDir = file(it)
}