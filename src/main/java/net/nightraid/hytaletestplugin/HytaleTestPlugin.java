package net.nightraid.hytaletestplugin;

import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.plugin.HytalePlugin;
import com.hypixel.hytale.event.IEventDispatcher;
import net.nightraid.hytaletestplugin.listeners.PlayerListener;
import net.nightraid.hytaletestplugin.listeners.BlockListener;

public class HytaleTestPlugin extends HytalePlugin {

    @Override
    public void onEnable() {
        getLogger().info("HytaleTestPlugin enabled!");
        
        // Register Event Listeners
        registerEvents();
    }

    @Override
    public void onDisable() {
        getLogger().info("HytaleTestPlugin disabled!");
    }

    private void registerEvents() {
        HytaleServer server = HytaleServer.get();
        
        // Register Player Events
        PlayerListener playerListener = new PlayerListener(this);
        
        // AddPlayerToWorldEvent (PlayerJoinEvent equivalent)
        IEventDispatcher dispatcher = server.getEventBus()
            .dispatchFor(com.hypixel.hytale.server.core.event.events.player.AddPlayerToWorldEvent.class, "*");
        if (dispatcher.hasListener()) {
            dispatcher.register(playerListener::onPlayerJoin);
        }
        
        // PlayerDisconnectEvent (PlayerQuitEvent equivalent)
        dispatcher = server.getEventBus()
            .dispatchFor(com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent.class, "*");
        if (dispatcher.hasListener()) {
            dispatcher.register(playerListener::onPlayerDisconnect);
        }
        
        // PlayerChatEvent
        dispatcher = server.getEventBus()
            .dispatchFor(com.hypixel.hytale.server.core.event.events.player.PlayerChatEvent.class, "*");
        if (dispatcher.hasListener()) {
            dispatcher.register(playerListener::onPlayerChat);
        }
        
        // Register Block Events
        BlockListener blockListener = new BlockListener(this);
        
        // Note: ECS Events werden über ComponentAccessor.invoke() gefeuert
        // Für ECS Events brauchst du ein System oder einen Entity-Listener
        
        getLogger().info("Event listeners registered!");
    }
}
