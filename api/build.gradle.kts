plugins {
    `java-library`
    id("com.vanniktech.maven.publish") version "0.35.0"
}

dependencies {
    compileOnlyApi(libs.velocity.api)
    compileOnlyApi(libs.velocity.proxy)
}

java {
    withSourcesJar()
    withJavadocJar()
}

tasks {
    javadoc {
        options.encoding = Charsets.UTF_8.name()
        (options as StandardJavadocDocletOptions).links("https://jd.papermc.io/velocity/3.4.0/")
    }
}

mavenPublishing {
    publishToMavenCentral()
    signAllPublications()
    coordinates(project.group as String, "vpacketevents-api", project.version as String)

    pom {
        name.set(project.name)
        description.set(project.description)
        inceptionYear.set("2022")
        url.set("https://github.com/4drian3d/VPacketEvents")
        licenses {
            license {
                name.set("GNU General Public License version 3 or later")
                url.set("https://opensource.org/licenses/GPL-3.0")
            }
        }
        developers {
            developer {
                id.set("4drian3d")
                name.set("Adrian Gonzales")
                email.set("adriangonzalesval@gmail.com")
            }
        }
        scm {
            connection.set("scm:git:https://github.com/4drian3d/VPacketEvents.git")
            developerConnection.set("scm:git:ssh://git@github.com/4drian3d/VPacketEvents.git")
            url.set("https://github.com/4drian3d/VPacketEvents")
        }
        ciManagement {
            name.set("GitHub Actions")
            url.set("https://github.com/4drian3d/VPacketEvents")
        }
        issueManagement {
            name.set("GitHub")
            url.set("https://github.com/4drian3d/VPacketEvents/issues")
        }
    }
}
