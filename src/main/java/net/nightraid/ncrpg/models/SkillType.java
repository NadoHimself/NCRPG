package net.nightraid.ncrpg.models;

/**
 * Enum representing all skill types in NCRPG
 */
public enum SkillType {
    MINING("Mining", "⛏"),
    WOODCUTTING("Woodcutting", "🪓"),
    FARMING("Farming", "🌾"),
    FISHING("Fishing", "🎣"),
    ACROBATICS("Acrobatics", "🤸"),
    EXCAVATION("Excavation", "🏺"),
    HERBALISM("Herbalism", "🌿"),
    SWORDS("Swords", "⚔"),
    AXES("Axes", "🔨"),  // Changed from duplicate 🪓 to 🔨
    ARCHERY("Archery", "🏹"),
    UNARMED("Unarmed", "👊"),
    COMBAT("Combat", "⚡");

    private final String displayName;
    private final String icon;

    SkillType(String displayName, String icon) {
        this.displayName = displayName;
        this.icon = icon;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getIcon() {
        return icon;
    }

    /**
     * Get config path for this skill
     */
    public String getConfigPath() {
        return "skills." + name().toLowerCase();
    }

    /**
     * Get skill from string (case-insensitive)
     */
    public static SkillType fromString(String name) {
        for (SkillType skill : values()) {
            if (skill.name().equalsIgnoreCase(name) || skill.displayName.equalsIgnoreCase(name)) {
                return skill;
            }
        }
        return null;
    }
}