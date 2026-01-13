# NCRPG - NightRaid RPG Skills System

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

A complete mcMMO alternative plugin for Hytale featuring 12 fully-featured skills, party system, MySQL database, and active abilities.

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
- Async MySQL with auto-reconnect
- Top 10 leaderboards per skill
- Power level rankings
- Efficient caching system

### Abilities
- Active abilities with cooldowns
- Passive abilities with level scaling
- Percentage-based bonuses
- Duration tracking

## Installation

1. Build the plugin: `mvn clean package`
2. Place `NCRPG.jar` in your Hytale plugins folder
3. Configure database settings in `config.yml`
4. Restart server
5. Grant permissions as needed

## Configuration

All settings are configurable in `config.yml`:
- Database connection settings
- XP multipliers and formulas
- Per-skill enable/disable
- XP values for all block/entity types
- Ability unlock levels and percentages
- Party settings

## Commands

- `/skills` - Display all skill levels with progress bars
- `/stats <skill>` - Detailed skill information
- `/mcrank [skill]` - View leaderboards
- `/party create` - Create a party
- `/party invite <player>` - Invite player to party
- `/party leave` - Leave current party

## Permissions

- `ncrpg.skills` - Use /skills command
- `ncrpg.stats` - Use /stats command
- `ncrpg.mcrank` - Use /mcrank command
- `ncrpg.party` - Use party commands
- `ncrpg.{skill}.cap.{level}` - Set skill level cap

## Development Status

⚠️ **Note**: This plugin uses placeholders for Hytale API. Replace all `// NOTE: Replace with Hytale API` comments with actual Hytale API calls once available.

## Building

```bash
mvn clean package
```

## License

MIT License - See LICENSE file

## Credits

Developed by NightRaid.net
