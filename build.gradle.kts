plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = project.property("pluginGroup") as String
version = project.property("pluginVersion") as String

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
    archiveBaseName.set(project.property("pluginName") as String)
    archiveClassifier.set("")
    archiveVersion.set(project.property("pluginVersion") as String)
    
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
