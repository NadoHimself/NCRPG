package net.nightraid.ncrpg.listeners;

import com.hypixel.hytale.event.EventHandler;
import com.hypixel.hytale.event.Listener;
import com.hypixel.hytale.event.block.BlockBreakEvent;
import com.hypixel.hytale.entity.player.Player;
import com.hypixel.hytale.world.block.Block;
import com.hypixel.hytale.world.block.BlockType;
import com.hypixel.hytale.item.ItemStack;
import com.hypixel.hytale.world.Location;

import net.nightraid.ncrpg.NCRPG;
import net.nightraid.ncrpg.managers.PlayerDataManager;
import net.nightraid.ncrpg.managers.SkillManager;
import net.nightraid.ncrpg.models.PlayerSkillData;
import net.nightraid.ncrpg.models.SkillType;
import net.nightraid.ncrpg.skills.*;

import java.util.Random;

/**
 * Handles block break events for Mining, Woodcutting, Excavation, and Herbalism skills
 */
public class BlockBreakListener implements Listener {
    private final NCRPG plugin;
    private final PlayerDataManager playerDataManager;
    private final SkillManager skillManager;
    private final Random random;

    public BlockBreakListener(NCRPG plugin) {
        this.plugin = plugin;
        this.playerDataManager = plugin.getPlayerDataManager();
        this.skillManager = plugin.getSkillManager();
        this.random = new Random();
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.isCancelled()) {
            return;
        }

        Player player = event.getPlayer();
        Block block = event.getBlock();
        BlockType blockType = block.getType();
        String typeName = blockType.getName();
        
        // Check if player is using an active ability
        boolean hasSuperBreaker = plugin.getActiveAbilityManager()
            .isAbilityActive(player.getUniqueId(), SkillType.MINING, "super-breaker");
        boolean hasTreeFeller = plugin.getActiveAbilityManager()
            .isAbilityActive(player.getUniqueId(), SkillType.WOODCUTTING, "tree-feller");
        boolean hasGigaDrill = plugin.getActiveAbilityManager()
            .isAbilityActive(player.getUniqueId(), SkillType.EXCAVATION, "giga-drill-breaker");
        boolean hasGreenTerra = plugin.getActiveAbilityManager()
            .isAbilityActive(player.getUniqueId(), SkillType.HERBALISM, "green-terra");
        
