package net.nightraid.ncrpg.skills;

import net.nightraid.ncrpg.NCRPG;
import net.nightraid.ncrpg.models.SkillType;

/**
 * Herbalism Skill Implementation
 * Features: Green Thumb (auto-replant), double drops, Shroom Thumb
 */
public class HerbalismSkill extends Skill {

    public HerbalismSkill(NCRPG plugin) {
        super(plugin, SkillType.HERBALISM);
    }

    /**
     * Check if Green Thumb should auto-replant
     */
    public boolean hasGreenThumb(int level) {
        return isPassiveUnlocked("green-thumb", level) && 
               rollChance(getAbilityChance("green-thumb", level));
    }

    /**
     * Check if player should receive double drops
     */
    public boolean shouldDoubleDrops(int level) {
        double chance = getAbilityChance("double-drops", level);
        return rollChance(chance);
    }

    /**
     * Check if Shroom Thumb is active (mushroom spreading)
     */
    public boolean hasShroomThumb(int level) {
        return isPassiveUnlocked("shroom-thumb", level) && 
               rollChance(getAbilityChance("shroom-thumb", level));
    }

    /**
     * Get Hylian Luck chance (rare drops)
     */
    public boolean hasHylianLuck(int level) {
        return isPassiveUnlocked("hylian-luck", level) && 
               rollChance(getAbilityChance("hylian-luck", level));
    }

    /**
     * Get XP for harvesting plants
     */
    public int getXPForPlant(String plantType) {
        return getXPForAction(plantType.toLowerCase());
    }
}
