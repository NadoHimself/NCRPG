# 🎮 NCRPG - NightRaid RPG Plugin for Hytale

![Java 25](https://img.shields.io/badge/Java-25-orange.svg)
![Hytale](https://img.shields.io/badge/Hytale-1.0.0-blue.svg)
![Status](https://img.shields.io/badge/Status-In_Development-yellow.svg)

**Complete mcMMO Alternative Plugin for Hytale** - A comprehensive RPG system with 12 skills, party system, MySQL database, and active abilities.

---

## 🎯 Features

### Player Skills (12 Skills)
- ⚔️ **Combat Skills**: Swords, Axes, Archery, Unarmed
- 🔨 **Mining Skills**: Mining, Excavation
- 🌳 **Gathering Skills**: Woodcutting, Herbalism, Fishing
- 🏭 **Crafting Skills**: Repair, Alchemy, Enchanting

### Core Features
- 📈 **Leveling System** - Gain XP and level up skills
- 🔥 **Active Abilities** - Special abilities for each skill
- 👥 **Party System** - Team up with friends
- 💾 **MySQL Database** - Persistent player data storage
- 🏆 **Leaderboards** - Compete with other players

---

## 🛠️ Building the Plugin

### Prerequisites

- **Java 25** ([Download Temurin](https://adoptium.net/temurin/releases/?version=25))
- **Maven 3.9+** ([Download](https://maven.apache.org/download.cgi))
- **HytaleServer.jar** (from your Hytale installation)

### Quick Build

```bash
# 1. Clone the repository
git clone https://github.com/NadoHimself/NCRPG.git
cd NCRPG

# 2. Create libs directory
mkdir libs

# 3. Copy HytaleServer.jar to libs/
# Windows:
copy "%appdata%\Hytale\install\release\package\game\latest\Server\HytaleServer.jar" libs\

# Linux:
cp ~/.local/share/Hytale/install/release/package/game/latest/Server/HytaleServer.jar libs/

# macOS:
cp ~/Library/Application\ Support/Hytale/install/release/package/game/latest/Server/HytaleServer.jar libs/

# 4. Build the plugin
mvn clean package
```

**Output:** `target/NCRPG-1.0.0-SNAPSHOT.jar`

📚 **Detailed instructions:** See [BUILD.md](BUILD.md)

---

## 📦 Installation

### Server Installation

1. **Build or download** the plugin JAR
2. **Copy** `NCRPG-1.0.0-SNAPSHOT.jar` to your Hytale server plugins folder:

```bash
# Windows
copy target\NCRPG-1.0.0-SNAPSHOT.jar %appdata%\Hytale\UserData\Mods\

# Linux
cp target/NCRPG-1.0.0-SNAPSHOT.jar ~/.local/share/Hytale/UserData/Mods/

# macOS
cp target/NCRPG-1.0.0-SNAPSHOT.jar ~/Library/Application\ Support/Hytale/UserData/Mods/
```

3. **Restart** your Hytale server
4. **Configure** `config.yml` (auto-generated on first run)

---

## 🤖 GitHub Actions Build

This repository uses GitHub Actions for automatic builds. To enable builds:

### 1️⃣ Encode HytaleServer.jar

```bash
# Linux/macOS
cat libs/HytaleServer.jar | base64 -w 0 > hytale.txt

# Windows (PowerShell)
[Convert]::ToBase64String([IO.File]::ReadAllBytes("libs\HytaleServer.jar")) | Out-File hytale.txt
```

### 2️⃣ Add as GitHub Secret

1. Go to **Settings** → **Secrets and variables** → **Actions**
2. Click **New repository secret**
3. Name: `HYTALE_SERVER_JAR_BASE64`
4. Value: Paste content from `hytale.txt`
5. Click **Add secret**

### 3️⃣ Trigger Build

- Push to `main` branch or
- Go to **Actions** → **Maven Build** → **Run workflow**

**Download built plugin:** Actions → Latest workflow → Artifacts → **NCRPG-Plugin**

---

## ⚙️ Configuration

### config.yml

```yaml
# Database Configuration
database:
  host: localhost
  port: 3306
  database: hytale_rpg
  username: root
  password: password

# Skill Configuration
skills:
  max-level: 100
  xp-multiplier: 1.0

# Party Configuration
party:
  max-size: 8
  xp-share: true
  xp-share-range: 50
```

---

## 💻 Development

### Project Structure

```
NCRPG/
├── src/
│   └── main/
│       ├── java/
│       │   └── net/nightraid/hytaletestplugin/
│       │       ├── HytaleTestPlugin.java       # Main plugin class
│       │       └── listeners/
│       │           ├── PlayerListener.java      # Player events
│       │           └── BlockListener.java       # Block events
│       └── resources/
│           └── plugin.yml                   # Plugin metadata
├── libs/
│   └── HytaleServer.jar                 # (not in repo)
├── pom.xml                              # Maven configuration
└── BUILD.md                             # Build instructions
```

### Development Workflow

1. Make code changes
2. Run `mvn clean package`
3. Copy JAR to Hytale server
4. Restart server and test

---

## 🐛 Troubleshooting

### Build fails: "Could not find artifact"

**Problem:** HytaleServer.jar not found

**Solution:** Make sure `libs/HytaleServer.jar` exists (see [BUILD.md](BUILD.md))

### "Unsupported class file major version"

**Problem:** Wrong Java version

**Solution:** Install Java 25

```bash
java --version  # Should show "25.x.x"
```

### GitHub Actions build skipped

**Problem:** `HYTALE_SERVER_JAR_BASE64` secret not configured

**Solution:** See [GitHub Actions Build](#-github-actions-build) section above

---

## 📄 License

This project is licensed under the MIT License.

---

## 👤 Author

**Kielian** ([NadoHimself](https://github.com/NadoHimself))  
**Organization:** [NightRaid.net](https://nightraid.net)

---

## 📧 Support

- **Issues:** [GitHub Issues](https://github.com/NadoHimself/NCRPG/issues)
- **Discussions:** [GitHub Discussions](https://github.com/NadoHimself/NCRPG/discussions)
- **Website:** [nightraid.net](https://nightraid.net)

---

## ⭐ Acknowledgments

- Inspired by **mcMMO** for Minecraft
- Built for **Hytale** by Hypixel Studios
- Community-driven development

---

**🎉 Happy Modding!**
