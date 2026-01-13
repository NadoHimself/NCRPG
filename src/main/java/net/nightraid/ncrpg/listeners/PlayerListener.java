package net.nightraid.ncrpg.listeners;

import com.hypixel.hytale.server.core.event.Subscribe;
import com.hypixel.hytale.server.core.event.events.player.PlayerConnectEvent;
import net.nightraid.ncrpg.NCRPGPlugin;

public class PlayerListener {
    
    private final NCRPGPlugin plugin;
    
    public PlayerListener(NCRPGPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Subscribe
    public void onPlayerConnect(PlayerConnectEvent event) {
        plugin.getLogger().info("[NCRPG] Spieler verbunden: " + event.getPlayer().getName());
    }
}
