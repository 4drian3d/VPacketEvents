subprojects {
    apply<JavaPlugin>()
    repositories {
        maven("https://papermc.io/repo/repository/maven-public/")
        maven("https://maven.elytrium.net/repo/")
    }
}