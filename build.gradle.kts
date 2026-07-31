subprojects {
    pluginManager.apply("java")
    repositories {
        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://maven.elytrium.net/repo/")
    }
    tasks {
        withType<JavaCompile> {
            options.encoding = Charsets.UTF_8.name()
            options.release.set(25)
        }
    }
    configure<JavaPluginExtension> {
        toolchain.languageVersion.set(JavaLanguageVersion.of(25))
    }
}