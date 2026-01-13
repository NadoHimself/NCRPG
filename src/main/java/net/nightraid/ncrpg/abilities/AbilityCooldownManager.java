package net.nightraid.ncrpg.abilities;

import net.nightraid.ncrpg.models.SkillType;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manager for ability cooldowns
 */
public class AbilityCooldownManager {

    // Map: Player UUID -> Skill -> Ability -> Expiry Time
    private final Map<UUID, Map<SkillType, Map<String, Long>>> cooldowns;

    public AbilityCooldownManager() {
        this.cooldowns = new ConcurrentHashMap<>();
    }

    /**
     * Set cooldown for an ability
     * @param uuid Player UUID
     * @param skill Skill type
     * @param abilityName Ability name
     * @param cooldownSeconds Cooldown duration in seconds
     */
    public void setCooldown(UUID uuid, SkillType skill, String abilityName, int cooldownSeconds) {
        cooldowns.computeIfAbsent(uuid, k -> new ConcurrentHashMap<>())
                .computeIfAbsent(skill, k -> new ConcurrentHashMap<>())
                .put(abilityName, System.currentTimeMillis() + (cooldownSeconds * 1000L));
    }

    /**
     * Check if ability is on cooldown
     */
    public boolean isOnCooldown(UUID uuid, SkillType skill, String abilityName) {
        Map<SkillType, Map<String, Long>> playerCooldowns = cooldowns.get(uuid);
        if (playerCooldowns == null) {
            return false;
        }

        Map<String, Long> skillCooldowns = playerCooldowns.get(skill);
        if (skillCooldowns == null) {
            return false;
        }

        Long expiryTime = skillCooldowns.get(abilityName);
        if (expiryTime == null) {
            return false;
        }

        if (System.currentTimeMillis() >= expiryTime) {
            skillCooldowns.remove(abilityName);
            return false;
        }

        return true;
    }

    /**
     * Get remaining cooldown in seconds
     */
    public int getRemainingCooldown(UUID uuid, SkillType skill, String abilityName) {
        Map<SkillType, Map<String, Long>> playerCooldowns = cooldowns.get(uuid);
        if (playerCooldowns == null) {
            return 0;
        }

        Map<String, Long> skillCooldowns = playerCooldowns.get(skill);
        if (skillCooldowns == null) {
            return 0;
        }

        Long expiryTime = skillCooldowns.get(abilityName);
        if (expiryTime == null) {
            return 0;
        }

        long remaining = expiryTime - System.currentTimeMillis();
        return remaining > 0 ? (int) (remaining / 1000) : 0;
    }

    /**
     * Clear cooldown for an ability
     */
    public void clearCooldown(UUID uuid, SkillType skill, String abilityName) {
        Map<SkillType, Map<String, Long>> playerCooldowns = cooldowns.get(uuid);
        if (playerCooldowns != null) {
            Map<String, Long> skillCooldowns = playerCooldowns.get(skill);
            if (skillCooldowns != null) {
                skillCooldowns.remove(abilityName);
            }
        }
    }

    /**
     * Clear all cooldowns for a player
     */
    public void clearAllCooldowns(UUID uuid) {
        cooldowns.remove(uuid);
    }
}
