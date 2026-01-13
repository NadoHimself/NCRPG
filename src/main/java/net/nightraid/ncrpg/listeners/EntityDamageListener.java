package net.nightraid.ncrpg.listeners;

import com.hypixel.hytale.event.EventHandler;
import com.hypixel.hytale.event.Listener;
import com.hypixel.hytale.event.entity.EntityDamageByEntityEvent;
import com.hypixel.hytale.entity.Entity;
import com.hypixel.hytale.entity.LivingEntity;
import com.hypixel.hytale.entity.player.Player;
import com.hypixel.hytale.item.ItemStack;
import com.hypixel.hytale.item.ItemType;
import com.hypixel.hytale.util.Vector;

import net.nightraid.ncrpg.NCRPG;
import net.nightraid.ncrpg.managers.PlayerDataManager;
import net.nightraid.ncrpg.managers.SkillManager;
import net.nightraid.ncrpg.models.PlayerSkillData;
import net.nightraid.ncrpg.models.SkillType;
import net.nightraid.ncrpg.skills.*;

import java.util.Random;

/**
 * Handles entity damage events for combat skills:
 * Swords, Axes, Archery, Unarmed, Acrobatics, and general Combat
 */
public class EntityDamageListener implements Listener {
    private final NCRPG plugin;
    private final PlayerDataManager playerDataManager;
    private final SkillManager skillManager;
    private final Random random;

    public EntityDamageListener(NCRPG plugin) {
        this.plugin = plugin;
        this.playerDataManager = plugin.getPlayerDataManager();
        this.skillManager = plugin.getSkillManager();
        this.random = new Random();
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) {
            return;
        }

        Entity damager = event.getDamager();
        Entity victim = event.getEntity();

        // Handle player attacking entity
        if (damager instanceof Player && victim instanceof LivingEntity) {
            handlePlayerAttack((Player) damager, (LivingEntity) victim, event);
        }

