package net.nightraid.ncrpg.skills;

import net.nightraid.ncrpg.NCRPG;
import net.nightraid.ncrpg.models.SkillType;

/**
 * Abstract base class for all skills
 * Provides common functionality for XP calculation, level progression, and ability checks
 */
public abstract class Skill {
    protected final NCRPG plugin;
    protected final SkillType skillType;

    public Skill(NCRPG plugin, SkillType skillType) {
        this.plugin = plugin;
        this.skillType = skillType;
    }

    public SkillType getSkillType() {
        return skillType;
    }

    /**
     * Calculate XP required for a specific level
     */
    public int getXPRequiredForLevel(int level) {
        double baseXP = plugin.getConfigManager().getDouble("xp.base-per-level", 1000.0);
        double exponent = plugin.getConfigManager().getDouble("xp.exponent", 1.5);
        return (int) (baseXP * Math.pow(level, exponent));
    }

    /**
     * Check if a passive ability is unlocked at current level
     */
    public boolean isPassiveUnlocked(String abilityName, int level) {
        int unlockLevel = plugin.getConfigManager().getInt(
            "skills." + skillType.name().toLowerCase() + ".abilities." + abilityName + ".unlock-level", 1
        );
        return level >= unlockLevel;
    }

    /**
     * Get the chance percentage for an ability based on level
     */
    public double getAbilityChance(String abilityName, int level) {
        if (!isEnabled()) return 0.0;
        
        int unlockLevel = plugin.getConfigManager().getInt(
            "skills." + skillType.name().toLowerCase() + ".abilities." + abilityName + ".unlock-level", 1
        );
        
        if (level < unlockLevel) return 0.0;
        
        double maxChance = plugin.getConfigManager().getDouble(
            "skills." + skillType.name().toLowerCase() + ".abilities." + abilityName + ".max-chance", 100.0
        );
        
        int maxLevel = plugin.getConfigManager().getInt(
            "skills." + skillType.name().toLowerCase() + ".abilities." + abilityName + ".max-level", 100
        );
        
        // Linear scaling from unlock level to max level
        if (level >= maxLevel) return maxChance;
        
        double progression = (double)(level - unlockLevel) / (maxLevel - unlockLevel);
        return maxChance * progression;
    }

    /**
     * Get XP reward for an action
     */
    public int getXPForAction(String actionType) {
        if (!isEnabled()) return 0;
        
        double multiplier = plugin.getConfigManager().getDouble("xp.multiplier", 1.0);
        int baseXP = plugin.getConfigManager().getInt(
            "skills." + skillType.name().toLowerCase() + ".xp." + actionType, 10
        );
        
        return (int) (baseXP * multiplier);
    }

    /**
     * Check if this skill is enabled
     */
    public boolean isEnabled() {
        return plugin.getConfigManager().getBoolean(
            "skills." + skillType.name().toLowerCase() + ".enabled", true
        );
    }

    /**
     * Get ability duration in ticks
     */
    public int getAbilityDuration(String abilityName) {
        return plugin.getConfigManager().getInt(
            "skills." + skillType.name().toLowerCase() + ".abilities." + abilityName + ".duration", 600
        );
    }

    /**
     * Get ability cooldown in ticks
     */
    public int getAbilityCooldown(String abilityName) {
        return plugin.getConfigManager().getInt(
            "skills." + skillType.name().toLowerCase() + ".abilities." + abilityName + ".cooldown", 1200
        );
    }

    /**
     * Calculate damage bonus based on level
     */
    protected double calculateDamageBonus(int level) {
        double bonusPerLevel = plugin.getConfigManager().getDouble(
            "skills." + skillType.name().toLowerCase() + ".damage-bonus-per-level", 0.1
        );
        return 1.0 + (level * bonusPerLevel);
    }

    /**
     * Roll a random chance check
     */
    protected boolean rollChance(double percentage) {
        return Math.random() * 100.0 < percentage;
    }
}
