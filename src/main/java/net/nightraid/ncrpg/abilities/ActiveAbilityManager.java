package net.nightraid.ncrpg.abilities;

import net.nightraid.ncrpg.models.SkillType;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manager for active abilities with durations
 */
public class ActiveAbilityManager {

    // Map: Player UUID -> Skill -> Ability -> Expiry Time
    private final Map<UUID, Map<SkillType, Map<String, Long>>> activeAbilities;

    public ActiveAbilityManager() {
        this.activeAbilities = new ConcurrentHashMap<>();
    }

    /**
     * Activate an ability
     * @param uuid Player UUID
     * @param skill Skill type
     * @param abilityName Ability name
     * @param durationSeconds Duration in seconds
     */
    public void activateAbility(UUID uuid, SkillType skill, String abilityName, int durationSeconds) {
        activeAbilities.computeIfAbsent(uuid, k -> new ConcurrentHashMap<>())
                .computeIfAbsent(skill, k -> new ConcurrentHashMap<>())
                .put(abilityName, System.currentTimeMillis() + (durationSeconds * 1000L));
    }

    /**
     * Check if ability is active
     */
    public boolean isActive(UUID uuid, SkillType skill, String abilityName) {
        Map<SkillType, Map<String, Long>> playerAbilities = activeAbilities.get(uuid);
        if (playerAbilities == null) {
            return false;
        }

        Map<String, Long> skillAbilities = playerAbilities.get(skill);
        if (skillAbilities == null) {
            return false;
        }

        Long expiryTime = skillAbilities.get(abilityName);
        if (expiryTime == null) {
            return false;
        }

        if (System.currentTimeMillis() >= expiryTime) {
            skillAbilities.remove(abilityName);
            return false;
        }

        return true;
    }

    /**
     * Get remaining duration in seconds
     */
    public int getRemainingDuration(UUID uuid, SkillType skill, String abilityName) {
        Map<SkillType, Map<String, Long>> playerAbilities = activeAbilities.get(uuid);
        if (playerAbilities == null) {
            return 0;
        }

        Map<String, Long> skillAbilities = playerAbilities.get(skill);
        if (skillAbilities == null) {
            return 0;
        }

        Long expiryTime = skillAbilities.get(abilityName);
        if (expiryTime == null) {
            return 0;
        }

        long remaining = expiryTime - System.currentTimeMillis();
        return remaining > 0 ? (int) (remaining / 1000) : 0;
    }

    /**
     * Deactivate an ability
     */
    public void deactivateAbility(UUID uuid, SkillType skill, String abilityName) {
        Map<SkillType, Map<String, Long>> playerAbilities = activeAbilities.get(uuid);
        if (playerAbilities != null) {
            Map<String, Long> skillAbilities = playerAbilities.get(skill);
            if (skillAbilities != null) {
                skillAbilities.remove(abilityName);
            }
        }
    }

    /**
     * Deactivate all abilities for a player
     */
    public void deactivateAll(UUID uuid) {
        activeAbilities.remove(uuid);
    }
}
