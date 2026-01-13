# Changelog

All notable changes to NCRPG will be documented in this file.

## [Unreleased]

### Added
- Hytale plugin system integration
- Gradle build system (replacing Maven)
- manifest.json plugin descriptor
- Comprehensive setup documentation (SETUP.md)
- .gitignore for Gradle projects
- Gradle configuration files (settings.gradle, gradle.properties)

### Changed
- **BREAKING**: Migrated from Maven to Gradle
- **BREAKING**: Updated to Java 21 target
- Plugin main class now extends `HytalePlugin`
- Event system adapted for Hytale API structure
- Command registration updated for Hytale
- Logger changed from java.util.logging to SLF4J

### Removed
- Maven pom.xml configuration
- Bukkit/Spigot API references
- Placeholder comments (replaced with real structure)

## [1.0.0] - 2026-01-13

### Initial Hytale Release

#### Features
- 12 fully-featured RPG skills:
  - Mining (double drops, Super Breaker)
  - Woodcutting (Tree Feller, double drops)
  - Farming (triple drops, Green Terra, Hylian Luck)
  - Fishing (Treasure Hunter, Magic Hunter, Shake)
  - Acrobatics (Roll, Graceful Roll, dodge)
  - Excavation (Giga Drill Breaker, Archaeology)
  - Herbalism (Green Thumb, double drops)
  - Swords (Rupture/bleed, Counter Attack)
  - Axes (Critical strikes, Skull Splitter)
  - Archery (Daze, arrow retrieval)
  - Unarmed (Disarm, Berserk)
  - Combat (general damage bonuses)

- Party system:
  - XP sharing within configurable range
  - Party invites and management
  - Bonus XP multiplier
  - Automatic leader transfer

- Database integration:
  - Async MySQL with HikariCP
  - Player data caching
  - Auto-save system
  - Leaderboards (top 10 per skill)
  - Power level tracking

- Active abilities:
  - Cooldown management
  - Duration tracking
  - Level-based scaling
  - Visual/particle effects

- Commands:
  - `/skills` - View all skill levels
  - `/stats <skill>` - Detailed skill info
  - `/mcrank [skill]` - Leaderboards
  - `/party` - Party management

- Configuration:
  - YAML-based config system
  - Per-skill enable/disable
  - XP multipliers and formulas
  - Ability percentages and unlock levels
  - Database connection settings

#### Technical
- Java 21+ requirement
- Gradle 8.0+ build system
- Hytale Server API integration
- MySQL 8.0+ / MariaDB 10.5+ support
- SLF4J logging framework
- Entity Component System ready
- Async task scheduling
- Efficient data caching

#### Documentation
- Comprehensive README.md
- Detailed SETUP.md guide
- API adjustment instructions
- Troubleshooting section
- Development guidelines

---

## Version History Notes

### Pre-release Development
- January 13, 2026: Initial project structure created
- January 13, 2026: All 12 skills implemented
- January 13, 2026: Event listeners added
- January 13, 2026: Party system completed
- January 13, 2026: Database integration finished
- January 13, 2026: Migrated to Hytale API

### Known Issues
- Hytale Server API not yet publicly available
- Some API method names may need adjustment
- Event signatures pending official documentation
- Block/Item/Entity APIs need verification

### Roadmap
- [ ] Web-based leaderboard interface
- [ ] Discord bot integration
- [ ] Admin management GUI
- [ ] Custom skill creation API
- [ ] PlaceholderAPI support
- [ ] Performance profiling and optimization
- [ ] Multi-language support

---

**Format**: This changelog follows [Keep a Changelog](https://keepachangelog.com/en/1.0.0/)  
**Versioning**: This project uses [Semantic Versioning](https://semver.org/spec/v2.0.0.html)