        // Handle entity attacking player (for Acrobatics dodge)
        if (victim instanceof Player && damager instanceof LivingEntity) {
            handlePlayerDefense((Player) victim, (LivingEntity) damager, event);
        }
    }

    private void handlePlayerAttack(Player player, LivingEntity target, EntityDamageByEntityEvent event) {
        ItemStack weapon = player.getItemInMainHand();
        ItemType weaponType = weapon != null ? weapon.getType() : null;
        
        // Determine weapon type and handle accordingly
        if (weaponType != null) {
            String typeName = weaponType.getName();
            
            if (isSword(typeName)) {
                handleSwordsAttack(player, target, event);
            } else if (isAxe(typeName)) {
                handleAxesAttack(player, target, event);
            } else if (isBow(typeName)) {
                handleArcheryAttack(player, target, event);
            } else {
                handleUnarmedAttack(player, target, event);
            }
        } else {
            handleUnarmedAttack(player, target, event);
        }
        
        // General combat XP
        skillManager.addXP(player.getUniqueId(), SkillType.COMBAT, (int) event.getDamage());
    }

    private void handleSwordsAttack(Player player, LivingEntity target, EntityDamageByEntityEvent event) {
        PlayerSkillData skillData = playerDataManager.getPlayerData(player.getUniqueId())
            .getSkillData(SkillType.SWORDS);
        
        SwordsSkill swordsSkill = new SwordsSkill(plugin);
        
        // Grant XP
        int xp = (int) (event.getDamage() * 10);
        skillManager.addXP(player.getUniqueId(), SkillType.SWORDS, xp);
        
        // Apply damage scaling
        double damageBonus = swordsSkill.getDamageBonus(skillData.getLevel());
        event.setDamage(event.getDamage() * (1.0 + damageBonus));
        
        // Check for Rupture (Bleed)
        if (swordsSkill.shouldApplyRupture(skillData.getLevel())) {
            applyBleedEffect(target, swordsSkill.getRuptureDuration(skillData.getLevel()));
            player.sendMessage("§c§l⚔ §4Rupture! Target is bleeding!");
        }
        
        // Counter Attack is handled in defense method
    }

    private void handleAxesAttack(Player player, LivingEntity target, EntityDamageByEntityEvent event) {
        PlayerSkillData skillData = playerDataManager.getPlayerData(player.getUniqueId())
            .getSkillData(SkillType.AXES);
        
        AxesSkill axesSkill = new AxesSkill(plugin);
        
        // Grant XP
        int xp = (int) (event.getDamage() * 10);
        skillManager.addXP(player.getUniqueId(), SkillType.AXES, xp);
        
        // Check for Critical Strike
        if (axesSkill.shouldCriticalStrike(skillData.getLevel())) {
            double critMultiplier = axesSkill.getCriticalMultiplier(skillData.getLevel());
            event.setDamage(event.getDamage() * critMultiplier);
            player.sendMessage("§e§l⚡ §6Critical Strike!");
        }
        
        // Check for Armor Impact
        if (axesSkill.shouldArmorImpact(skillData.getLevel()) && target instanceof Player) {
            damageArmor((Player) target, axesSkill.getArmorDamage(skillData.getLevel()));
            player.sendMessage("§7§l🛡 §8Armor Impact!");
        }
        
        // Skull Splitter ability
        boolean hasSkullSplitter = plugin.getActiveAbilityManager()
            .isAbilityActive(player.getUniqueId(), SkillType.AXES, "skull-splitter");
        
        if (hasSkullSplitter) {
            // Deal AoE damage to nearby entities
            damageNearbyEntities(player, target.getLocation(), event.getDamage() * 0.5);
            player.sendMessage("§4§l💀 §cSkull Splitter AoE!");
        }
    }

    private void handleArcheryAttack(Player player, LivingEntity target, EntityDamageByEntityEvent event) {
        PlayerSkillData skillData = playerDataManager.getPlayerData(player.getUniqueId())
            .getSkillData(SkillType.ARCHERY);
        
        ArcherySkill archerySkill = new ArcherySkill(plugin);
        
        // Grant XP
        int xp = (int) (event.getDamage() * 10);
        skillManager.addXP(player.getUniqueId(), SkillType.ARCHERY, xp);
        
        // Apply damage scaling
        double damageBonus = archerySkill.getDamageBonus(skillData.getLevel());
        event.setDamage(event.getDamage() * (1.0 + damageBonus));
        
        // Check for Daze
        if (archerySkill.shouldDaze(skillData.getLevel()) && target instanceof Player) {
            applyDazeEffect((Player) target, archerySkill.getDazeDuration(skillData.getLevel()));
            player.sendMessage("§d§l⭐ §5Target is dazed!");
        }
        
        // Arrow retrieval is handled separately on arrow pickup
    }

    private void handleUnarmedAttack(Player player, LivingEntity target, EntityDamageByEntityEvent event) {
        PlayerSkillData skillData = playerDataManager.getPlayerData(player.getUniqueId())
            .getSkillData(SkillType.UNARMED);
        
        UnarmedSkill unarmedSkill = new UnarmedSkill(plugin);
        
        // Grant XP
        int xp = (int) (event.getDamage() * 10);
        skillManager.addXP(player.getUniqueId(), SkillType.UNARMED, xp);
        
        // Apply damage bonus
        double damageBonus = unarmedSkill.getDamageBonus(skillData.getLevel());
        event.setDamage(event.getDamage() * (1.0 + damageBonus));
        
        // Check for Disarm
        if (unarmedSkill.shouldDisarm(skillData.getLevel()) && target instanceof Player) {
            Player targetPlayer = (Player) target;
            ItemStack weapon = targetPlayer.getItemInMainHand();
            
            if (weapon != null && !weapon.getType().getName().equals("air")) {
                // Drop weapon on ground
                targetPlayer.getWorld().dropItem(targetPlayer.getLocation(), weapon);
                targetPlayer.setItemInMainHand(null);
                
                player.sendMessage("§e§l⚔ §6Disarmed target!");
                targetPlayer.sendMessage("§c§l⚔ §4You have been disarmed!");
            }
        }
        
        // Berserk ability
        boolean hasBerserk = plugin.getActiveAbilityManager()
            .isAbilityActive(player.getUniqueId(), SkillType.UNARMED, "berserk");
        
        if (hasBerserk) {
            event.setDamage(event.getDamage() * 1.5); // 50% damage boost
        }
    }

    private void handlePlayerDefense(Player player, LivingEntity attacker, EntityDamageByEntityEvent event) {
        // Acrobatics - Dodge
        PlayerSkillData acrobaticsData = playerDataManager.getPlayerData(player.getUniqueId())
            .getSkillData(SkillType.ACROBATICS);
        
        AcrobaticsSkill acrobaticsSkill = new AcrobaticsSkill(plugin);
        
        if (acrobaticsSkill.shouldDodge(acrobaticsData.getLevel())) {
            event.setCancelled(true);
            player.sendMessage("§a§l↔ §2Dodged!");
            
            // Grant XP for successful dodge
            skillManager.addXP(player.getUniqueId(), SkillType.ACROBATICS, 20);
            return;
        }
        
        // Swords - Counter Attack
        PlayerSkillData swordsData = playerDataManager.getPlayerData(player.getUniqueId())
            .getSkillData(SkillType.SWORDS);
        
        SwordsSkill swordsSkill = new SwordsSkill(plugin);
        ItemStack weapon = player.getItemInMainHand();
        
        if (weapon != null && isSword(weapon.getType().getName()) && 
            swordsSkill.shouldCounterAttack(swordsData.getLevel())) {
            
            // Reflect damage back
            double reflectedDamage = event.getDamage() * swordsSkill.getCounterDamage(swordsData.getLevel());
            attacker.damage(reflectedDamage);
            
            player.sendMessage("§e§l⚔ §6Counter Attack!");
        }
    }

    // Weapon type checking
    private boolean isSword(String itemType) {
        return itemType.contains("sword");
    }

    private boolean isAxe(String itemType) {
        return itemType.contains("axe") && !itemType.contains("pickaxe");
    }

    private boolean isBow(String itemType) {
        return itemType.contains("bow") || itemType.contains("crossbow");
    }

    // Effect application methods
    private void applyBleedEffect(LivingEntity target, int duration) {
        // TODO: Implement bleed effect using Hytale's potion/effect system
        // Apply damage over time for 'duration' ticks
    }

    private void applyDazeEffect(Player target, int duration) {
        // TODO: Implement daze effect (slowness, mining fatigue)
        target.sendMessage("§5§l⭐ §dYou are dazed!");
    }

    private void damageArmor(Player target, double damage) {
        // TODO: Damage all armor pieces
        // Iterate through armor slots and reduce durability
    }

    private void damageNearbyEntities(Player player, com.hypixel.hytale.world.Location location, double damage) {
        // TODO: Get entities in radius and damage them
        // Use location.getNearbyEntities(radius) and apply damage
    }
}
