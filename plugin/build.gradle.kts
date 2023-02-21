plugins {
    java
    alias(libs.plugins.runvelocity)
    alias(libs.plugins.blossom)
    alias(libs.plugins.shadow)
}

dependencies {
    compileOnly(libs.netty)
    compileOnly(libs.velocity.api)
    compileOnly(libs.velocity.proxy)
    annotationProcessor(libs.velocity.api)
    implementation(projects.vpacketeventsApi)
}

tasks {
    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(11)
    }
    clean {
        delete("run")
    }
    runVelocity {
        velocityVersion(libs.versions.velocity.get())
    }
}

blossom {
    replaceTokenIn("src/main/java/io/github/_4drian3d/Constants.java")
    replaceToken("{version}", project.version)
}

java.toolchain.languageVersion.set(JavaLanguageVersion.of(11))
