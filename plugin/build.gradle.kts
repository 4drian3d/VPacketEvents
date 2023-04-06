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
    build {
        dependsOn(shadowJar)
    }
    clean {
        delete("run")
    }
    runVelocity {
        velocityVersion(libs.versions.velocity.get())
    }
}

blossom {
    replaceTokenIn("src/main/java/io/github/_4drian3d/vpacketevents/plugin/Constants.java")
    replaceToken("{version}", project.version)
}
