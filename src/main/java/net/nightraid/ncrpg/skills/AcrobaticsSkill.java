package net.nightraid.ncrpg.skills;

import net.nightraid.ncrpg.NCRPG;
import net.nightraid.ncrpg.models.SkillType;

/**
 * Acrobatics Skill Implementation
 * Features: Roll, Graceful Roll (sneaking), combat dodge, 95% max fall damage reduction
 */
public class AcrobaticsSkill extends Skill {

    public AcrobaticsSkill(NCRPG plugin) {
        super(plugin, SkillType.ACROBATICS);
    }

    /**
     * Check if player should negate fall damage with Roll
     */
    public boolean shouldRoll(int level, boolean isSneaking) {
        String abilityName = isSneaking ? "graceful-roll" : "roll";
        double chance = getAbilityChance(abilityName, level);
        return rollChance(chance);
    }

    /**
     * Calculate fall damage reduction percentage
     */
    public double getFallDamageReduction(int level) {
        double maxReduction = plugin.getConfigManager().getDouble(
            "skills.acrobatics.max-fall-damage-reduction", 0.95
        );
        
        int maxLevel = plugin.getConfigManager().getInt(
            "skills.acrobatics.max-reduction-level", 100
        );
        
        return Math.min(maxReduction, (level / (double) maxLevel) * maxReduction);
    }

    /**
     * Check if player should dodge an attack
     */
    public boolean shouldDodge(int level) {
        double chance = getAbilityChance("dodge", level);
        return rollChance(chance);
    }

    /**
     * Get XP for taking fall damage
     */
    public int getXPForFall(double damage) {
        int xpPerDamage = plugin.getConfigManager().getInt(
            "skills.acrobatics.xp.fall-per-damage", 10
        );
        return (int) (damage * xpPerDamage);
    }
}
