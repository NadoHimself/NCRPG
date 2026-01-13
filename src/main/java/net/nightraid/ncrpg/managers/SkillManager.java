package net.nightraid.ncrpg.managers;

import net.nightraid.ncrpg.NCRPG;
import net.nightraid.ncrpg.models.PlayerSkillData;
import net.nightraid.ncrpg.models.SkillType;

import java.util.UUID;

/**
 * Manager for skill XP, leveling, and progression
 */
public class SkillManager {

    private final NCRPG plugin;

    public SkillManager(NCRPG plugin) {
        this.plugin = plugin;
    }

    /**
     * Add XP to a player's skill
     * @param uuid Player UUID
     * @param skillType Skill type
     * @param amount XP amount to add
     */
    public void addXP(UUID uuid, SkillType skillType, double amount) {
        // Apply global XP multiplier
        double multiplier = plugin.getConfigManager().getDouble("general.xp-multiplier", 1.0);
        amount *= multiplier;

        // Get player data
        PlayerDataManager dataManager = plugin.getPlayerDataManager();
        PlayerSkillData skillData = dataManager.getSkillData(uuid, skillType);

        if (skillData == null) {
            return;
        }

        // Add XP
        double oldXP = skillData.getXp();
        skillData.addXp(amount);

        // Check for level up
        double baseXP = plugin.getConfigManager().getDouble("general.xp-base-per-level", 1000.0);
        double exponent = plugin.getConfigManager().getDouble("general.xp-exponent", 1.5);

        int levelsGained = 0;
        while (skillData.canLevelUp(baseXP, exponent)) {
            levelUp(uuid, skillType, skillData, baseXP, exponent);
            levelsGained++;
        }

        // Send progress message if XP was gained but no level up
        if (levelsGained == 0 && amount > 0) {
            sendProgressMessage(uuid, skillType, amount, skillData, baseXP, exponent);
        }
    }

    /**
     * Handle skill level up
     */
    private void levelUp(UUID uuid, SkillType skillType, PlayerSkillData skillData, double baseXP, double exponent) {
        int oldLevel = skillData.getLevel();
        int newLevel = oldLevel + 1;

        // Deduct XP required for level up
        double required = skillData.getXpForNextLevel(baseXP, exponent);
        skillData.setXp(skillData.getXp() - required);
        skillData.setLevel(newLevel);

        // Send level up message
        sendLevelUpMessage(uuid, skillType, newLevel);

        // Check for milestone
        int milestoneInterval = plugin.getConfigManager().getInt("general.milestone-interval", 10);
        if (newLevel % milestoneInterval == 0) {
            sendMilestoneMessage(uuid, skillType, newLevel);
        }

        // Check for ability unlocks
        checkAbilityUnlocks(uuid, skillType, newLevel);
    }

    /**
     * Send XP progress message to player
     */
    private void sendProgressMessage(UUID uuid, SkillType skillType, double amount, PlayerSkillData skillData, double baseXP, double exponent) {
        // NOTE: Replace with Hytale API player messaging
        // Player player = Bukkit.getPlayer(uuid);
        // if (player != null) {
        //     double progress = skillData.getProgressPercent(baseXP, exponent);
        //     player.sendMessage(String.format("§a+%.1f %s XP (%.1f%%)", amount, skillType.getDisplayName(), progress));
        // }
    }

    /**
     * Send level up message to player
     */
    private void sendLevelUpMessage(UUID uuid, SkillType skillType, int newLevel) {
        // NOTE: Replace with Hytale API player messaging
        // Player player = Bukkit.getPlayer(uuid);
        // if (player != null) {
        //     player.sendMessage("§6§l✦ §e§lLEVEL UP! §6§l✦");
        //     player.sendMessage(String.format("§7%s §8» §a%d", skillType.getDisplayName(), newLevel));
        //     player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
        // }
    }

    /**
     * Send milestone message to player
     */
    private void sendMilestoneMessage(UUID uuid, SkillType skillType, int level) {
        // NOTE: Replace with Hytale API player messaging
        // Player player = Bukkit.getPlayer(uuid);
        // if (player != null) {
        //     player.sendMessage("§6§l★ §e§lMILESTONE REACHED! §6§l★");
        //     player.sendMessage(String.format("§7%s Level §a%d", skillType.getDisplayName(), level));
        // }
    }

    /**
     * Check for and announce ability unlocks
     */
    private void checkAbilityUnlocks(UUID uuid, SkillType skillType, int level) {
        // NOTE: Replace with Hytale API player messaging
        // This would check config for ability unlock levels and notify player
        // Example:
        // if (level == plugin.getConfigManager().getInt(skillType.getConfigPath() + ".double-drop.unlock-level", 0)) {
        //     player.sendMessage("§a§lABILITY UNLOCKED! §7Double Drop");
        // }
    }

    /**
     * Get skill level for player
     */
    public int getLevel(UUID uuid, SkillType skillType) {
        PlayerSkillData data = plugin.getPlayerDataManager().getSkillData(uuid, skillType);
        return data != null ? data.getLevel() : 1;
    }

    /**
     * Get total power level (sum of all skill levels)
     */
    public int getPowerLevel(UUID uuid) {
        int total = 0;
        for (SkillType skill : SkillType.values()) {
            total += getLevel(uuid, skill);
        }
        return total;
    }

    /**
     * Calculate chance percentage based on level
     * @param level Current skill level
     * @param unlockLevel Level required to unlock
     * @param maxChance Maximum chance percentage
     * @param maxLevel Level at which max chance is reached
     * @return Chance percentage (0-maxChance)
     */
    public double calculateChance(int level, int unlockLevel, double maxChance, int maxLevel) {
        if (level < unlockLevel) {
            return 0.0;
        }
        if (level >= maxLevel) {
            return maxChance;
        }
        
        // Linear scaling from unlock to max
        double levelsToMax = maxLevel - unlockLevel;
        double levelsGained = level - unlockLevel;
        return (levelsGained / levelsToMax) * maxChance;
    }
}
