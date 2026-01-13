package net.nightraid.ncrpg.listeners;

import com.hypixel.hytale.event.EventHandler;
import com.hypixel.hytale.event.Listener;
import com.hypixel.hytale.event.player.PlayerJoinEvent;
import com.hypixel.hytale.entity.player.Player;

import net.nightraid.ncrpg.NCRPG;
import net.nightraid.ncrpg.managers.PlayerDataManager;
import net.nightraid.ncrpg.models.PlayerData;
import net.nightraid.ncrpg.models.SkillType;

/**
 * Handles player join events - loads player data from database
 */
public class PlayerJoinListener implements Listener {
    private final NCRPG plugin;
    private final PlayerDataManager playerDataManager;

    public PlayerJoinListener(NCRPG plugin) {
        this.plugin = plugin;
        this.playerDataManager = plugin.getPlayerDataManager();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        
        // Load player data asynchronously
        playerDataManager.loadPlayerData(player.getUniqueId(), player.getName())
            .thenAccept(data -> {
                if (data != null) {
                    NCRPG.getPluginLogger().info("Loaded data for player: " + player.getName());
                    
                    // Update level caps based on permissions
                    updateLevelCaps(player, data);
                    
                    // Send welcome message
                    player.sendMessage("§a[NCRPG] §7Welcome! Your skill data has been loaded.");
                } else {
                    NCRPG.getPluginLogger().warn("Failed to load data for player: " + player.getName());
                    player.sendMessage("§c[NCRPG] §7Failed to load your skill data. Contact an admin!");
                }
            })
            .exceptionally(ex -> {
                NCRPG.getPluginLogger().error("Error loading player data: " + ex.getMessage(), ex);
                player.sendMessage("§c[NCRPG] §7An error occurred while loading your data.");
                return null;
            });
    }

    /**
     * Update skill level caps based on player permissions
     */
    private void updateLevelCaps(Player player, PlayerData data) {
        for (SkillType skillType : SkillType.values()) {
            int levelCap = getPlayerLevelCap(player, skillType);
            data.getSkillData(skillType).setSkillCap(levelCap);
        }
    }

    /**
     * Get the maximum level cap for a skill based on permissions
     * Format: ncrpg.{skill}.cap.{level}
     */
    private int getPlayerLevelCap(Player player, SkillType skillType) {
        String skillName = skillType.name().toLowerCase();
        
        // Check permissions from highest to lowest
        for (int level = 1000; level >= 0; level -= 10) {
            if (player.hasPermission("ncrpg." + skillName + ".cap." + level)) {
                return level;
            }
        }
        
        // Default cap from config
        return plugin.getConfigManager().getInt(
            "skills." + skillType.name().toLowerCase() + ".default-cap", 100
        );
    }
}
