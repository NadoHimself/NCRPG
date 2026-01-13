package net.nightraid.ncrpg.listeners;

import com.hypixel.hytale.event.EventHandler;
import com.hypixel.hytale.event.Listener;
import com.hypixel.hytale.event.player.PlayerQuitEvent;
import com.hypixel.hytale.entity.player.Player;

import net.nightraid.ncrpg.NCRPG;
import net.nightraid.ncrpg.managers.PlayerDataManager;

/**
 * Handles player quit events - saves player data to database
 */
public class PlayerQuitListener implements Listener {
    private final NCRPG plugin;
    private final PlayerDataManager playerDataManager;

    public PlayerQuitListener(NCRPG plugin) {
        this.plugin = plugin;
        this.playerDataManager = plugin.getPlayerDataManager();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        
        NCRPG.getPluginLogger().info("Saving data for player: " + player.getName());
        
        // Save player data synchronously to ensure it completes before disconnect
        try {
            playerDataManager.savePlayerData(player.getUniqueId())
                .thenRun(() -> {
                    NCRPG.getPluginLogger().info("Successfully saved data for: " + player.getName());
                    
                    // Unload player data from cache
                    playerDataManager.unloadPlayerData(player.getUniqueId());
                })
                .exceptionally(ex -> {
                    NCRPG.getPluginLogger().error("Failed to save data for " + player.getName(), ex);
                    return null;
                })
                .join(); // Wait for completion
        } catch (Exception e) {
            NCRPG.getPluginLogger().error("Error saving player data on quit: " + e.getMessage(), e);
        }
    }
}
