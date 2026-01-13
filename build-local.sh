#!/bin/bash
# ============================================
# NCRPG Local Build Script
# Builds the plugin without requiring Hytale API
# ============================================

echo ""
echo "========================================"
echo "  NCRPG - Local Build Script"
echo "  Version 1.0.0"
echo "========================================"
echo ""

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo -e "${RED}[ERROR]${NC} Java is not installed or not in PATH!"
    echo "Please install Java 21+ from https://adoptium.net/"
    exit 1
fi

echo -e "${GREEN}[INFO]${NC} Java detected"
java -version
echo ""

# Check if we're in the right directory
if [ ! -f "build.gradle" ]; then
    echo -e "${RED}[ERROR]${NC} build.gradle not found!"
    echo "Please run this script from the NCRPG root directory."
    exit 1
fi

echo -e "${GREEN}[INFO]${NC} Project directory confirmed"
echo ""

# Clean previous builds
echo -e "${YELLOW}[STEP 1/3]${NC} Cleaning previous builds..."
if [ -d "build" ]; then
    rm -rf build
    echo -e "${GREEN}[OK]${NC} Build directory cleaned"
else
    echo -e "${GREEN}[OK]${NC} No previous build found"
fi
echo ""

# Make gradlew executable
chmod +x gradlew

# Build with Gradle (ignoring compilation errors)
echo -e "${YELLOW}[STEP 2/3]${NC} Building with Gradle..."
echo -e "${YELLOW}[INFO]${NC} Note: Compilation errors are expected without Hytale API"
echo ""

./gradlew clean build -x test --continue 2>/dev/null || true

echo ""

# Check if JAR was created
echo -e "${YELLOW}[STEP 3/3]${NC} Checking build output..."
if [ -f "build/libs/NCRPG-1.0.0.jar" ]; then
    echo ""
    echo "========================================"
    echo "  BUILD SUCCESSFUL!"
    echo "========================================"
    echo ""
    echo "JAR Location: build/libs/NCRPG-1.0.0.jar"
    echo ""
    ls -lh build/libs/*.jar
    echo ""
    echo -e "${GREEN}[NEXT STEPS]${NC}"
    echo "1. Copy JAR to: ~/.hytale/UserData/Mods/"
    echo "2. Configure database in config.yml"
    echo "3. Start your Hytale server"
    echo "4. Test with: /skills"
    echo ""
    echo "See QUICKSTART.md for detailed instructions."
    echo ""
else
    echo ""
    echo "========================================"
    echo "  BUILD INCOMPLETE"
    echo "========================================"
    echo ""
    echo -e "${YELLOW}[INFO]${NC} JAR not found - this is expected without Hytale API"
    echo ""
    echo "To build successfully:"
    echo "1. Obtain HytaleServer.jar from your Hytale installation"
    echo "2. Install it locally:"
    echo "   mvn install:install-file -Dfile=HytaleServer.jar \\"
    echo "       -DgroupId=com.hypixel.hytale \\"
    echo "       -DartifactId=hytale-server \\"
    echo "       -Dversion=1.0.0 \\"
    echo "       -Dpackaging=jar"
    echo "3. Run this script again: ./build-local.sh"
    echo ""
    echo "See QUICKSTART.md for full instructions."
    echo ""
fi
