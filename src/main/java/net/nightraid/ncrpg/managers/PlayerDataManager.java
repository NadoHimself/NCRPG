package net.nightraid.ncrpg.managers;

import net.nightraid.ncrpg.NCRPG;
import net.nightraid.ncrpg.models.PlayerSkillData;
import net.nightraid.ncrpg.models.SkillType;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manager for player data caching and loading/saving
 */
public class PlayerDataManager {

    private final NCRPG plugin;
    private final Map<UUID, Map<SkillType, PlayerSkillData>> cache;

    public PlayerDataManager(NCRPG plugin) {
        this.plugin = plugin;
        this.cache = new ConcurrentHashMap<>();
    }

    /**
     * Load player data asynchronously
     */
    public CompletableFuture<Void> loadPlayerData(UUID uuid, String playerName) {
        return CompletableFuture.runAsync(() -> {
            // Load from database
            Map<SkillType, PlayerSkillData> skillData = plugin.getDatabaseManager().loadPlayerSkills(uuid);

            // If new player, initialize all skills
            if (skillData.isEmpty()) {
                skillData = new HashMap<>();
                for (SkillType skill : SkillType.values()) {
                    skillData.put(skill, new PlayerSkillData(uuid, skill));
                }
                
                // Save new player to database
                plugin.getDatabaseManager().savePlayer(uuid, playerName);
            }

            // Cache the data
            cache.put(uuid, skillData);

            plugin.getLogger().info("Loaded data for player: " + playerName);
        });
    }

    /**
     * Save player data asynchronously
     */
    public CompletableFuture<Void> savePlayerData(UUID uuid) {
        Map<SkillType, PlayerSkillData> skillData = cache.get(uuid);
        if (skillData == null) {
            return CompletableFuture.completedFuture(null);
        }

        return CompletableFuture.runAsync(() -> {
            for (PlayerSkillData data : skillData.values()) {
                plugin.getDatabaseManager().saveSkillData(data);
            }
        });
    }

    /**
     * Save all cached player data
     */
    public void saveAll() {
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (UUID uuid : cache.keySet()) {
            futures.add(savePlayerData(uuid));
        }
        
        // Wait for all saves to complete
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    }

    /**
     * Unload player data from cache
     */
    public void unloadPlayerData(UUID uuid) {
        cache.remove(uuid);
    }

    /**
     * Get skill data for player
     */
    public PlayerSkillData getSkillData(UUID uuid, SkillType skillType) {
        Map<SkillType, PlayerSkillData> skillData = cache.get(uuid);
        if (skillData == null) {
            return null;
        }
        return skillData.get(skillType);
    }

    /**
     * Get all skill data for player
     */
    public Map<SkillType, PlayerSkillData> getAllSkillData(UUID uuid) {
        return cache.getOrDefault(uuid, new HashMap<>());
    }

    /**
     * Update skill cap based on permissions
     * NOTE: Replace with Hytale API permission checks
     */
    public void updateSkillCaps(UUID uuid) {
        Map<SkillType, PlayerSkillData> skillData = cache.get(uuid);
        if (skillData == null) {
            return;
        }

        // NOTE: Replace with Hytale API
        // Player player = Bukkit.getPlayer(uuid);
        // if (player == null) return;

        for (SkillType skill : SkillType.values()) {
            int cap = 100; // Default cap

            // Check permissions for higher caps
            // for (int i = 1000; i >= 100; i -= 100) {
            //     if (player.hasPermission("ncrpg." + skill.name().toLowerCase() + ".cap." + i)) {
            //         cap = i;
            //         break;
            //     }
            // }

            PlayerSkillData data = skillData.get(skill);
            if (data != null) {
                data.setSkillCap(cap);
            }
        }
    }

    /**
     * Check if player data is loaded
     */
    public boolean isLoaded(UUID uuid) {
        return cache.containsKey(uuid);
    }
}
