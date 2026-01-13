package net.nightraid.ncrpg.listeners;

import com.hypixel.hytale.event.EventHandler;
import com.hypixel.hytale.event.Listener;
import com.hypixel.hytale.event.player.PlayerFishEvent;
import com.hypixel.hytale.entity.player.Player;
import com.hypixel.hytale.item.ItemStack;
import com.hypixel.hytale.item.ItemType;
import com.hypixel.hytale.world.Location;

import net.nightraid.ncrpg.NCRPG;
import net.nightraid.ncrpg.managers.PlayerDataManager;
import net.nightraid.ncrpg.managers.SkillManager;
import net.nightraid.ncrpg.models.PlayerSkillData;
import net.nightraid.ncrpg.models.SkillType;
import net.nightraid.ncrpg.skills.FishingSkill;

import java.util.Random;

/**
 * Handles fishing events for Fishing skill
 * Features: Treasure Hunter, Magic Hunter, Shake, Master Angler
 */
public class PlayerFishListener implements Listener {
    private final NCRPG plugin;
    private final PlayerDataManager playerDataManager;
    private final SkillManager skillManager;
    private final Random random;

    public PlayerFishListener(NCRPG plugin) {
        this.plugin = plugin;
        this.playerDataManager = plugin.getPlayerDataManager();
        this.skillManager = plugin.getSkillManager();
        this.random = new Random();
    }

    @EventHandler
    public void onPlayerFish(PlayerFishEvent event) {
        // Only process successful catches
        if (event.getState() != PlayerFishEvent.State.CAUGHT_FISH) {
            return;
        }

        if (event.isCancelled()) {
            return;
        }

        Player player = event.getPlayer();
        PlayerSkillData skillData = playerDataManager.getPlayerData(player.getUniqueId())
            .getSkillData(SkillType.FISHING);
        
        FishingSkill fishingSkill = new FishingSkill(plugin);
        
        // Grant XP
        int xp = fishingSkill.getXPForFish();
        skillManager.addXP(player.getUniqueId(), SkillType.FISHING, xp);
        
        // Treasure Hunter - Replace catch with treasure
        int treasureTier = fishingSkill.getTreasureTier(skillData.getLevel());
        if (treasureTier > 0) {
            double treasureChance = 0.05 * treasureTier; // 5% per tier
            if (random.nextDouble() < treasureChance) {
                ItemStack treasure = generateTreasure(treasureTier);
                if (treasure != null) {
                    event.setCaught(treasure);
                    player.sendMessage("§e§l⭐ §6Treasure Hunter! §7(Tier " + treasureTier + ")");
                }
            }
        }
        
        // Magic Hunter - Add enchantments to caught items
        if (fishingSkill.hasMagicHunter(skillData.getLevel()) && random.nextDouble() < 0.15) {
            ItemStack caught = event.getCaught();
            if (caught != null && canBeEnchanted(caught)) {
                addRandomEnchantment(caught, skillData.getLevel());
                player.sendMessage("§d§l✦ §5Magic Hunter! §7Enchanted item!");
            }
        }
        
        // Shake - Get mob drops while fishing
        if (fishingSkill.canShake(skillData.getLevel()) && random.nextDouble() < 0.1) {
            ItemStack mobDrop = generateMobDrop(skillData.getLevel());
            if (mobDrop != null) {
                Location location = player.getLocation();
                location.getWorld().dropItem(location, mobDrop);
                player.sendMessage("§b§l🌊 §3Shake! §7Found mob loot!");
            }
        }
        
        // Master Angler bonus (faster fishing)
        // This is handled by modifying the fishing time in the ability system
    }

    /**
     * Generate treasure based on tier
     */
    private ItemStack generateTreasure(int tier) {
        String[] tier1Items = {"iron_ingot", "string", "leather", "bone"};
        String[] tier2Items = {"gold_ingot", "emerald", "diamond", "ender_pearl"};
        String[] tier3Items = {"enchanted_book", "name_tag", "saddle", "diamond_block"};
        String[] tier4Items = {"totem", "elytra", "netherite_scrap", "ancient_debris"};
        
        String[] itemPool;
        int baseAmount;
        
        switch (tier) {
            case 1:
                itemPool = tier1Items;
                baseAmount = random.nextInt(3) + 1;
                break;
            case 2:
                itemPool = tier2Items;
                baseAmount = random.nextInt(2) + 1;
                break;
            case 3:
                itemPool = tier3Items;
                baseAmount = 1;
                break;
            case 4:
                itemPool = tier4Items;
                baseAmount = 1;
                break;
            default:
                return null;
        }
        
        String itemName = itemPool[random.nextInt(itemPool.length)];
        // TODO: Replace with actual Hytale item creation
        // return new ItemStack(ItemType.fromName(itemName), baseAmount);
        return null; // Placeholder
    }

    /**
     * Generate random mob drop
     */
    private ItemStack generateMobDrop(int level) {
        String[] mobDrops = {
            "rotten_flesh", "bone", "spider_eye", "gunpowder",
            "slime_ball", "ender_pearl", "blaze_rod", "ghast_tear"
        };
        
        // Higher level = better drops
        int maxIndex = Math.min((level / 20) + 3, mobDrops.length);
        String dropName = mobDrops[random.nextInt(maxIndex)];
        int amount = random.nextInt(3) + 1;
        
        // TODO: Replace with actual Hytale item creation
        // return new ItemStack(ItemType.fromName(dropName), amount);
        return null; // Placeholder
    }

    /**
     * Check if item can be enchanted
     */
    private boolean canBeEnchanted(ItemStack item) {
        if (item == null) return false;
        
        String typeName = item.getType().getName().toLowerCase();
        return typeName.contains("rod") || 
               typeName.contains("bow") || 
               typeName.contains("sword") ||
               typeName.contains("armor") ||
               typeName.contains("tool");
    }

    /**
     * Add random enchantment to item based on level
     */
    private void addRandomEnchantment(ItemStack item, int level) {
        // TODO: Implement enchantment system when Hytale API is available
        // Higher level = better enchantments
        // Examples: Unbreaking, Mending, Luck of the Sea, Lure
    }
}
