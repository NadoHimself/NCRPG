package net.nightraid.ncrpg.skills;

import net.nightraid.ncrpg.NCRPG;
import net.nightraid.ncrpg.models.SkillType;

/**
 * Axes Skill Implementation
 * Features: Critical strikes, armor impact, skull splitter
 */
public class AxesSkill extends Skill {

    public AxesSkill(NCRPG plugin) {
        super(plugin, SkillType.AXES);
    }

    /**
     * Get damage bonus multiplier
     */
    public double getDamageBonus(int level) {
        return calculateDamageBonus(level);
    }

    /**
     * Check if critical strike should occur
     */
    public boolean shouldCriticalStrike(int level) {
        double chance = getAbilityChance("critical-strike", level);
        return rollChance(chance);
    }

    /**
     * Get critical strike damage multiplier
     */
    public double getCriticalStrikeMultiplier(int level) {
        double baseMultiplier = plugin.getConfigManager().getDouble(
            "skills.axes.abilities.critical-strike.base-multiplier", 1.5
        );
        double perLevelBonus = plugin.getConfigManager().getDouble(
            "skills.axes.abilities.critical-strike.multiplier-per-level", 0.01
        );
        return baseMultiplier + (level * perLevelBonus);
    }

    /**
     * Check if Armor Impact should apply
     */
    public boolean shouldArmorImpact(int level) {
        double chance = getAbilityChance("armor-impact", level);
        return rollChance(chance);
    }

    /**
     * Get Armor Impact durability damage multiplier
     */
    public double getArmorImpactMultiplier() {
        return plugin.getConfigManager().getDouble(
            "skills.axes.abilities.armor-impact.durability-multiplier", 2.0
        );
    }

    /**
     * Get Skull Splitter area damage multiplier
     */
    public double getSkullSplitterMultiplier() {
        return plugin.getConfigManager().getDouble(
            "skills.axes.abilities.skull-splitter.damage-multiplier", 0.5
        );
    }

    /**
     * Get Skull Splitter radius
     */
    public double getSkullSplitterRadius() {
        return plugin.getConfigManager().getDouble(
            "skills.axes.abilities.skull-splitter.radius", 3.0
        );
    }

    /**
     * Get XP for hitting with axe
     */
    public int getXPForHit() {
        return getXPForAction("hit");
    }
}
