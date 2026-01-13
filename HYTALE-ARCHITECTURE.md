# Hytale Architecture Guide for NCRPG

## 🎯 Confirmed Hytale Technical Details

Based on official sources and the Hytale developer community:

### Server Architecture

- **Server-Side Language:** Java (perfect for us!)
- **Client-Side Language:** C# (closed source, anti-cheat)
- **Server Model:** "Shared Source" - Server code readable for modders
- **Java Version:** Java 21+ recommended

### Entity Component System (ECS)

**CRITICAL:** Hytale uses **Flecs ECS**, not traditional event-based architecture like Bukkit/Spigot.

#### Traditional OOP (What we currently use)
```java
class Zombie extends Monster {
    private int health;
    public void attack() { ... }
}
```

#### ECS (What Hytale uses)
```java
// Entity is just an ID
int zombieEntity = 101;

// Components are pure data
world.addComponent(zombieEntity, new PositionComponent(x, y, z));
world.addComponent(zombieEntity, new HealthComponent(20));
world.addComponent(zombieEntity, new AIComponent());

// Systems process entities with specific components
class CombatSystem {
    public void update() {
        for (Entity e : world.getEntitiesWith(HealthComponent.class, DamageComponent.class)) {
            // Process combat
        }
    }
}
```

### Data-Driven Design

- **Blocks, Items, UI:** Defined via **JSON schemas**
- **Gameplay Behavior:** **Visual node-based scripting** (like Unreal Blueprints)
- **Backend Logic:** Java plugins (our NCRPG)

## 🔄 Migration Strategy

### Phase 1: Current Implementation (Event-Based) ✅

Our current code uses traditional event listeners:

```java
@EventHandler
public void onBlockBreak(BlockBreakEvent event) {
    Player player = event.getPlayer();
    // Grant XP
}
```

**Status:** ✅ Complete and ready

**Why this might still work:**
- Hytale likely provides event wrappers over ECS for plugin compatibility
- Many engines offer "high-level" APIs over ECS internals
- Easier for plugin developers familiar with Bukkit/Spigot

### Phase 2: ECS Adaptation (Day 1 of Early Access)

Once we have access to the actual Hytale server:

1. **Decompile Server JAR** (if EULA allows)
2. **Study Component Structure**
3. **Identify System Injection Points**
4. **Migrate Critical Systems**

#### Example ECS Migration

**Before (Event-Based):**
```java
@EventHandler
public void onBlockBreak(BlockBreakEvent event) {
    Block block = event.getBlock();
    Player player = event.getPlayer();
    
    if (block.getType() == IRON_ORE) {
        grantMiningXP(player, 15);
    }
}
```

**After (ECS-Based):**
```java
public class MiningSkillSystem extends HytaleSystem {
    @Override
    public void update(World world) {
        // Query for entities with PlayerComponent + MiningActionComponent
        Query query = world.query()
            .with(PlayerComponent.class)
            .with(MiningActionComponent.class)
            .build();
        
        query.forEach((entity, player, miningAction) -> {
            if (miningAction.getBlockType() == IRON_ORE) {
                grantMiningXP(player.getUUID(), 15);
            }
        });
    }
}
```

### Phase 3: Visual Scripting Integration

For custom behaviors (mob AI, custom items), we'll need to:

1. Use Hytale's visual node editor
2. Export behavior graphs as JSON
3. Reference them in our plugin code

## 🛠️ Current NCRPG Code Structure

### What Works Now

✅ **Managers** - SkillManager, PlayerDataManager, PartyManager  
✅ **Database** - MySQL with HikariCP, async operations  
✅ **Skills** - All 12 skills with XP calculations  
✅ **Commands** - /skills, /stats, /mcrank, /party  
✅ **Configuration** - YAML-based config system  

### What Needs Adaptation

⚠️ **Event Listeners** - May need ECS conversion  
⚠️ **Block/Entity References** - ECS entity IDs instead of objects  
⚠️ **World Interaction** - Component queries instead of direct access  

### What Stays The Same

✅ **Business Logic** - XP formulas, level calculations  
✅ **Player Data** - UUID-based storage  
✅ **Database Schema** - Compatible with any architecture  
✅ **Party System** - Player-to-player relationships  

## 📚 Flecs ECS Learning Resources

### Key Concepts

1. **Entity:** Just an ID (int or long)
2. **Component:** Pure data struct (no methods)
3. **System:** Logic that processes entities with specific components
4. **Query:** Filter entities by component composition

