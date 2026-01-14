# 🎮 Hytale Plugin API Documentation

> **Version:** 1.0.0-ALPHA  
> **Last Updated:** January 14, 2026  
> **Status:** Work in Progress - Based on Hytale Server Decompilation

---

## 📑 Table of Contents

1. [Plugin System](#-plugin-system)
2. [Event System](#-event-system)
3. [Available Events](#-available-events)
4. [Best Practices](#-best-practices)
5. [Examples](#-examples)

---

## 🔌 Plugin System

### Plugin Architecture

The Hytale Plugin System is built on a sophisticated lifecycle management system with automatic resource cleanup and dependency resolution.

### Plugin Lifecycle States

```java
public enum PluginState {
    NONE,           // Initial state
    SETUP,          // setup() phase
    START,          // start() phase  
    ENABLED,        // Fully loaded and active
    SHUTDOWN,       // Shutting down
    DISABLED        // Completely disabled
}
```

**Lifecycle Flow:**
```
NONE → SETUP → START → ENABLED → SHUTDOWN → DISABLED
```

### Creating a Plugin

All plugins must extend `JavaPlugin`:

```java
package com.example.myplugin;

import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;

public class MyPlugin extends JavaPlugin {
    
    public MyPlugin(JavaPluginInit init) {
        super(init);
    }
    
    @Override
    protected void setup() {
        // Called during SETUP phase
        // Register events, commands, configs here
        getLogger().info("Setting up MyPlugin...");
        
        // Register events
        getEventRegistry().register(PlayerConnectEvent.class, event -> {
            getLogger().info("Player connected: " + event.getPlayer().getName());
        });
    }
    
    @Override
    protected void start() {
        // Called during START phase
        // Initialize runtime systems, spawn entities, etc.
        getLogger().info("MyPlugin started successfully!");
    }
    
    @Override
    protected void shutdown() {
        // Called during SHUTDOWN phase
        // Save data, cleanup resources
        getLogger().info("MyPlugin shutting down...");
    }
}
```

### Plugin Manifest

Every plugin requires a `manifest.json` in the JAR root:

```json
{
  "name": "MyPlugin",
  "group": "com.example",
  "version": "1.0.0",
  "main": "com.example.myplugin.MyPlugin",
  "description": "My awesome Hytale plugin",
  "authors": ["YourName"],
  "serverVersion": ">=1.0.0",
  "dependencies": {
    "SomeOtherPlugin": ">=2.0.0"
  },
  "disabledByDefault": false,
  "includesAssetPack": false
}
```

### PluginBase - Available Registries

Every plugin has access to these registries via `getXxxRegistry()`:

| Registry | Purpose | Example |
|----------|---------|---------|
| **EventRegistry** | Event listeners | `getEventRegistry().register(...)` |
| **CommandRegistry** | Commands | `getCommandRegistry().register(...)` |
| **TaskRegistry** | Scheduled tasks | `getTaskRegistry().schedule(...)` |
| **BlockStateRegistry** | Custom blocks | `getBlockStateRegistry().register(...)` |
| **EntityRegistry** | Custom entities | `getEntityRegistry().register(...)` |
| **ClientFeatureRegistry** | Client features | `getClientFeatureRegistry().register(...)` |
| **AssetRegistry** | Custom assets | `getAssetRegistry().register(...)` |
| **EntityStoreRegistry** | Entity components | `getEntityStoreRegistry().register(...)` |
| **ChunkStoreRegistry** | Chunk components | `getChunkStoreRegistry().register(...)` |

**All registries automatically clean up when the plugin shuts down!**

### Configuration System

Plugins can define configurations using Codecs:

```java
public class MyPlugin extends JavaPlugin {
    
    private Config<MyConfig> config;
    
    public MyPlugin(JavaPluginInit init) {
        super(init);
        
        // Define config BEFORE setup()
        this.config = withConfig("config", MyConfig.CODEC);
    }
    
    @Override
    protected void setup() {
        MyConfig cfg = config.get();
        getLogger().info("Max players: " + cfg.maxPlayers);
    }
}

public class MyConfig {
    public static final Codec<MyConfig> CODEC = BuilderCodec.create(...)
    
    public int maxPlayers;
    public String welcomeMessage;
}
```

---

## 🎯 Event System

### Event Architecture

The Hytale Event System is a **priority-based, type-safe, high-performance** event bus with support for:
- ✅ Synchronous events
- ✅ Asynchronous events  
- ✅ Priority execution
- ✅ Keyed listeners (e.g., per-player)
- ✅ Global listeners
- ✅ Cancellable events
- ✅ Automatic cleanup

### Event Interfaces

#### Core Interfaces

```java
// Base interface - ALL events extend this
public interface IBaseEvent {}

// Standard synchronous event
public interface IEvent extends IBaseEvent {}

// Asynchronous event (returns CompletableFuture)
public interface IAsyncEvent extends IBaseEvent {}

// Cancellable events
public interface ICancellable {
    boolean isCancelled();
    void setCancelled(boolean cancelled);
}

// Events with post-processing
public interface IProcessedEvent {
    void processEvent(@Nonnull String identifier);
}
```

### Event Registration

#### 1. Simple Event Registration

```java
@Override
protected void setup() {
    getEventRegistry().register(PlayerConnectEvent.class, event -> {
        getLogger().info("Player joined: " + event.getPlayer().getName());
    });
}
```

#### 2. With Priority

```java
getEventRegistry().register(
    EventPriority.HIGH,  // or use short value
    PlayerChatEvent.class,
    event -> {
        if (event.getMessage().contains("spam")) {
            event.setCancelled(true);
        }
    }
);
```

#### 3. Keyed Registration (Per-Entity)

```java
UUID playerUUID = player.getUniqueId();

getEventRegistry().register(
    PlayerInteractEvent.class,
    playerUUID,  // Only fires for this player
    event -> {
        getLogger().info("Specific player interacted!");
    }
);
```

#### 4. Global Registration (All Keys)

```java
getEventRegistry().registerGlobal(
    EntityEvent.class,
    event -> {
        // Fires for ALL entities, regardless of key
    }
);
```

#### 5. Unhandled Registration (Fallback)

```java
getEventRegistry().registerUnhandled(
    CustomEvent.class,
    event -> {
        // Only fires if no other handler processed it
        getLogger().warning("No handler for this event!");
    }
);
```

#### 6. Async Event Registration

```java
getEventRegistry().registerAsync(
    SomeAsyncEvent.class,
    event -> CompletableFuture.supplyAsync(() -> {
        // Heavy computation in background thread
        processExpensiveOperation();
        return null;
    })
);
```

### Event Priorities

Events are executed in priority order (lowest to highest):

| Priority | Value | When to Use |
|----------|-------|-------------|
| **LOWEST** | -100 | First to run - early modifications |
| **LOW** | -50 | Before normal handlers |
| **NORMAL** | 0 | Default - most handlers |
| **HIGH** | 50 | After normal handlers |
| **HIGHEST** | 100 | Last to run - final checks/logging |

**Execution Order:** LOWEST → LOW → NORMAL → HIGH → HIGHEST

**Same Priority:** Execution order matches registration order.

### Event Registration Management

```java
// Store registration for later unregister
EventRegistration<PlayerConnectEvent> registration = 
    getEventRegistry().register(PlayerConnectEvent.class, event -> {
        // Handler
    });

// Later: check and unregister
if (registration.isEnabled()) {
    registration.unregister();
}

// Note: All registrations are automatically unregistered on plugin shutdown!
```

---

## 📋 Available Events

### Player Events

Located in `com.hypixel.hytale.server.core.event.events.player`

#### PlayerConnectEvent
Fired when a player establishes connection to the server.

```java
public class PlayerConnectEvent implements IEvent {
    @Nonnull public Player getPlayer();
    @Nonnull public PlayerConnectionData getConnectionData();
}
```

**Example:**
```java
getEventRegistry().register(PlayerConnectEvent.class, event -> {
    Player player = event.getPlayer();
    getLogger().info(player.getName() + " connected from " + 
        event.getConnectionData().getAddress());
});
```

---

#### PlayerDisconnectEvent
Fired when a player disconnects from the server.

```java
public class PlayerDisconnectEvent implements IEvent {
    @Nonnull public Player getPlayer();
}
```

---

#### PlayerChatEvent
Fired when a player sends a chat message. **Cancellable.**

```java
public class PlayerChatEvent implements IEvent, ICancellable {
    @Nonnull public Player getPlayer();
    @Nonnull public String getMessage();
    public void setMessage(String message);
    
    // Formatting
    @Nullable public Formatter getFormatter();
    public void setFormatter(@Nullable Formatter formatter);
    
    // ICancellable
    public boolean isCancelled();
    public void setCancelled(boolean cancelled);
}
```

**Example:**
```java
getEventRegistry().register(
    EventPriority.NORMAL,
    PlayerChatEvent.class,
    event -> {
        // Prevent spam
        if (event.getMessage().toLowerCase().contains("spam")) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("§cNo spam allowed!");
            return;
        }
        
        // Format message
        String formatted = "[VIP] " + event.getMessage();
        event.setMessage(formatted);
    }
);
```

---

#### PlayerInteractEvent
Fired when a player interacts with the world. **Cancellable.**

```java
public class PlayerInteractEvent implements IEvent, ICancellable {
    @Nonnull public Player getPlayer();
    @Nonnull public InteractionType getInteractionType();
    @Nonnull public BlockPosition getPosition();
    
    public boolean isCancelled();
    public void setCancelled(boolean cancelled);
}
```

---

#### PlayerReadyEvent
Fired when a player is fully loaded and ready to play.

```java
public class PlayerReadyEvent implements IEvent {
    @Nonnull public Player getPlayer();
}
```

**Use this instead of PlayerConnectEvent for spawning entities, sending messages, etc.**

---

#### AddPlayerToWorldEvent
Fired when a player is added to a world instance.

```java
public class AddPlayerToWorldEvent implements IEvent {
    @Nonnull public Player getPlayer();
    @Nonnull public World getWorld();
}
```

---

#### DrainPlayerFromWorldEvent
Fired when a player is removed from a world instance.

```java
public class DrainPlayerFromWorldEvent implements IEvent {
    @Nonnull public Player getPlayer();
    @Nonnull public World getWorld();
}
```

---

#### PlayerCraftEvent
Fired when a player crafts an item.

```java
public class PlayerCraftEvent implements IEvent {
    @Nonnull public Player getPlayer();
    @Nonnull public Recipe getRecipe();
    @Nonnull public ItemStack getResult();
}
```

---

#### PlayerMouseButtonEvent
Fired when a player presses/releases a mouse button.

```java
public class PlayerMouseButtonEvent implements IEvent {
    @Nonnull public Player getPlayer();
    public int getButton(); // 0=Left, 1=Right, 2=Middle
    public boolean isPressed();
    @Nonnull public MouseAction getAction();
}
```

---

#### PlayerMouseMotionEvent
Fired when a player moves their mouse.

```java
public class PlayerMouseMotionEvent implements IEvent {
    @Nonnull public Player getPlayer();
    public double getDeltaX();
    public double getDeltaY();
    @Nonnull public Vector2d getPosition();
}
```

---

#### PlayerSetupConnectEvent
Fired during player connection setup phase. **Cancellable.**

```java
public class PlayerSetupConnectEvent implements IEvent, ICancellable {
    @Nonnull public ConnectionData getConnectionData();
    @Nonnull public UUID getPlayerUUID();
    @Nullable public String getKickMessage();
    public void setKickMessage(@Nullable String message);
    
    public boolean isCancelled();
    public void setCancelled(boolean cancelled);
}
```

**Example: IP Whitelist**
```java
getEventRegistry().register(
    EventPriority.HIGHEST,
    PlayerSetupConnectEvent.class,
    event -> {
        if (!isIPWhitelisted(event.getConnectionData().getAddress())) {
            event.setCancelled(true);
            event.setKickMessage("§cYou are not whitelisted!");
        }
    }
);
```

---

#### PlayerSetupDisconnectEvent
Fired during player disconnect setup phase.

```java
public class PlayerSetupDisconnectEvent implements IEvent {
    @Nonnull public UUID getPlayerUUID();
    @Nonnull public DisconnectReason getReason();
}
```

---

### Entity Events

Located in `com.hypixel.hytale.server.core.event.events.entity`

#### EntityEvent
Base class for all entity events.

```java
public abstract class EntityEvent implements IEvent {
    @Nonnull public Entity getEntity();
}
```

---

#### EntityRemoveEvent
Fired when an entity is removed from the world.

```java
public class EntityRemoveEvent extends EntityEvent {
    @Nonnull public Entity getEntity();
    @Nonnull public RemoveReason getReason();
}
```

---

#### LivingEntityInventoryChangeEvent
Fired when a living entity's inventory changes.

```java
public class LivingEntityInventoryChangeEvent implements IEvent {
    @Nonnull public LivingEntity getEntity();
    @Nonnull public Inventory getInventory();
    public int getSlot();
    @Nullable public ItemStack getOldItem();
    @Nullable public ItemStack getNewItem();
}
```

---

#### LivingEntityUseBlockEvent
Fired when a living entity uses a block.

```java
public class LivingEntityUseBlockEvent implements IEvent {
    @Nonnull public LivingEntity getEntity();
    @Nonnull public BlockPosition getPosition();
    @Nonnull public BlockState getBlockState();
}
```

---

### ECS (Entity Component System) Events

Located in `com.hypixel.hytale.server.core.event.events.ecs`

#### BreakBlockEvent
Fired when a block is broken. **Cancellable.**

```java
public class BreakBlockEvent implements IEvent, ICancellable {
    @Nonnull public Entity getEntity();
    @Nonnull public BlockPosition getPosition();
    @Nonnull public BlockState getBlockState();
    
    public boolean isCancelled();
    public void setCancelled(boolean cancelled);
}
```

---

#### PlaceBlockEvent
Fired when a block is placed. **Cancellable.**

```java
public class PlaceBlockEvent implements IEvent, ICancellable {
    @Nonnull public Entity getEntity();
    @Nonnull public BlockPosition getPosition();
    @Nonnull public BlockState getBlockState();
    
    public boolean isCancelled();
    public void setCancelled(boolean cancelled);
}
```

---

#### UseBlockEvent
Fired when a block is used/interacted with. **Cancellable.**

```java
public class UseBlockEvent implements IEvent, ICancellable {
    @Nonnull public Entity getEntity();
    @Nonnull public BlockPosition getPosition();
    @Nonnull public BlockState getBlockState();
    @Nonnull public InteractionHand getHand();
    
    public boolean isCancelled();
    public void setCancelled(boolean cancelled);
}
```

**Sub-events:**
- `UseBlockEvent.Pre` - Before interaction
- `UseBlockEvent.Post` - After interaction

---

#### DamageBlockEvent
Fired when a block takes damage.

```java
public class DamageBlockEvent implements IEvent {
    @Nonnull public Entity getEntity();
    @Nonnull public BlockPosition getPosition();
    @Nonnull public BlockState getBlockState();
    public float getDamage();
    public void setDamage(float damage);
}
```

---

#### DropItemEvent
Fired when an item is dropped. **Cancellable.**

```java
public class DropItemEvent implements IEvent, ICancellable {
    @Nonnull public Entity getEntity();
    @Nonnull public ItemStack getItemStack();
    
    public boolean isCancelled();
    public void setCancelled(boolean cancelled);
}
```

**Sub-events:**
- `DropItemEvent.PlayerRequest` - Player intentionally drops item

---

#### InteractivelyPickupItemEvent
Fired when an entity picks up an item. **Cancellable.**

```java
public class InteractivelyPickupItemEvent implements IEvent, ICancellable {
    @Nonnull public Entity getEntity();
    @Nonnull public ItemEntity getItemEntity();
    
    public boolean isCancelled();
    public void setCancelled(boolean cancelled);
}
```

---

#### CraftRecipeEvent
Fired when a recipe is crafted. **Cancellable.**

```java
public class CraftRecipeEvent implements IEvent, ICancellable {
    @Nonnull public Entity getEntity();
    @Nonnull public Recipe getRecipe();
    @Nonnull public ItemStack getResult();
    
    public boolean isCancelled();
    public void setCancelled(boolean cancelled);
}
```

**Sub-events:**
- `CraftRecipeEvent.Pre` - Before crafting
- `CraftRecipeEvent.Post` - After crafting

---

#### SwitchActiveSlotEvent
Fired when a player switches their active hotbar slot.

```java
public class SwitchActiveSlotEvent implements IEvent {
    @Nonnull public Entity getEntity();
    public int getOldSlot();
    public int getNewSlot();
}
```

---

#### ChangeGameModeEvent
Fired when a player's game mode changes.

```java
public class ChangeGameModeEvent implements IEvent {
    @Nonnull public Player getPlayer();
    @Nonnull public GameMode getOldMode();
    @Nonnull public GameMode getNewMode();
}
```

---

#### DiscoverZoneEvent
Fired when a player discovers a new zone/area.

```java
public class DiscoverZoneEvent implements IEvent {
    @Nonnull public Player getPlayer();
    @Nonnull public Zone getZone();
}
```

**Sub-events:**
- `DiscoverZoneEvent.Display` - For UI/notification

---

### Permission Events

Located in `com.hypixel.hytale.server.core.event.events.permissions`

#### PlayerPermissionChangeEvent
Fired when a player's permissions change.

```java
public abstract class PlayerPermissionChangeEvent implements IEvent {
    @Nonnull public Player getPlayer();
    @Nonnull public String getPermission();
}
```

**Sub-events:**
- `PlayerPermissionChangeEvent.PermissionAdded`
- `PlayerPermissionChangeEvent.PermissionRemoved`
- `PlayerPermissionChangeEvent.GroupAdded`
- `PlayerPermissionChangeEvent.GroupRemoved`

---

#### GroupPermissionChangeEvent
Fired when a permission group changes.

```java
public abstract class GroupPermissionChangeEvent implements IEvent {
    @Nonnull public String getGroupName();
    @Nonnull public String getPermission();
}
```

**Sub-events:**
- `GroupPermissionChangeEvent.Added`
- `GroupPermissionChangeEvent.Removed`

---

#### PlayerGroupEvent
Fired when a player is added/removed from a group.

```java
public abstract class PlayerGroupEvent implements IEvent {
    @Nonnull public Player getPlayer();
    @Nonnull public String getGroupName();
}
```

**Sub-events:**
- `PlayerGroupEvent.Added`
- `PlayerGroupEvent.Removed`

---

### System Events

Located in `com.hypixel.hytale.server.core.event.events`

#### BootEvent
Fired when the server boots up.

```java
public class BootEvent implements IEvent {
    // Server startup initialization
}
```

---

#### PrepareUniverseEvent
Fired when the universe is being prepared.

```java
public class PrepareUniverseEvent implements IEvent {
    @Nonnull public Universe getUniverse();
}
```

---

#### ShutdownEvent
Fired when the server is shutting down.

```java
public class ShutdownEvent implements IEvent {
    @Nonnull public ShutdownReason getReason();
}
```

---

## 💡 Best Practices

### Event Handler Guidelines

#### 1. Always Check Cancellation State

```java
getEventRegistry().register(PlayerChatEvent.class, event -> {
    if (event.isCancelled()) {
        return; // Don't process if already cancelled
    }
    
    // Your logic here
});
```

#### 2. Use Appropriate Priorities

```java
// Early: Modify event data
getEventRegistry().register(
    EventPriority.LOW,
    PlayerChatEvent.class,
    event -> event.setMessage("[Formatted] " + event.getMessage())
);

// Late: Logging, analytics
getEventRegistry().register(
    EventPriority.HIGHEST,
    PlayerChatEvent.class,
    event -> logChatMessage(event)
);
```

#### 3. Minimize Event Handler Complexity

```java
// ❌ BAD: Heavy processing in event handler
getEventRegistry().register(PlayerConnectEvent.class, event -> {
    loadPlayerDataFromDatabase(event.getPlayer()); // BLOCKS main thread!
});

// ✅ GOOD: Async processing
getEventRegistry().registerAsync(PlayerConnectEvent.class, event -> 
    CompletableFuture.supplyAsync(() -> {
        return loadPlayerDataFromDatabase(event.getPlayer());
    })
);
```

#### 4. Use Keyed Listeners for Per-Entity Logic

```java
// Per-player cooldown tracker
Map<UUID, Long> cooldowns = new ConcurrentHashMap<>();

public void registerPlayerCooldown(Player player) {
    getEventRegistry().register(
        PlayerInteractEvent.class,
        player.getUniqueId(),
        event -> {
            long lastUse = cooldowns.getOrDefault(player.getUniqueId(), 0L);
            if (System.currentTimeMillis() - lastUse < 5000) {
                event.setCancelled(true);
                return;
            }
            cooldowns.put(player.getUniqueId(), System.currentTimeMillis());
        }
    );
}
```

#### 5. Cleanup Resources

```java
@Override
protected void shutdown() {
    // Plugin registries auto-cleanup, but manual resources need cleanup
    saveData();
    closeConnections();
}
```

---

## 📝 Examples

### Example 1: Welcome Message Plugin

```java
package com.example.welcome;

import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;

public class WelcomePlugin extends JavaPlugin {
    
    public WelcomePlugin(JavaPluginInit init) {
        super(init);
    }
    
    @Override
    protected void setup() {
        getEventRegistry().register(PlayerReadyEvent.class, event -> {
            event.getPlayer().sendMessage("§aWelcome to our server!");
            getLogger().info(event.getPlayer().getName() + " joined the game");
        });
    }
}
```

---

### Example 2: Anti-Spam Plugin

```java
package com.example.antispam;

import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.event.events.player.PlayerChatEvent;
import com.hypixel.hytale.event.EventPriority;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class AntiSpamPlugin extends JavaPlugin {
    
    private final Map<UUID, ChatData> chatData = new ConcurrentHashMap<>();
    
    public AntiSpamPlugin(JavaPluginInit init) {
        super(init);
    }
    
    @Override
    protected void setup() {
        getEventRegistry().register(
            EventPriority.HIGH,
            PlayerChatEvent.class,
            event -> {
                UUID playerId = event.getPlayer().getUniqueId();
                ChatData data = chatData.computeIfAbsent(playerId, k -> new ChatData());
                
                // Check spam
                long now = System.currentTimeMillis();
                if (now - data.lastMessage < 1000) { // 1 second cooldown
                    data.spamCount++;
                    
                    if (data.spamCount > 3) {
                        event.setCancelled(true);
                        event.getPlayer().sendMessage("§cStop spamming!");
                        return;
                    }
                } else {
                    data.spamCount = 0;
                }
                
                data.lastMessage = now;
            }
        );
    }
    
    private static class ChatData {
        long lastMessage = 0;
        int spamCount = 0;
    }
}
```

---

### Example 3: Custom Block Protection

```java
package com.example.protection;

import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.event.events.ecs.BreakBlockEvent;
import com.hypixel.hytale.event.EventPriority;

import java.util.HashSet;
import java.util.Set;

public class ProtectionPlugin extends JavaPlugin {
    
    private final Set<BlockPosition> protectedBlocks = new HashSet<>();
    
    public ProtectionPlugin(JavaPluginInit init) {
        super(init);
    }
    
    @Override
    protected void setup() {
        getEventRegistry().register(
            EventPriority.HIGHEST,
            BreakBlockEvent.class,
            event -> {
                if (protectedBlocks.contains(event.getPosition())) {
                    event.setCancelled(true);
                    
                    if (event.getEntity() instanceof Player) {
                        Player player = (Player) event.getEntity();
                        player.sendMessage("§cThis block is protected!");
                    }
                }
            }
        );
    }
    
    public void protectBlock(BlockPosition position) {
        protectedBlocks.add(position);
        getLogger().info("Protected block at " + position);
    }
}
```

---

### Example 4: Admin Command Blocker

```java
package com.example.commandblocker;

import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.event.events.player.PlayerChatEvent;
import com.hypixel.hytale.event.EventPriority;

import java.util.List;

public class CommandBlockerPlugin extends JavaPlugin {
    
    private final List<String> blockedCommands = List.of(
        "/op", "/deop", "/stop", "/reload"
    );
    
    public CommandBlockerPlugin(JavaPluginInit init) {
        super(init);
    }
    
    @Override
    protected void setup() {
        getEventRegistry().register(
            EventPriority.LOWEST, // Check early
            PlayerChatEvent.class,
            event -> {
                String message = event.getMessage().toLowerCase();
                
                if (!message.startsWith("/")) return;
                
                for (String blocked : blockedCommands) {
                    if (message.startsWith(blocked)) {
                        if (!event.getPlayer().hasPermission("admin.bypass")) {
                            event.setCancelled(true);
                            event.getPlayer().sendMessage("§cYou don't have permission!");
                            getLogger().warning(event.getPlayer().getName() + 
                                " tried to use blocked command: " + message);
                        }
                        break;
                    }
                }
            }
        );
    }
}
```

---

### Example 5: Zone Discovery Announcer

```java
package com.example.zonediscovery;

import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.event.events.ecs.DiscoverZoneEvent;

public class ZoneAnnouncerPlugin extends JavaPlugin {
    
    public ZoneAnnouncerPlugin(JavaPluginInit init) {
        super(init);
    }
    
    @Override
    protected void setup() {
        getEventRegistry().register(
            DiscoverZoneEvent.Display.class,
            event -> {
                String zoneName = event.getZone().getName();
                String playerName = event.getPlayer().getName();
                
                // Broadcast to all players
                broadcastMessage("§e" + playerName + " §7discovered §6" + zoneName + "§7!");
                
                // Reward the discoverer
                event.getPlayer().sendMessage("§a+100 XP for discovering " + zoneName);
            }
        );
    }
    
    private void broadcastMessage(String message) {
        // TODO: Implement server-wide broadcast
        getLogger().info(message);
    }
}
```

---

## 🔜 Coming Soon

The following systems are planned for documentation:

- [ ] **Command System** - Command registration and execution
- [ ] **Task/Scheduler System** - Repeating and delayed tasks
- [ ] **Entity System** - Custom entities and spawning
- [ ] **Component System (ECS)** - Entity components and storage
- [ ] **Block System** - Custom blocks and world manipulation
- [ ] **Player API** - Player management and data
- [ ] **Server API** - World access and server utilities
- [ ] **Permission System** - Permission checks and management
- [ ] **Asset System** - Custom resources and asset packs
- [ ] **Inventory API** - Inventory manipulation
- [ ] **Recipe System** - Custom crafting recipes

---

## 📄 License

This documentation is based on reverse-engineered Hytale Server code and is intended for educational purposes only.

---

## 🤝 Contributing

Found an error or want to add examples? Contributions are welcome!

1. Fork the repository
2. Create a feature branch
3. Submit a pull request

---

## 📮 Contact

- **Author:** Kielian (NadoHimself)
- **Company:** Age of Flair
- **GitHub:** [@NadoHimself](https://github.com/NadoHimself)

---

**Last Updated:** January 14, 2026  
**Status:** Active Development - Based on Hytale Server Alpha