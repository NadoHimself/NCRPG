plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

val pluginGroup: String by project
val pluginVersion: String by project
val pluginName: String by project

group = pluginGroup
version = pluginVersion

repositories {
    mavenCentral()
}

dependencies {
    // Hytale Server API (provided by server)
    compileOnly(files("libs/HytaleServer.jar"))
    
    // MySQL Driver
    implementation("com.mysql:mysql-connector-j:8.3.0")
    
    // HikariCP for connection pooling
    implementation("com.zaxxer:HikariCP:5.1.0")
    
    // Lombok for cleaner code
    compileOnly("org.projectlombok:lombok:1.18.34")
    annotationProcessor("org.projectlombok:lombok:1.18.34")
    
    // Annotations
    compileOnly("org.jetbrains:annotations:24.1.0")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.release.set(25)
}

tasks.shadowJar {
    archiveBaseName.set(pluginName)
    archiveClassifier.set("")
    archiveVersion.set(pluginVersion)
    
    // Relocate dependencies to avoid conflicts
    relocate("com.zaxxer.hikari", "net.nightraid.ncrpg.libs.hikari")
    relocate("com.mysql", "net.nightraid.ncrpg.libs.mysql")
    
    // Exclude unnecessary files
    exclude("META-INF/*.SF")
    exclude("META-INF/*.DSA")
    exclude("META-INF/*.RSA")
}

tasks.build {
    dependsOn(tasks.shadowJar)
}

// Set default task
defaultTasks("shadowJar")
