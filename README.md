# NCRPG - NightRaid RPG Skills System

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Hytale](https://img.shields.io/badge/Hytale-Plugin-blue.svg)](https://hytale.com)
[![Java 21+](https://img.shields.io/badge/Java-21%2B-orange.svg)](https://adoptium.net/)

A complete mcMMO alternative plugin for Hytale featuring 12 fully-featured skills, party system, MySQL database, and active abilities.

## 🚧 Status: Hytale Integration Ready

This plugin has been **migrated to Hytale's plugin system**. The codebase is ready for deployment once the Hytale Server API is available.

✅ **Complete**: Gradle build, manifest.json, plugin structure  
⏳ **Pending**: Official Hytale API documentation for final adjustments

**See [SETUP.md](SETUP.md) for detailed installation instructions.**

---

## Features

### 12 Complete Skills
- **Mining** - Double drops, Super Breaker ability, mining speed multiplier
- **Woodcutting** - Tree Feller ability, double drops, leaf blower
- **Farming** - Triple/double drops, Green Terra ability, Hylian Luck
- **Fishing** - Treasure Hunter (4 tiers), Magic Hunter, Shake, Master Angler
- **Acrobatics** - Roll, Graceful Roll, dodge, 95% max fall damage reduction
- **Excavation** - Giga Drill Breaker, Archaeology (4 treasure tiers)
- **Herbalism** - Green Thumb (auto-replant), double drops, Shroom Thumb
- **Swords** - Rupture (bleed), Counter Attack, damage scaling
- **Axes** - Critical strikes, armor impact, skull splitter
- **Archery** - Daze, arrow retrieval, damage scaling
- **Unarmed** - Disarm, iron arm grip, berserk
- **Combat** - General combat damage bonuses

### Party System
- XP sharing within configurable range
- Party invite and management system
- Bonus XP multiplier for party members
- Automatic leader transfer

### Database & Leaderboards
- Async MySQL with HikariCP connection pooling
- Top 10 leaderboards per skill
- Power level rankings
- Efficient caching system with auto-save

### Abilities
- Active abilities with cooldowns
- Passive abilities with level scaling
- Percentage-based bonuses
- Duration tracking and visual effects

## Quick Start

### Prerequisites
- Java 21 or higher
- Hytale Server (Early Access)
- MySQL 8.0+ or MariaDB 10.5+
- Gradle 8.0+ (or use wrapper)

### Building

```bash
# Clone repository
git clone https://github.com/NadoHimself/NCRPG.git
cd NCRPG

# Build with Gradle
gradle clean build

# Output: build/libs/NCRPG-1.0.0.jar
```

### Installation

1. Copy `NCRPG-1.0.0.jar` to `%appdata%/Hytale/UserData/Mods/`
2. Configure database in `config.yml`
3. Restart Hytale server
4. Grant permissions as needed

**Full setup guide: [SETUP.md](SETUP.md)**

## Configuration

All settings are configurable in `config.yml`:
- Database connection settings
- XP multipliers and formulas
- Per-skill enable/disable
- XP values for all block/entity types
- Ability unlock levels and percentages
- Party settings and range
- Auto-save intervals

## Commands

| Command | Description | Permission |
|---------|-------------|------------|
| `/skills` | Display all skill levels with progress bars | `ncrpg.skills` |
| `/stats <skill>` | Detailed skill information | `ncrpg.stats` |
| `/mcrank [skill]` | View leaderboards | `ncrpg.mcrank` |
| `/party create` | Create a party | `ncrpg.party` |
| `/party invite <player>` | Invite player to party | `ncrpg.party` |
| `/party accept` | Accept party invitation | `ncrpg.party` |
| `/party leave` | Leave current party | `ncrpg.party` |
| `/party disband` | Disband your party | `ncrpg.party` |

## Permissions

- `ncrpg.*` - All NCRPG permissions
- `ncrpg.skills` - Use /skills command (default: true)
- `ncrpg.stats` - Use /stats command (default: true)
- `ncrpg.mcrank` - Use /mcrank command (default: true)
- `ncrpg.party` - Use party commands (default: true)
- `ncrpg.{skill}.cap.{level}` - Set skill level cap per skill

## Architecture

```
NCRPG/
├── abilities/        # Active ability management
├── commands/        # Command handlers
├── config/          # Configuration manager
├── database/        # MySQL/HikariCP integration
├── listeners/       # Hytale event listeners
├── managers/        # Core business logic
├── models/          # Data models (PlayerData, Skills)
├── party/           # Party system
└── skills/          # 12 skill implementations
```

## Development

### Tech Stack
- **Java 21+** - Modern Java with records and pattern matching
- **Gradle 8.0+** - Build automation
- **Hytale API** - Plugin framework
- **MySQL** - Persistent storage
- **HikariCP** - Connection pooling
- **SLF4J** - Logging

### Contributing

1. Fork the repository
2. Create feature branch (`git checkout -b feature/amazing-feature`)
3. Commit changes (`git commit -m 'Add amazing feature'`)
4. Push to branch (`git push origin feature/amazing-feature`)
5. Open Pull Request

### API Adjustments

The plugin uses Hytale API structure based on available information. Once official documentation is released:

1. Update import statements in all classes
2. Adjust event handler signatures
3. Implement Entity Component System patterns
4. Update block/item/entity API calls

See [SETUP.md](SETUP.md) for detailed API adjustment guide.

## Roadmap

- [ ] Final API adjustments (waiting for official docs)
- [ ] Web-based leaderboard interface
- [ ] Discord integration for stats
- [ ] Admin GUI for skill management
- [ ] Custom skill creation API
- [ ] PlaceholderAPI equivalent support

## Support

- **Issues**: [GitHub Issues](https://github.com/NadoHimself/NCRPG/issues)
- **Discord**: NightRaid.net Community
- **Website**: [nightraid.net](https://nightraid.net)

## License

MIT License - See [LICENSE](LICENSE) file

## Credits

**Developed by NightRaid.net**

- Lead Developer: Kielian
- Inspired by mcMMO
- Built for Hytale Early Access (January 2026)

---

⭐ **Star this repo if you're excited for NCRPG on Hytale!** ⭐