        // Handle Mining
        if (isMiningBlock(typeName)) {
            handleMining(player, block, typeName, hasSuperBreaker);
        }
        // Handle Woodcutting
        else if (isWoodBlock(typeName)) {
            handleWoodcutting(player, block, typeName, hasTreeFeller);
        }
        // Handle Excavation
        else if (isExcavationBlock(typeName)) {
            handleExcavation(player, block, typeName, hasGigaDrill);
        }
        // Handle Herbalism
        else if (isHerbalismBlock(typeName)) {
            handleHerbalism(player, block, typeName, hasGreenTerra);
        }
    }

    private void handleMining(Player player, Block block, String blockType, boolean hasSuperBreaker) {
        PlayerSkillData skillData = playerDataManager.getPlayerData(player.getUniqueId())
            .getSkillData(SkillType.MINING);
        
        MiningSkill miningSkill = new MiningSkill(plugin);
        
        // Grant XP
        int xp = miningSkill.getXPForBlock(blockType);
        if (hasSuperBreaker) {
            xp *= 3; // Triple XP during Super Breaker
        }
        skillManager.addXP(player.getUniqueId(), SkillType.MINING, xp);
        
        // Check for double drops
        if (miningSkill.shouldDoubleDrops(skillData.getLevel()) || hasSuperBreaker) {
            Location location = block.getLocation();
            ItemStack drop = block.getDrops().get(0); // Get first drop
            location.getWorld().dropItem(location, drop);
            
            player.sendMessage("§e§l+§6 Double Drop!");
        }
        
        // Mining speed multiplier is handled by the ability system
    }

    private void handleWoodcutting(Player player, Block block, String blockType, boolean hasTreeFeller) {
        PlayerSkillData skillData = playerDataManager.getPlayerData(player.getUniqueId())
            .getSkillData(SkillType.WOODCUTTING);
        
        WoodcuttingSkill woodcuttingSkill = new WoodcuttingSkill(plugin);
        
        // Grant XP
        int xp = woodcuttingSkill.getXPForWood(blockType);
        skillManager.addXP(player.getUniqueId(), SkillType.WOODCUTTING, xp);
        
        // Check for double drops
        if (woodcuttingSkill.shouldDoubleDrops(skillData.getLevel()) && !hasTreeFeller) {
            Location location = block.getLocation();
            ItemStack drop = block.getDrops().get(0);
            location.getWorld().dropItem(location, drop);
            
            player.sendMessage("§e§l+§6 Double Drop!");
        }
        
        // Handle Tree Feller ability
        if (hasTreeFeller) {
            int maxBlocks = woodcuttingSkill.getTreeFellerMaxBlocks(skillData.getLevel());
            breakConnectedWood(player, block, maxBlocks);
        }
    }

    private void handleExcavation(Player player, Block block, String blockType, boolean hasGigaDrill) {
        PlayerSkillData skillData = playerDataManager.getPlayerData(player.getUniqueId())
            .getSkillData(SkillType.EXCAVATION);
        
        ExcavationSkill excavationSkill = new ExcavationSkill(plugin);
        
        // Grant XP
        int xp = excavationSkill.getXPForBlock(blockType);
        if (hasGigaDrill) {
            xp *= 3;
        }
        skillManager.addXP(player.getUniqueId(), SkillType.EXCAVATION, xp);
        
        // Check for treasure
        if (excavationSkill.shouldFindTreasure(skillData.getLevel())) {
            int tier = excavationSkill.getArchaeologyTier(skillData.getLevel());
            dropTreasure(player, block.getLocation(), tier);
        }
        
        // Check for triple drops during Giga Drill Breaker
        if (hasGigaDrill && excavationSkill.shouldTripleDrops(skillData.getLevel())) {
            Location location = block.getLocation();
            ItemStack drop = block.getDrops().get(0);
            location.getWorld().dropItem(location, drop);
            location.getWorld().dropItem(location, drop);
            
            player.sendMessage("§e§l++§6 Triple Drop!");
        }
    }

    private void handleHerbalism(Player player, Block block, String blockType, boolean hasGreenTerra) {
        PlayerSkillData skillData = playerDataManager.getPlayerData(player.getUniqueId())
            .getSkillData(SkillType.HERBALISM);
        
        HerbalismSkill herbalismSkill = new HerbalismSkill(plugin);
        
        // Grant XP
        int xp = herbalismSkill.getXPForPlant(blockType);
        if (hasGreenTerra) {
            xp *= 3;
        }
        skillManager.addXP(player.getUniqueId(), SkillType.HERBALISM, xp);
        
        // Check for double drops
        if (herbalismSkill.shouldDoubleDrops(skillData.getLevel()) || hasGreenTerra) {
            Location location = block.getLocation();
            ItemStack drop = block.getDrops().get(0);
            location.getWorld().dropItem(location, drop);
            
            player.sendMessage("§e§l+§6 Double Drop!");
        }
        
        // Check for Green Thumb (auto-replant)
        if (herbalismSkill.hasGreenThumb(skillData.getLevel()) && isCrop(blockType)) {
            // Replant crop after a short delay
            plugin.getServer().getScheduler().runTaskLater(() -> {
                block.setType(getSeedType(blockType));
            }, 10L); // 0.5 second delay
        }
    }

    // Block type checking methods
    private boolean isMiningBlock(String blockType) {
        return blockType.contains("ore") || 
               blockType.contains("stone") || 
               blockType.contains("cobblestone") ||
               blockType.contains("andesite") ||
               blockType.contains("diorite") ||
               blockType.contains("granite");
    }

    private boolean isWoodBlock(String blockType) {
        return blockType.contains("log") || 
               blockType.contains("wood") ||
               blockType.contains("stem"); // For mushroom stems
    }

    private boolean isExcavationBlock(String blockType) {
        return blockType.equals("dirt") || 
               blockType.equals("sand") || 
               blockType.equals("gravel") ||
               blockType.equals("clay") ||
               blockType.equals("soul_sand") ||
               blockType.equals("mycelium");
    }

    private boolean isHerbalismBlock(String blockType) {
        return blockType.contains("leaves") || 
               blockType.contains("flower") || 
               blockType.contains("mushroom") ||
               blockType.contains("crop") ||
               blockType.contains("wheat") ||
               blockType.contains("carrot") ||
               blockType.contains("potato");
    }

    private boolean isCrop(String blockType) {
        return blockType.contains("crop") ||
               blockType.contains("wheat") ||
               blockType.contains("carrot") ||
               blockType.contains("potato") ||
               blockType.contains("beetroot");
    }

    // Helper methods
    private void breakConnectedWood(Player player, Block startBlock, int maxBlocks) {
        // TODO: Implement recursive tree breaking
        // This would find all connected log blocks and break them
        // Limited to maxBlocks to prevent abuse
        player.sendMessage("§6§lTREE FELLER! §7Breaking connected wood...");
    }

    private void dropTreasure(Player player, Location location, int tier) {
        // TODO: Implement treasure drop based on tier
        // Tier 1: Basic items (iron nuggets, etc.)
        // Tier 2: Uncommon items (gold nuggets, emeralds)
        // Tier 3: Rare items (diamonds, etc.)
        // Tier 4: Very rare items (special artifacts)
        player.sendMessage("§e§l★ §6You found treasure! (Tier " + tier + ")");
    }

    private BlockType getSeedType(String cropType) {
        // TODO: Map fully grown crops to their seed/initial state
        // This would return the appropriate BlockType for replanting
        return null; // Placeholder
    }
}
