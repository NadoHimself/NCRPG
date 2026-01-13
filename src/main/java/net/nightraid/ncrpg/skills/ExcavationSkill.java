package net.nightraid.ncrpg.skills;

import net.nightraid.ncrpg.NCRPG;
import net.nightraid.ncrpg.models.SkillType;

/**
 * Excavation Skill Implementation
 * Features: Giga Drill Breaker, Archaeology with 4 treasure tiers
 */
public class ExcavationSkill extends Skill {

    public ExcavationSkill(NCRPG plugin) {
        super(plugin, SkillType.EXCAVATION);
    }

    /**
     * Get Archaeology tier (1-4) based on level
     */
    public int getArchaeologyTier(int level) {
        if (level >= 75) return 4;
        if (level >= 50) return 3;
        if (level >= 25) return 2;
        if (isPassiveUnlocked("archaeology", level)) return 1;
        return 0;
    }

    /**
     * Check if player should find treasure
     */
    public boolean shouldFindTreasure(int level) {
        double chance = getAbilityChance("archaeology", level);
        return rollChance(chance);
    }

    /**
     * Get Giga Drill Breaker multiplier
     */
    public double getGigaDrillBreakerMultiplier() {
        return plugin.getConfigManager().getDouble(
            "skills.excavation.abilities.giga-drill-breaker.multiplier", 3.0
        );
    }

    /**
     * Check if triple drops should occur during Giga Drill Breaker
     */
    public boolean shouldTripleDrops(int level) {
        double chance = getAbilityChance("triple-drops", level);
        return rollChance(chance);
    }

    /**
     * Get XP for excavating a specific block type
     */
    public int getXPForBlock(String blockType) {
        return getXPForAction(blockType.toLowerCase());
    }
}
