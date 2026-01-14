# NCRPG - NightRaid Complete RPG

> Complete mcMMO Alternative for Hytale

## 🎮 Features

- **Skills System** - Track player progression
- **Events** - Listen to player and block events
- **Database** - MySQL/HikariCP support
- **Lightweight** - Minimal overhead

## 📋 Requirements

- Java 25 JDK ([Download](https://www.oracle.com/java/technologies/downloads/))
- Gradle 8.x (included via wrapper)
- Hytale Server JAR

## 🚀 Quick Start

### 1. Clone Repository

```bash
git clone https://github.com/NadoHimself/NCRPG.git
cd NCRPG
```

### 2. Add Hytale Server JAR

Place `HytaleServer.jar` in the `libs/` folder:

```
NCRPG/
└── libs/
    └── HytaleServer.jar  ← Place here
```

### 3. Build Plugin

**Windows:**
```powershell
.\gradlew.bat shadowJar
```

**Linux/Mac:**
```bash
./gradlew shadowJar
```

### 4. Find Output

Your plugin JAR will be in:
```
build/libs/NCRPG-1.0.0-SNAPSHOT.jar
```

## 📦 Installation

1. Copy `NCRPG-1.0.0-SNAPSHOT.jar` to your Hytale server's `plugins/` folder
2. Start your Hytale server
3. Look for startup messages:
   ```
   [NCRPG] Plugin wird aktiviert...
   [NCRPG] Plugin erfolgreich aktiviert!
   ```

## 🔧 Development

### Build Commands

```bash
# Clean build
./gradlew clean shadowJar

# Compile only
./gradlew compileJava

# Run tests
./gradlew test
```

### Project Structure

```
NCRPG/
├── src/main/java/net/nightraid/ncrpg/
│   ├── NCRPGPlugin.java          # Main plugin class
│   └── listeners/
│       ├── BlockListener.java    # Block event listener
│       └── PlayerListener.java   # Player event listener
├── src/main/resources/
│   ├── manifest.json             # Hytale plugin manifest
│   └── config.yml                # Plugin configuration
├── build.gradle.kts              # Gradle build script
├── gradle.properties             # Project properties
└── settings.gradle.kts           # Gradle settings
```

## 📖 Plugin Information

- **Group:** NightRaid
- **Name:** NCRPG
- **Identifier:** `NCRPG`
- **Main Class:** `net.nightraid.ncrpg.NCRPGPlugin`
- **Version:** 1.0.0-SNAPSHOT

## 🔌 Loading Plugin

On a running Hytale server:

```
/plugin load NCRPG
```

## 📝 License

This project is licensed under the MIT License.

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## 📧 Support

- Website: [NightRaid.net](https://nightraid.net)
- Issues: [GitHub Issues](https://github.com/NadoHimself/NCRPG/issues)

---

**Built with ❤️ for the Hytale Community**
