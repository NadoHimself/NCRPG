# NCRPG - Hytale Setup Guide

## ⚠️ Current Status

This plugin has been **migrated to Hytale** but requires the official Hytale Server API to compile and run.

### What's Ready
- ✅ Gradle build configuration
- ✅ manifest.json plugin descriptor
- ✅ Main plugin class with Hytale API structure
- ✅ Complete skill system architecture (12 skills)
- ✅ Party system
- ✅ MySQL database integration
- ✅ Command system structure
- ✅ Event listener framework

### What's Missing
- ❌ **Hytale Server API dependency** (not publicly available yet)
- ❌ Exact API method names (needs official documentation)
- ❌ Event class implementations
- ❌ Block/Item/Entity API details

## Prerequisites

### Required
- **Java 21+** (JDK 21 or higher)
- **Gradle 8.0+**
- **Hytale Server** (Early Access)
- **MySQL 8.0+** or **MariaDB 10.5+**

### Recommended
- IntelliJ IDEA 2024.3+
- Hytale Server Plugin Template (from CurseForge)

## Installation Steps

### 1. Get Hytale Server API

```bash
# Download HytaleServer.jar from official Hytale launcher
# It should be located at:
# Windows: %appdata%/Hytale/Server/HytaleServer.jar
# Linux: ~/.hytale/Server/HytaleServer.jar
```

### 2. Install to Local Maven Repository

```bash
mvn install:install-file \
  -Dfile=HytaleServer.jar \
  -DgroupId=com.hypixel.hytale \
  -DartifactId=hytale-server \
  -Dversion=1.0.0 \
  -Dpackaging=jar
```

### 3. Configure Database

Edit `src/main/resources/config.yml`:

```yaml
database:
  host: localhost
  port: 3306
  database: hytale_ncrpg
  username: your_username
  password: your_password
```

### 4. Build Plugin

```bash
# Build with Gradle
gradle clean build

# The compiled JAR will be in:
# build/libs/NCRPG-1.0.0.jar
```

### 5. Install Plugin

```bash
# Copy to Hytale mods folder
cp build/libs/NCRPG-1.0.0.jar "%appdata%/Hytale/UserData/Mods/"

# Or on Linux:
cp build/libs/NCRPG-1.0.0.jar "~/.hytale/UserData/Mods/"
```

### 6. Start Hytale Server

```bash
java -jar HytaleServer.jar
```

## Development Setup

### IntelliJ IDEA

1. Open project: `File` → `Open` → Select NCRPG folder
2. Gradle will auto-import dependencies
3. Set Java SDK to 21+: `File` → `Project Structure` → `Project SDK`
4. Enable annotation processing: `Settings` → `Build, Execution, Deployment` → `Compiler` → `Annotation Processors`

### API Adjustments Needed

Once you have access to the official Hytale API documentation, you'll need to adjust:

1. **Import Statements** in all classes
   - Verify `com.hypixel.hytale.plugin.HytalePlugin` is correct
   - Update event package names
   - Confirm command system structure

2. **Event Handlers**
   - Replace generic event signatures with actual Hytale events
   - Update event method names (e.g., `getPlayer()`, `getBlock()`)
   - Implement Entity Component System patterns

3. **Block/Item APIs**
   - Update block type checking methods
   - Implement item drop mechanics
   - Add particle effect calls

4. **Command System**
   - Verify command registration method
   - Update command executor interfaces
   - Implement permission checks

## Debugging

### Enable Debug Logging

Add to Hytale server config:

```yaml
logging:
  level:
    net.nightraid.ncrpg: DEBUG
```

### Common Issues

**Build fails with "cannot find symbol"**
- Hytale API dependency is missing
- Install HytaleServer.jar to local Maven repo (see step 2)

**Plugin doesn't load**
- Check `manifest.json` is in the JAR
- Verify Java 21+ is being used
- Check Hytale server logs for errors

**Database connection fails**
- Verify MySQL is running
- Check credentials in `config.yml`
- Ensure database `hytale_ncrpg` exists

## API Documentation

Official Hytale documentation:
- Main site: https://hytale.com
- Modding docs: https://docs.hytale.com (when available)
- CurseForge: https://support.curseforge.com/en/support/solutions/folders/9000278269

## Support

- GitHub Issues: https://github.com/NadoHimself/NCRPG/issues
- Discord: NightRaid.net Community
- Website: https://nightraid.net

## License

MIT License - See LICENSE file
