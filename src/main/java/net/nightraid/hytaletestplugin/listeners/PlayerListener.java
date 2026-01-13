package net.nightraid.hytaletestplugin.listeners;

import com.hypixel.hytale.server.core.event.Subscribe;
import com.hypixel.hytale.server.core.event.events.player.PlayerConnectEvent;
import net.nightraid.hytaletestplugin.HytaleTestPlugin;

public class PlayerListener {
    
    private final HytaleTestPlugin plugin;
    
    public PlayerListener(HytaleTestPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Subscribe
    public void onPlayerConnect(PlayerConnectEvent event) {
        plugin.getLogger().info("Spieler verbunden: " + event.getPlayer().getName());
    }
}
