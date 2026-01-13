package net.nightraid.ncrpg.listeners;

import com.hypixel.hytale.event.EventHandler;
import com.hypixel.hytale.event.Listener;
import com.hypixel.hytale.event.player.PlayerHarvestBlockEvent;
import com.hypixel.hytale.entity.player.Player;
import com.hypixel.hytale.world.block.Block;
import com.hypixel.hytale.world.block.BlockType;
import com.hypixel.hytale.item.ItemStack;
import com.hypixel.hytale.item.ItemType;
import com.hypixel.hytale.world.Location;

import net.nightraid.ncrpg.NCRPG;
import net.nightraid.ncrpg.managers.PlayerDataManager;
import net.nightraid.ncrpg.managers.SkillManager;
import net.nightraid.ncrpg.models.PlayerSkillData;
import net.nightraid.ncrpg.models.SkillType;
import net.nightraid.ncrpg.skills.FarmingSkill;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

/**
 * Handles crop harvesting events for Farming skill
 * Features: Triple/Double drops, Hylian Luck, Green Terra
 */
public class PlayerHarvestListener implements Listener {
    private final NCRPG plugin;
    private final PlayerDataManager playerDataManager;
    private final SkillManager skillManager;
    private final Random random;

    public PlayerHarvestListener(NCRPG plugin) {
        this.plugin = plugin;
        this.playerDataManager = plugin.getPlayerDataManager();
        this.skillManager = plugin.getSkillManager();
        this.random = new Random();
    }

    @EventHandler
    public void onPlayerHarvest(PlayerHarvestBlockEvent event) {
        if (event.isCancelled()) {
            return;
        }

        Player player = event.getPlayer();
        Block block = event.getHarvestedBlock();
        BlockType blockType = block.getType();
        String cropType = blockType.getName();
        
        PlayerSkillData skillData = playerDataManager.getPlayerData(player.getUniqueId())
            .getSkillData(SkillType.FARMING);
        
        FarmingSkill farmingSkill = new FarmingSkill(plugin);
        
        // Check if Green Terra is active
        boolean hasGreenTerra = plugin.getActiveAbilityManager()
            .isAbilityActive(player.getUniqueId(), SkillType.FARMING, "green-terra");
        
        // Grant XP
        int xp = farmingSkill.getXPForCrop(cropType);
        if (hasGreenTerra) {
            xp *= 3; // Triple XP during Green Terra
        }
        skillManager.addXP(player.getUniqueId(), SkillType.FARMING, xp);
        
        // Calculate drop multiplier
        int multiplier = hasGreenTerra ? 3 : farmingSkill.getDropMultiplier(skillData.getLevel());
        
        // Apply drop multiplier
        if (multiplier > 1) {
            List<ItemStack> originalDrops = event.getItemsHarvested();
            List<ItemStack> bonusDrops = new ArrayList<>();
            
            for (ItemStack drop : originalDrops) {
                // Create bonus drops (multiplier - 1 because original drop already exists)
                for (int i = 1; i < multiplier; i++) {
                    ItemStack bonus = drop.clone();
                    bonusDrops.add(bonus);
                }
            }
            
            // Add bonus drops to the event
            originalDrops.addAll(bonusDrops);
            
            if (multiplier == 2) {
                player.sendMessage("§e§l+§6 Double Drop!");
            } else if (multiplier == 3) {
                player.sendMessage("§e§l++§6 Triple Drop!");
            }
        }
        
        // Hylian Luck - Rare drops from crops
        if (farmingSkill.hasHylianLuck(skillData.getLevel())) {
            double luckChance = 0.01 * (skillData.getLevel() / 10.0); // Scales with level
            if (random.nextDouble() < luckChance) {
                ItemStack rareDrop = generateRareDrop();
                if (rareDrop != null) {
                    Location location = block.getLocation();
                    location.getWorld().dropItem(location, rareDrop);
                    player.sendMessage("§e§l✦ §6Hylian Luck! §7Found rare item!");
                }
            }
        }
        
        // Green Terra visual feedback
        if (hasGreenTerra) {
            player.sendMessage("§a§l🌿 §2Green Terra! §7Triple rewards!");
        }
    }

    /**
     * Generate rare drop for Hylian Luck
     */
    private ItemStack generateRareDrop() {
        String[] rareItems = {
            "golden_apple",      // Most common
            "emerald",
            "diamond",
            "enchanted_book",
            "golden_carrot",
            "melon_block",       // Minecraft reference
            "enchanted_golden_apple" // Very rare
        };
        
        // Weighted random selection (earlier items more common)
        int maxIndex = random.nextInt(10);
        if (maxIndex >= rareItems.length) {
            maxIndex = 0; // Default to golden apple
        }
        
        String itemName = rareItems[Math.min(maxIndex, rareItems.length - 1)];
        int amount = (maxIndex <= 2) ? random.nextInt(2) + 1 : 1;
        
        // TODO: Replace with actual Hytale item creation
        // return new ItemStack(ItemType.fromName(itemName), amount);
        return null; // Placeholder until Hytale API is available
    }

    /**
     * Check if the crop is fully grown
     */
    private boolean isFullyGrown(Block block) {
        // TODO: Implement with Hytale block state API
        // Check if crop is at maximum growth stage
        return true; // Placeholder
    }
}
