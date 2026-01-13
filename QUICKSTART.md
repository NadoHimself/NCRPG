# NCRPG - Quick Start Guide

🚀 **Get your NCRPG plugin running in 5 minutes!**

## Prerequisites

- ✅ **Java 21+** installed
- ✅ **Gradle** (wrapper included)
- ✅ **Hytale Server** (Early Access)
- ✅ **MySQL** or **MariaDB** running

## Step 1: Install Hytale Server API

### Locate HytaleServer.jar

**Windows:**
```bash
cd %appdata%\Hytale\Server
```

**Linux/Mac:**
```bash
cd ~/.hytale/Server
```

### Install to Maven Local

```bash
mvn install:install-file \
  -Dfile=HytaleServer.jar \
  -DgroupId=com.hypixel.hytale \
  -DartifactId=hytale-server \
  -Dversion=1.0.0 \
  -Dpackaging=jar
```

✅ **Success:** You should see `BUILD SUCCESS`

## Step 2: Build the Plugin

```bash
# Clone or navigate to NCRPG directory
cd NCRPG

# Build with Gradle
./gradlew clean build

# Or on Windows:
gradlew.bat clean build
```

✅ **Output:** `build/libs/NCRPG-1.0.0.jar`

## Step 3: Configure Database

Create MySQL database:

```sql
CREATE DATABASE hytale_ncrpg;
CREATE USER 'ncrpg'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON hytale_ncrpg.* TO 'ncrpg'@'localhost';
FLUSH PRIVILEGES;
```

## Step 4: Install Plugin

### Copy to Hytale mods folder

**Windows:**
```bash
copy build\libs\NCRPG-1.0.0.jar %appdata%\Hytale\UserData\Mods\
```

**Linux/Mac:**
```bash
cp build/libs/NCRPG-1.0.0.jar ~/.hytale/UserData/Mods/
```

## Step 5: Configure Plugin

On first run, the plugin creates `config.yml`. Edit it:

**Location:** `%appdata%\Hytale\UserData\Mods\NCRPG\config.yml`

```yaml
database:
  host: localhost
  port: 3306
  database: hytale_ncrpg
  username: ncrpg
  password: your_password
```

## Step 6: Start Server

```bash
java -jar HytaleServer.jar
```

✅ **Check logs for:** `NCRPG has been enabled successfully!`

## Step 7: Test In-Game

Join your server and try:

```
/skills              # View all your skill levels
/stats mining        # Detailed mining stats
/mcrank             # View leaderboards
/party create        # Create a party
```

## Troubleshooting

### Build fails: "cannot find symbol"

❌ **Problem:** Hytale API not installed locally

✅ **Solution:** Complete Step 1 again

### Plugin doesn't load

❌ **Problem:** Wrong mods folder or incompatible Java version

✅ **Solution:**
- Verify Java 21+ with `java -version`
- Check correct mods folder path
- Check Hytale server logs for errors

### Database connection fails

❌ **Problem:** Wrong credentials or MySQL not running

✅ **Solution:**
- Start MySQL: `sudo systemctl start mysql` (Linux)
- Verify credentials in config.yml
- Test connection: `mysql -u ncrpg -p`

### Skills don't gain XP

❌ **Problem:** Event listeners not working

✅ **Solution:**
- Check server logs for errors
- Verify Hytale API version matches plugin
- Report issue on GitHub

## Development Build

For development with auto-reload:

```bash
# Watch for changes and auto-build
./gradlew build --continuous
```

## Quick Commands Reference

| Command | Description |
|---------|-------------|
| `/skills` | View all skill levels with progress bars |
| `/stats <skill>` | Detailed info for specific skill |
| `/mcrank [skill]` | Leaderboards (top 10 or specific skill) |
| `/party create` | Create a party |
| `/party invite <player>` | Invite player to party |
| `/party leave` | Leave current party |

## Skills Overview

### Gathering Skills
- ⛏️ **Mining** - Break ores and stone for double drops
- 🪓 **Woodcutting** - Chop trees with Tree Feller ability
- 🌾 **Farming** - Harvest crops for triple drops
- 🎣 **Fishing** - Catch fish and find treasure
- ⛰️ **Excavation** - Dig for archaeology treasures
- 🌿 **Herbalism** - Gather plants with auto-replant

### Combat Skills
- ⚔️ **Swords** - Bleed enemies with Rupture
- 🪓 **Axes** - Deal critical strikes
- 🏹 **Archery** - Daze enemies and retrieve arrows
- 👊 **Unarmed** - Disarm opponents and go Berserk
- 🤸 **Acrobatics** - Dodge attacks and negate fall damage
- 💪 **Combat** - General combat bonuses

## Next Steps

- 📖 Read [SETUP.md](SETUP.md) for detailed configuration
- 🔧 Customize skills in `config.yml`
- 🎮 Join your server and start leveling!
- 🐛 Report issues on [GitHub](https://github.com/NadoHimself/NCRPG/issues)

## Support

- **GitHub Issues:** [Report bugs](https://github.com/NadoHimself/NCRPG/issues)
- **Discord:** NightRaid.net Community
- **Website:** https://nightraid.net

---

**Made with ❤️ by NightRaid.net**
