package net.nightraid.ncrpg.skills;

import net.nightraid.ncrpg.NCRPG;
import net.nightraid.ncrpg.models.SkillType;

/**
 * Archery Skill Implementation
 * Features: Daze, arrow retrieval, damage scaling
 */
public class ArcherySkill extends Skill {

    public ArcherySkill(NCRPG plugin) {
        super(plugin, SkillType.ARCHERY);
    }

    /**
     * Get damage bonus multiplier
     */
    public double getDamageBonus(int level) {
        return calculateDamageBonus(level);
    }

    /**
     * Check if Daze should apply (enemy slowness)
     */
    public boolean shouldDaze(int level) {
        double chance = getAbilityChance("daze", level);
        return rollChance(chance);
    }

    /**
     * Get Daze duration in ticks
     */
    public int getDazeDuration(int level) {
        int baseDuration = plugin.getConfigManager().getInt(
            "skills.archery.abilities.daze.base-duration", 40
        );
        int perLevelIncrease = plugin.getConfigManager().getInt(
            "skills.archery.abilities.daze.duration-per-level", 1
        );
        return baseDuration + (level * perLevelIncrease);
    }

    /**
     * Check if arrow should be retrieved
     */
    public boolean shouldRetrieveArrow(int level) {
        double chance = getAbilityChance("arrow-retrieval", level);
        return rollChance(chance);
    }

    /**
     * Get distance bonus multiplier (extra damage at long range)
     */
    public double getDistanceBonus(double distance) {
        double maxDistance = plugin.getConfigManager().getDouble(
            "skills.archery.max-distance-bonus-range", 50.0
        );
        double maxBonus = plugin.getConfigManager().getDouble(
            "skills.archery.max-distance-bonus", 1.5
        );
        
        if (distance >= maxDistance) return maxBonus;
        return 1.0 + ((distance / maxDistance) * (maxBonus - 1.0));
    }

    /**
     * Get XP for hitting with arrow
     */
    public int getXPForHit() {
        return getXPForAction("hit");
    }
}
