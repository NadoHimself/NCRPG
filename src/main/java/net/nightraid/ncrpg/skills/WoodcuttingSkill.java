package net.nightraid.ncrpg.skills;

import net.nightraid.ncrpg.NCRPG;
import net.nightraid.ncrpg.models.SkillType;

/**
 * Woodcutting Skill Implementation
 * Features: Tree Feller ability, double drops, leaf blower
 */
public class WoodcuttingSkill extends Skill {

    public WoodcuttingSkill(NCRPG plugin) {
        super(plugin, SkillType.WOODCUTTING);
    }

    /**
     * Check if player should receive double drops
     */
    public boolean shouldDoubleDrops(int level) {
        double chance = getAbilityChance("double-drops", level);
        return rollChance(chance);
    }

    /**
     * Check if Leaf Blower is active (auto-break leaves)
     */
    public boolean hasLeafBlower(int level) {
        return isPassiveUnlocked("leaf-blower", level);
    }

    /**
     * Get max tree size for Tree Feller ability
     */
    public int getTreeFellerMaxBlocks(int level) {
        int baseMax = plugin.getConfigManager().getInt(
            "skills.woodcutting.abilities.tree-feller.base-max-blocks", 50
        );
        int perLevelIncrease = plugin.getConfigManager().getInt(
            "skills.woodcutting.abilities.tree-feller.blocks-per-level", 1
        );
        return Math.min(baseMax + (level * perLevelIncrease), 500);
    }

    /**
     * Get XP for cutting a specific wood type
     */
    public int getXPForWood(String woodType) {
        return getXPForAction(woodType.toLowerCase());
    }
}
