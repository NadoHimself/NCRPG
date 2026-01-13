package net.nightraid.ncrpg.models;

import java.util.UUID;

/**
 * Model class representing player skill data
 */
public class PlayerSkillData {
    private final UUID uuid;
    private final SkillType skillType;
    private int level;
    private double xp;
    private int skillCap;

    public PlayerSkillData(UUID uuid, SkillType skillType) {
        this.uuid = uuid;
        this.skillType = skillType;
        this.level = 1;
        this.xp = 0;
        this.skillCap = 100; // Default cap
    }

    public PlayerSkillData(UUID uuid, SkillType skillType, int level, double xp, int skillCap) {
        this.uuid = uuid;
        this.skillType = skillType;
        this.level = level;
        this.xp = xp;
        this.skillCap = skillCap;
    }

    // Getters and setters

    public UUID getUuid() {
        return uuid;
    }
    
    public SkillType getSkillType() {
        return skillType;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = Math.min(level, skillCap);
    }

    public double getXp() {
        return xp;
    }

    public void setXp(double xp) {
        this.xp = xp;
    }

    public void addXp(double amount) {
        this.xp += amount;
    }

    public int getSkillCap() {
        return skillCap;
    }

    public void setSkillCap(int skillCap) {
        this.skillCap = skillCap;
        if (this.level > skillCap) {
            this.level = skillCap;
        }
    }

    /**
     * Calculate XP required for next level
     * Formula: baseXP * (level^exponent)
     */
    public double getXpForNextLevel(double baseXP, double exponent) {
        return baseXP * Math.pow(level, exponent);
    }

    /**
     * Get progress percentage to next level
     */
    public double getProgressPercent(double baseXP, double exponent) {
        double required = getXpForNextLevel(baseXP, exponent);
        return (xp / required) * 100.0;
    }

    /**
     * Check if ready to level up
     */
    public boolean canLevelUp(double baseXP, double exponent) {
        if (level >= skillCap) {
            return false;
        }
        return xp >= getXpForNextLevel(baseXP, exponent);
    }
}
