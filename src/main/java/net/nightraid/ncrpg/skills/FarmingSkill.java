package net.nightraid.ncrpg.skills;

import net.nightraid.ncrpg.NCRPG;
import net.nightraid.ncrpg.models.SkillType;

/**
 * Farming Skill Implementation
 * Features: Triple drops, double drops, Green Terra ability, Hylian Luck
 */
public class FarmingSkill extends Skill {

    public FarmingSkill(NCRPG plugin) {
        super(plugin, SkillType.FARMING);
    }

    /**
     * Check if player should receive triple drops
     */
    public boolean shouldTripleDrops(int level) {
        double chance = getAbilityChance("triple-drops", level);
        return rollChance(chance);
    }

    /**
     * Check if player should receive double drops
     */
    public boolean shouldDoubleDrops(int level) {
        double chance = getAbilityChance("double-drops", level);
        return rollChance(chance);
    }

    /**
     * Calculate final drop multiplier
     */
    public int getDropMultiplier(int level) {
        if (shouldTripleDrops(level)) return 3;
        if (shouldDoubleDrops(level)) return 2;
        return 1;
    }

    /**
     * Check if Hylian Luck is active (rare drops from crops)
     */
    public boolean hasHylianLuck(int level) {
        return isPassiveUnlocked("hylian-luck", level) && 
               rollChance(getAbilityChance("hylian-luck", level));
    }

    /**
     * Get XP for harvesting a specific crop
     */
    public int getXPForCrop(String cropType) {
        return getXPForAction(cropType.toLowerCase());
    }
}
