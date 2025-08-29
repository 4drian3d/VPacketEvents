plugins {
    java
    alias(libs.plugins.runvelocity)
    alias(libs.plugins.blossom)
    alias(libs.plugins.shadow)
    alias(libs.plugins.idea.ext)
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
    shadowJar {
        archiveBaseName.set(rootProject.name)
        archiveClassifier.set("")
    }
}

sourceSets {
    main {
        blossom {
            javaSources {
                property("version", project.version.toString())
            }
        }
    }
}
