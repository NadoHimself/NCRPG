package net.nightraid.ncrpg.skills;

import net.nightraid.ncrpg.NCRPG;
import net.nightraid.ncrpg.models.SkillType;

/**
 * Mining Skill Implementation
 * Features: Double Drops (25+ level), Super Breaker ability, mining speed multiplier
 */
public class MiningSkill extends Skill {

    public MiningSkill(NCRPG plugin) {
        super(plugin, SkillType.MINING);
    }

    /**
     * Check if player should receive double drops
     */
    public boolean shouldDoubleDrops(int level) {
        if (level < 25) return false;
        double chance = getAbilityChance("double-drops", level);
        return rollChance(chance);
    }

    /**
     * Get mining speed multiplier based on level
     */
    public double getMiningSpeedMultiplier(int level) {
        double baseMultiplier = 1.0;
        double perLevelBonus = plugin.getConfigManager().getDouble(
            "skills.mining.speed-bonus-per-level", 0.01
        );
        return baseMultiplier + (level * perLevelBonus);
    }

    /**
     * Check if Super Breaker is active and get its multiplier
     */
    public double getSuperBreakerMultiplier() {
        // Super Breaker provides 3x mining speed
        return 3.0;
    }

    /**
     * Get XP for mining a specific block type
     */
    public int getXPForBlock(String blockType) {
        return getXPForAction(blockType.toLowerCase());
    }
}
