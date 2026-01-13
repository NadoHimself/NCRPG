package net.nightraid.ncrpg.skills;

import net.nightraid.ncrpg.NCRPG;
import net.nightraid.ncrpg.models.SkillType;

/**
 * Swords Skill Implementation
 * Features: Rupture (bleed), Counter Attack, damage scaling
 */
public class SwordsSkill extends Skill {

    public SwordsSkill(NCRPG plugin) {
        super(plugin, SkillType.SWORDS);
    }

    /**
     * Get damage bonus multiplier
     */
    public double getDamageBonus(int level) {
        return calculateDamageBonus(level);
    }

    /**
     * Check if Rupture (bleed) should apply
     */
    public boolean shouldRupture(int level) {
        double chance = getAbilityChance("rupture", level);
        return rollChance(chance);
    }

    /**
     * Get Rupture bleed duration in ticks
     */
    public int getRuptureDuration(int level) {
        int baseDuration = plugin.getConfigManager().getInt(
            "skills.swords.abilities.rupture.base-duration", 100
        );
        int perLevelIncrease = plugin.getConfigManager().getInt(
            "skills.swords.abilities.rupture.duration-per-level", 2
        );
        return baseDuration + (level * perLevelIncrease);
    }

    /**
     * Get Rupture damage per tick
     */
    public double getRuptureDamage(int level) {
        double baseDamage = plugin.getConfigManager().getDouble(
            "skills.swords.abilities.rupture.base-damage", 1.0
        );
        double perLevelIncrease = plugin.getConfigManager().getDouble(
            "skills.swords.abilities.rupture.damage-per-level", 0.1
        );
        return baseDamage + (level * perLevelIncrease);
    }

    /**
     * Check if Counter Attack should trigger
     */
    public boolean shouldCounterAttack(int level) {
        double chance = getAbilityChance("counter-attack", level);
        return rollChance(chance);
    }

    /**
     * Get Counter Attack damage multiplier
     */
    public double getCounterAttackMultiplier() {
        return plugin.getConfigManager().getDouble(
            "skills.swords.abilities.counter-attack.damage-multiplier", 1.5
        );
    }

    /**
     * Get XP for hitting with sword
     */
    public int getXPForHit() {
        return getXPForAction("hit");
    }
}
