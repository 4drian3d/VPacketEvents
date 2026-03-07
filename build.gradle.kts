subprojects {
    apply<JavaPlugin>()
    repositories {
        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://repo.opencollab.dev/maven-snapshots")
    }
    tasks {
        withType<JavaCompile> {
            options.encoding = Charsets.UTF_8.name()
            options.release.set(21)
        }
    }
    configure<JavaPluginExtension> {
        toolchain.languageVersion.set(JavaLanguageVersion.of(21))
    }
}