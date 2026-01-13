package net.nightraid.hytaletestplugin.listeners;

import com.hypixel.hytale.component.Holder;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.event.IBaseEvent;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.player.AddPlayerToWorldEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerChatEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import net.nightraid.hytaletestplugin.HytaleTestPlugin;

import javax.annotation.Nonnull;

public class PlayerListener {
    
    private final HytaleTestPlugin plugin;
    
    public PlayerListener(HytaleTestPlugin plugin) {
        this.plugin = plugin;
    }
    
    /**
     * Called when a player joins the world
     * This is the equivalent of Bukkit's PlayerJoinEvent
     */
    public void onPlayerJoin(@Nonnull IBaseEvent baseEvent) {
        if (!(baseEvent instanceof AddPlayerToWorldEvent)) return;
        
        AddPlayerToWorldEvent event = (AddPlayerToWorldEvent) baseEvent;
        Holder<EntityStore> holder = event.getHolder();
        World world = event.getWorld();
        
        // Get Player component
        Player player = holder.getComponent(Player.getComponentType());
        if (player == null) return;
        
        String playerName = player.getPlayerRef().getUsername();
        plugin.getLogger().info(playerName + " joined the world: " + world.getName());
        
        // Send welcome message
        player.sendMessage(Message.literal("Welcome to the server, " + playerName + "!"));
        
        // Broadcast join message (if enabled in event)
        if (event.shouldBroadcastJoinMessage()) {
            world.broadcast(Message.literal(playerName + " joined the game"));
        }
    }
    
    /**
     * Called when a player disconnects
     * This is the equivalent of Bukkit's PlayerQuitEvent
     */
    public void onPlayerDisconnect(@Nonnull IBaseEvent baseEvent) {
        if (!(baseEvent instanceof PlayerDisconnectEvent)) return;
        
        PlayerDisconnectEvent event = (PlayerDisconnectEvent) baseEvent;
        PlayerRef playerRef = event.getPlayerRef();
        
        String playerName = playerRef.getUsername();
        String reason = event.getDisconnectReason().toString();
        
        plugin.getLogger().info(playerName + " disconnected. Reason: " + reason);
    }
    
    /**
     * Called when a player sends a chat message
     */
    public void onPlayerChat(@Nonnull IBaseEvent baseEvent) {
        if (!(baseEvent instanceof PlayerChatEvent)) return;
        
        PlayerChatEvent event = (PlayerChatEvent) baseEvent;
        PlayerRef playerRef = event.getPlayerRef();
        String message = event.getMessage();
        
        plugin.getLogger().info("[CHAT] " + playerRef.getUsername() + ": " + message);
        
        // Example: Cancel messages containing "badword"
        if (message.toLowerCase().contains("badword")) {
            event.setCancelled(true);
            playerRef.sendMessage(Message.literal("§cYour message was blocked!"));
        }
    }
}
