package net.nightraid.ncrpg.skills;

import net.nightraid.ncrpg.NCRPG;
import net.nightraid.ncrpg.models.SkillType;

/**
 * Combat Skill Implementation
 * Features: General combat damage bonuses
 */
public class CombatSkill extends Skill {

    public CombatSkill(NCRPG plugin) {
        super(plugin, SkillType.COMBAT);
    }

    /**
     * Get damage bonus multiplier for all combat
     */
    public double getDamageBonus(int level) {
        return calculateDamageBonus(level);
    }

    /**
     * Get damage reduction when taking damage
     */
    public double getDamageReduction(int level) {
        double maxReduction = plugin.getConfigManager().getDouble(
            "skills.combat.max-damage-reduction", 0.2
        );
        
        int maxLevel = plugin.getConfigManager().getInt(
            "skills.combat.max-reduction-level", 100
        );
        
        return Math.min(maxReduction, (level / (double) maxLevel) * maxReduction);
    }

    /**
     * Get XP for dealing damage
     */
    public int getXPForDamage(double damage) {
        int xpPerDamage = plugin.getConfigManager().getInt(
            "skills.combat.xp.per-damage", 5
        );
        return (int) (damage * xpPerDamage);
    }
}
