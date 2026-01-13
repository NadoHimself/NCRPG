# libs/ Directory - Hytale Server API

This directory is used for storing the **Hytale Server API JAR** required to compile the plugin.

---

## ⚠️ Important: JAR Not Included

The `hytale-server.jar` is **NOT** included in this repository because:
1. Hytale is not publicly released yet (Early Access expected Q2 2026)
2. We respect Hypixel Studios' licensing terms
3. File size constraints on GitHub

---

## 📦 How to Get the Hytale Server JAR

### When Hytale Early Access is available:

**1. Download Hytale Server**
```bash
# Official download (example URL - will be updated)
wget https://hytale.com/downloads/hytale-server-latest.jar
```

**2. Copy to libs directory**
```bash
# Windows
copy hytale-server-latest.jar libs\hytale-server.jar

# Linux/Mac
cp hytale-server-latest.jar libs/hytale-server.jar
```

**3. Verify the JAR**
```bash
# Should show the JAR file
ls -lh libs/

# Expected output:
# -rw-r--r-- 1 user user 15M Jan 13 22:00 hytale-server.jar
```

---

## 🛠️ Alternative: Use Maven Local Repository

If you prefer Maven local repository over direct JAR files:

```bash
mvn install:install-file \
    -Dfile=hytale-server.jar \
    -DgroupId=com.hypixel.hytale \
    -DartifactId=hytale-server \
    -Dversion=1.0.0 \
    -Dpackaging=jar
```

Then update `build.gradle`:
```gradle
dependencies {
    // Replace this:
    compileOnly(files('libs/hytale-server.jar'))
    
    // With this:
    compileOnly 'com.hypixel.hytale:hytale-server:1.0.0'
}
```

---

## 🚀 Build Without API (Expected Behavior)

Without `hytale-server.jar`, the build will **compile with warnings**:

```
warning: [path] bad path element "libs/hytale-server.jar": no such file or directory
Note: Some input files use unchecked or unsafe operations.
```

**This is normal!** The plugin:
- ✅ Business logic compiles fine
- ✅ Database schema is created
- ✅ All 12 skills implemented
- ❌ Hytale API calls won't compile (expected)

---

## 📋 Day 1 Early Access Checklist

### Hour 1: Get the JAR
```bash
# 1. Download Hytale Server
cd ~/Downloads
wget https://hytale.com/downloads/hytale-server.jar

# 2. Copy to NCRPG project
cp hytale-server.jar ~/NCRPG/libs/

# 3. Verify
ls -lh ~/NCRPG/libs/hytale-server.jar
```

### Hour 2: First Build
```bash
cd ~/NCRPG

# Windows
build-local.bat

# Linux/Mac
./build-local.sh

# Should now build successfully!
```

### Hour 3: Deploy & Test
```bash
# Copy to server
cp build/libs/NCRPG-1.0.0.jar ~/.hytale/UserData/Mods/

# Start server
hytale-server start

# Test in-game
/skills
/stats mining
```

---

## 🔍 Decompiling for Research (Day 1)

If you want to understand Hytale's API structure:

```bash
# Install Fernflower (Java decompiler)
wget https://github.com/JetBrains/intellij-community/raw/master/plugins/java-decompiler/engine/fernflower.jar

# Decompile the server
java -jar fernflower.jar hytale-server.jar ./decompiled/

# Explore the API
cd decompiled
grep -r "EventHandler" .
grep -r "Component" .
grep -r "@Plugin" .
```

**This helps us:**
- Understand actual API structure
- Migrate from placeholder events to real ones
- Optimize for ECS if needed

---

## 📝 File Structure

```
NCRPG/
├── libs/
│   ├── README.md          (this file)
│   └── hytale-server.jar  (you add this)
├── build.gradle
├── src/
│   └── main/
│       └── java/
│           └── net/nightraid/ncrpg/
│               └── NCRPG.java
└── build-local.bat
```

---

## ❓ FAQ

### Q: Can I build without the JAR?
A: Yes, but with compilation warnings. Business logic still works.

### Q: Where do I get the JAR?
A: Official Hytale downloads page when Early Access releases.

### Q: Can I share the JAR?
A: Check Hypixel Studios' license terms (likely no redistribution).

### Q: What if the API changes?
A: We have TODOs marked throughout code. Update as needed.

---

## 🎯 Current Status

| Component | Status | Needs JAR? |
|-----------|--------|------------|
| Business Logic | ✅ Complete | ❌ No |
| Database | ✅ Complete | ❌ No |
| Skills System | ✅ Complete | ❌ No |
| Event Listeners | 🟡 Placeholders | ✅ Yes |
| Command System | 🟡 Placeholders | ✅ Yes |
| Build System | ✅ Complete | 🟡 Optional |

---

**Ready for Day 1 Hytale Early Access!** 🚀

*See [QUICKSTART.md](../QUICKSTART.md) for full build instructions.*
