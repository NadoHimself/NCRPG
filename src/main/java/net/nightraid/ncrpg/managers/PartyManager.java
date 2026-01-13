package net.nightraid.ncrpg.managers;

import net.nightraid.ncrpg.NCRPG;
import net.nightraid.ncrpg.models.SkillType;
import net.nightraid.ncrpg.party.Party;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manager for party system
 */
public class PartyManager {

    private final NCRPG plugin;
    private final Map<UUID, Party> playerParties; // Player UUID -> Party
    private final Map<UUID, Party> parties; // Party UUID -> Party
    private final Map<UUID, UUID> invites; // Player UUID -> Party UUID

    public PartyManager(NCRPG plugin) {
        this.plugin = plugin;
        this.playerParties = new ConcurrentHashMap<>();
        this.parties = new ConcurrentHashMap<>();
        this.invites = new ConcurrentHashMap<>();
    }

    /**
     * Create a new party
     */
    public Party createParty(UUID leaderUUID, String leaderName) {
        // Check if already in party
        if (playerParties.containsKey(leaderUUID)) {
            return null;
        }

        Party party = new Party(leaderUUID, leaderName);
        parties.put(party.getPartyId(), party);
        playerParties.put(leaderUUID, party);

        return party;
    }

    /**
     * Disband a party
     */
    public boolean disbandParty(UUID partyId) {
        Party party = parties.get(partyId);
        if (party == null) {
            return false;
        }

        // Remove all members from tracking
        for (UUID memberUUID : party.getMembers()) {
            playerParties.remove(memberUUID);
        }

        // Remove party
        parties.remove(partyId);
        return true;
    }

    /**
     * Send party invite
     */
    public boolean sendInvite(UUID senderUUID, UUID targetUUID) {
        Party party = playerParties.get(senderUUID);
        if (party == null) {
            return false;
        }

        // Check if sender is leader
        if (!party.getLeaderUUID().equals(senderUUID)) {
            return false;
        }

        // Check if target already in party
        if (playerParties.containsKey(targetUUID)) {
            return false;
        }

        // Check party size
        int maxSize = plugin.getConfigManager().getInt("party.max-size", 6);
        if (party.getMembers().size() >= maxSize) {
            return false;
        }

        invites.put(targetUUID, party.getPartyId());
        return true;
    }

    /**
     * Accept party invite
     */
    public boolean acceptInvite(UUID playerUUID, String playerName) {
        UUID partyId = invites.remove(playerUUID);
        if (partyId == null) {
            return false;
        }

        Party party = parties.get(partyId);
        if (party == null) {
            return false;
        }

        // Add to party
        party.addMember(playerUUID, playerName);
        playerParties.put(playerUUID, party);

        return true;
    }

    /**
     * Leave party
     */
    public boolean leaveParty(UUID playerUUID) {
        Party party = playerParties.get(playerUUID);
        if (party == null) {
            return false;
        }

        // Remove from party
        party.removeMember(playerUUID);
        playerParties.remove(playerUUID);

        // If leader left, transfer leadership or disband
        if (party.getLeaderUUID().equals(playerUUID)) {
            if (party.getMembers().isEmpty()) {
                disbandParty(party.getPartyId());
            } else {
                // Transfer to next member
                UUID newLeader = party.getMembers().iterator().next();
                party.setLeader(newLeader);
            }
        }

        return true;
    }

    /**
     * Distribute XP to party members within range
     */
    public void distributePartyXP(UUID playerUUID, SkillType skillType, double baseXP) {
        if (!plugin.getConfigManager().getBoolean("party.enabled", true)) {
            return;
        }

        Party party = playerParties.get(playerUUID);
        if (party == null) {
            return;
        }

        // NOTE: Replace with Hytale API
        // Player player = Bukkit.getPlayer(playerUUID);
        // if (player == null) return;

        double shareRange = plugin.getConfigManager().getDouble("party.xp-share-range", 30.0);
        double bonusMultiplier = plugin.getConfigManager().getDouble("party.xp-bonus-multiplier", 1.1);

        int membersInRange = 0;
        List<UUID> nearbyMembers = new ArrayList<>();

        // Count members in range
        // for (UUID memberUUID : party.getMembers()) {
        //     if (memberUUID.equals(playerUUID)) continue;
        //     
        //     Player member = Bukkit.getPlayer(memberUUID);
        //     if (member != null && member.getWorld().equals(player.getWorld())) {
        //         double distance = player.getLocation().distance(member.getLocation());
        //         if (distance <= shareRange) {
        //             membersInRange++;
        //             nearbyMembers.add(memberUUID);
        //         }
        //     }
        // }

        // If no members in range, no distribution
        if (membersInRange == 0) {
            return;
        }

        // Apply bonus multiplier
        double bonusXP = baseXP * bonusMultiplier;
        double xpPerMember = bonusXP / (membersInRange + 1); // +1 for the original player

        // Distribute to nearby members
        for (UUID memberUUID : nearbyMembers) {
            plugin.getSkillManager().addXP(memberUUID, skillType, xpPerMember);
        }

        // Give XP to original player
        plugin.getSkillManager().addXP(playerUUID, skillType, xpPerMember);
    }

    /**
     * Get party for player
     */
    public Party getParty(UUID playerUUID) {
        return playerParties.get(playerUUID);
    }

    /**
     * Check if player has pending invite
     */
    public boolean hasPendingInvite(UUID playerUUID) {
        return invites.containsKey(playerUUID);
    }
}