### Example Component Design for NCRPG

```java
// Skill data component
public record SkillDataComponent(
    UUID playerUUID,
    SkillType skillType,
    int level,
    int xp,
    long lastXPGain
) {}

// Active ability component
public record ActiveAbilityComponent(
    String abilityName,
    long startTime,
    long duration
) {}

// Mining action component
public record MiningActionComponent(
    BlockType blockType,
    long timestamp
) {}
```

### Example System

```java
public class SkillXPSystem extends HytaleSystem {
    @Override
    public void update(World world, float deltaTime) {
        // Find all players who performed skill actions this tick
        Query query = world.query()
            .with(PlayerComponent.class)
            .with(SkillActionComponent.class)
            .build();
        
        query.forEach((entity, player, action) -> {
            // Grant XP based on action
            grantXP(player.getUUID(), action.getSkillType(), action.getXPAmount());
            
            // Remove action component (one-time event)
            world.removeComponent(entity, SkillActionComponent.class);
        });
    }
}
```

## 🚀 Day 1 Early Access Plan

### Hour 1: Reconnaissance
1. Download Hytale Server
2. Decompile server JAR (check EULA)
3. Locate API packages
4. Identify event vs ECS structure

### Hour 2-4: Quick Adaptation
1. If events exist: Minimal changes needed
2. If pure ECS: Implement component wrappers
3. Test basic functionality (player join, commands)

### Hour 5-8: Skills Testing
1. Test each skill system
2. Verify XP gain
3. Check database persistence
4. Test party system

### Day 2-7: Polish & Optimize
1. Migrate to pure ECS (if beneficial)
2. Optimize component queries
3. Add visual scripting for special effects
4. Performance profiling

## 📝 Notes for NCRPG Developers

### Don't Panic!

The current event-based code is **not wasted work**. Here's why:

1. **Business logic stays the same** - XP formulas, level calculations, party logic
2. **Database design is architecture-agnostic**
3. **Commands work identically**
4. **Most APIs will have event wrappers** for compatibility

### What to Learn Before Launch

- 📚 ECS fundamentals (watch tutorials on Flecs, EnTT, or Unity ECS)
- 📖 JSON schema design
- 🎮 Visual scripting concepts (Blueprints, Bolt, PlayMaker)
- ☕ Modern Java features (records, pattern matching, sealed classes)

### Community Resources

- **HytaleCharts.com** - Developer news and guides
- **Hytale Discord** - Developer channels
- **Flecs Documentation** - https://github.com/SanderMertens/flecs
- **ECS FAQ** - https://github.com/SanderMertens/ecs-faq

## ✨ Advantages of ECS for NCRPG

Once we migrate to pure ECS:

### Performance
- **Cache-friendly** - Components stored contiguously in memory
- **Parallel processing** - Systems can run in parallel
- **Data-oriented** - Better CPU utilization

### Flexibility
- **Dynamic abilities** - Add/remove components at runtime
- **Modular skills** - Mix and match skill components
- **Easy debugging** - Inspect entity components directly

### Example: Party XP Sharing

**Traditional:**
```java
for (Player member : party.getMembers()) {
    if (member.isOnline() && inRange(member, player)) {
        grantXP(member, xp * 0.5);
    }
}
```

**ECS (Parallel):**
```java
Query partyQuery = world.query()
    .with(PlayerComponent.class)
    .with(PartyMemberComponent.class)
    .with(OnlineComponent.class)
    .build();

// Automatically parallelized by Flecs
partyQuery.forEach((entity, player, partyMember) -> {
    if (partyMember.getPartyID() == targetParty && inRange(...)) {
        grantXP(player.getUUID(), xp * 0.5);
    }
});
```

## 🎯 Conclusion

NCRPG is **well-positioned** for Hytale:

✅ Java server-side (perfect match)  
✅ Modular architecture (easy to adapt)  
✅ Database-driven (architecture agnostic)  
✅ "Shared Source" allows day-1 adaptation  

The event-based code is a **solid foundation**. ECS migration (if needed) will be straightforward because:

1. Business logic is separate from event handling
2. Components map 1:1 to our existing data models
3. Systems are just our current event handlers refactored

**We're ready for Hytale Early Access!** 🚀

---

**Last Updated:** January 13, 2026  
**Source:** HytaleCharts.com Developer API Guide  
**NCRPG Version:** 1.0.0
