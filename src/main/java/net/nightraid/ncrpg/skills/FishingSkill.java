package net.nightraid.ncrpg.skills;

import net.nightraid.ncrpg.NCRPG;
import net.nightraid.ncrpg.models.SkillType;

/**
 * Fishing Skill Implementation
 * Features: Treasure Hunter (4 tiers), Magic Hunter, Shake, Master Angler, Ice Fishing
 */
public class FishingSkill extends Skill {

    public FishingSkill(NCRPG plugin) {
        super(plugin, SkillType.FISHING);
    }

    /**
     * Get Treasure Hunter tier (1-4) based on level
     */
    public int getTreasureTier(int level) {
        if (level >= 75) return 4;
        if (level >= 50) return 3;
        if (level >= 25) return 2;
        if (isPassiveUnlocked("treasure-hunter", level)) return 1;
        return 0;
    }

    /**
     * Check if Magic Hunter is active (enchanted items)
     */
    public boolean hasMagicHunter(int level) {
        return isPassiveUnlocked("magic-hunter", level) && 
               rollChance(getAbilityChance("magic-hunter", level));
    }

    /**
     * Check if Shake is active (drops from mobs)
     */
    public boolean canShake(int level) {
        return isPassiveUnlocked("shake", level) && 
               rollChance(getAbilityChance("shake", level));
    }

    /**
     * Get fishing time reduction percentage (Master Angler)
     */
    public double getTimeReduction(int level) {
        if (!isPassiveUnlocked("master-angler", level)) return 0.0;
        
        double maxReduction = plugin.getConfigManager().getDouble(
            "skills.fishing.abilities.master-angler.max-reduction", 0.5
        );
        
        int maxLevel = plugin.getConfigManager().getInt(
            "skills.fishing.abilities.master-angler.max-level", 100
        );
        
        return Math.min(maxReduction, (level / (double) maxLevel) * maxReduction);
    }

    /**
     * Check if Ice Fishing is unlocked
     */
    public boolean hasIceFishing(int level) {
        return isPassiveUnlocked("ice-fishing", level);
    }

    /**
     * Get XP for catching fish
     */
    public int getXPForFish() {
        return getXPForAction("catch");
    }
}
