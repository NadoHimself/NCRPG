package net.nightraid.ncrpg.skills;

import net.nightraid.ncrpg.NCRPG;
import net.nightraid.ncrpg.models.SkillType;

/**
 * Unarmed Skill Implementation
 * Features: Disarm, iron arm grip, berserk
 */
public class UnarmedSkill extends Skill {

    public UnarmedSkill(NCRPG plugin) {
        super(plugin, SkillType.UNARMED);
    }

    /**
     * Get damage bonus multiplier
     */
    public double getDamageBonus(int level) {
        return calculateDamageBonus(level);
    }

    /**
     * Check if Disarm should occur
     */
    public boolean shouldDisarm(int level) {
        double chance = getAbilityChance("disarm", level);
        return rollChance(chance);
    }

    /**
     * Check if Iron Arm Grip prevents being disarmed
     */
    public boolean hasIronArmGrip(int level) {
        double chance = getAbilityChance("iron-arm-grip", level);
        return rollChance(chance);
    }

    /**
     * Get Berserk damage multiplier
     */
    public double getBerserkMultiplier() {
        return plugin.getConfigManager().getDouble(
            "skills.unarmed.abilities.berserk.damage-multiplier", 1.5
        );
    }

    /**
     * Check if arrow should be deflected
     */
    public boolean shouldDeflectArrow(int level) {
        double chance = getAbilityChance("arrow-deflect", level);
        return rollChance(chance);
    }

    /**
     * Get XP for hitting unarmed
     */
    public int getXPForHit() {
        return getXPForAction("hit");
    }
}
