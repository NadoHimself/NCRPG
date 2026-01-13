package net.nightraid.ncrpg.party;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Represents a party of players
 */
public class Party {

    private final UUID partyId;
    private UUID leaderUUID;
    private final Map<UUID, String> members; // UUID -> Player Name
    private final long createdAt;

    public Party(UUID leaderUUID, String leaderName) {
        this.partyId = UUID.randomUUID();
        this.leaderUUID = leaderUUID;
        this.members = new ConcurrentHashMap<>();
        this.members.put(leaderUUID, leaderName);
        this.createdAt = System.currentTimeMillis();
    }

    public UUID getPartyId() {
        return partyId;
    }

    public UUID getLeaderUUID() {
        return leaderUUID;
    }

    public void setLeader(UUID newLeader) {
        if (members.containsKey(newLeader)) {
            this.leaderUUID = newLeader;
        }
    }

    public Set<UUID> getMembers() {
        return members.keySet();
    }

    public Map<UUID, String> getMemberNames() {
        return new HashMap<>(members);
    }

    public void addMember(UUID uuid, String name) {
        members.put(uuid, name);
    }

    public void removeMember(UUID uuid) {
        members.remove(uuid);
    }

    public boolean isMember(UUID uuid) {
        return members.containsKey(uuid);
    }

    public boolean isLeader(UUID uuid) {
        return leaderUUID.equals(uuid);
    }

    public int getSize() {
        return members.size();
    }

    public long getCreatedAt() {
        return createdAt;
    }
}
