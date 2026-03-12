plugins {
    java
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("net.minecraftforge.gradle") version "6.0.25"
}

group = "com.czasclient"
version = "1.0.0"

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

minecraft {
    mappings("official", "1.20.1")
    
    runs {
        create("client") {
            workingDirectory(project.file("run"))
            property("forge.logging.markers", "REGISTRIES")
            property("forge.logging.console.level", "debug")
            mods {
                create("czasclient") {
                    source(sourceSets.main.get())
                }
            }
        }
    }
}

repositories {
    mavenCentral()
    maven("https://maven.minecraftforge.net/")
}

dependencies {
    implementation("net.minecraftforge:forge:1.20.1-47.3.0")
    implementation("org.lwjgl:lwjgl:3.3.3")
    implementation("org.lwjgl:lwjgl-opengl:3.3.3")
    implementation("com.google.code.gson:gson:2.10.1")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.named<Jar>("jar") {
    archiveBaseName.set("CzasClient")
    manifest {
        attributes(
            "Specification-Title" to "Czas Client",
            "Specification-Vendor" to "348 s",
            "Specification-Version" to "1",
            "Implementation-Title" to project.name,
            "Implementation-Version" to project.version,
            "Implementation-Vendor" to "348 s",
            "Implementation-Timestamp" to java.time.Instant.now().toString()
        )
    }
}

tasks.named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
    archiveBaseName.set("CzasClient")
    archiveClassifier.set("")
    mergeServiceFiles()
}
