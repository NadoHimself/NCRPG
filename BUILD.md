# 🔨 NCRPG Build Instructions

## Prerequisites

- **Java 25** (Temurin/Adoptium recommended)
- **Maven 3.9+**
- **HytaleServer.jar** (from your Hytale installation)

---

## 📥 Step 1: Get HytaleServer.jar

You need the Hytale Server JAR file to compile this plugin. Get it from your Hytale installation:

### Windows
```bash
cd %appdata%\Hytale\install\release\package\game\latest\Server
copy HytaleServer.jar <path-to-this-project>\libs\
```

### Linux
```bash
cp ~/.local/share/Hytale/install/release/package/game/latest/Server/HytaleServer.jar <path-to-this-project>/libs/
```

### macOS
```bash
cp ~/Library/Application\ Support/Hytale/install/release/package/game/latest/Server/HytaleServer.jar <path-to-this-project>/libs/
```

---

## ⚙️ Step 2: Build the Plugin

```bash
# Make sure you're in the project root directory
mvn clean package
```

**The compiled JAR will be in:** `target/NCRPG-1.0.0-SNAPSHOT.jar`

---

## 🚀 Step 3: Install the Plugin

Copy the built JAR to your Hytale server plugins folder:

### Windows
```bash
copy target\NCRPG-1.0.0-SNAPSHOT.jar %appdata%\Hytale\UserData\Mods\
```

### Linux
```bash
cp target/NCRPG-1.0.0-SNAPSHOT.jar ~/.local/share/Hytale/UserData/Mods/
```

### macOS
```bash
cp target/NCRPG-1.0.0-SNAPSHOT.jar ~/Library/Application\ Support/Hytale/UserData/Mods/
```

---

## 🐛 Troubleshooting

### Build fails with "Could not find artifact"

**Problem:** HytaleServer.jar not found in `libs/` folder

**Solution:** Make sure you copied HytaleServer.jar to the `libs/` directory (Step 1)

### "Unsupported class file major version"

**Problem:** Wrong Java version

**Solution:** Install Java 25 (Temurin/Adoptium)

```bash
java --version  # Should show "25.x.x"
```

---

## 📝 Quick Setup Script

**Windows (PowerShell):**
```powershell
# Run from project root
mkdir libs
copy "$env:APPDATA\Hytale\install\release\package\game\latest\Server\HytaleServer.jar" libs\
mvn clean package
```

**Linux/macOS (Bash):**
```bash
# Run from project root
mkdir -p libs
cp ~/.local/share/Hytale/install/release/package/game/latest/Server/HytaleServer.jar libs/
mvn clean package
```

---

## 🎯 Development Workflow

1. Make code changes
2. Run `mvn clean package`
3. Copy JAR to Mods folder
4. Restart Hytale server
5. Test your changes

---

## ✅ Successful Build Output

You should see:
```
[INFO] BUILD SUCCESS
[INFO] Total time: ~5s
[INFO] Final Memory: 50M/200M
```

And the file `target/NCRPG-1.0.0-SNAPSHOT.jar` should exist.
