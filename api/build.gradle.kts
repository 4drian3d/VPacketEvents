plugins {
    java
}

dependencies {
    compileOnly(libs.velocity.api)
    compileOnly(libs.velocity.proxy)
}

java.toolchain.languageVersion.set(JavaLanguageVersion.of(11))