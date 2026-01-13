package net.nightraid.ncrpg.commands;

import net.nightraid.ncrpg.NCRPG;
import net.nightraid.ncrpg.models.PlayerSkillData;
import net.nightraid.ncrpg.models.SkillType;

import java.util.Map;
import java.util.UUID;

/**
 * Command to display all skill levels
 * Usage: /skills
 * NOTE: Replace with Hytale API command executor
 */
public class SkillsCommand /* implements CommandExecutor */ {

    private final NCRPG plugin;

    public SkillsCommand(NCRPG plugin) {
        this.plugin = plugin;
    }

    // NOTE: Replace with Hytale API
    // @Override
    // public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    //     if (!(sender instanceof Player)) {
    //         sender.sendMessage("§cThis command can only be used by players!");
    //         return true;
    //     }
    //
    //     Player player = (Player) sender;
    //     UUID uuid = player.getUniqueId();
    //
    //     // Check if data is loaded
    //     if (!plugin.getPlayerDataManager().isLoaded(uuid)) {
    //         player.sendMessage("§cYour data is still loading, please wait...");
    //         return true;
    //     }
    //
    //     displaySkills(player, uuid);
    //     return true;
    // }

    /**
     * Display skills to player
     */
    private void displaySkills(Object player, UUID uuid) {
        // NOTE: Replace with Hytale API messaging
        // player.sendMessage("§6§l========== §e§lYOUR SKILLS §6§l==========");
        // player.sendMessage("");

        double baseXP = plugin.getConfigManager().getDouble("general.xp-base-per-level", 1000.0);
        double exponent = plugin.getConfigManager().getDouble("general.xp-exponent", 1.5);

        Map<SkillType, PlayerSkillData> allSkills = plugin.getPlayerDataManager().getAllSkillData(uuid);
        
        int totalPowerLevel = 0;
        for (SkillType skill : SkillType.values()) {
            PlayerSkillData data = allSkills.get(skill);
            if (data == null) continue;

            int level = data.getLevel();
            totalPowerLevel += level;
            double progress = data.getProgressPercent(baseXP, exponent);

            // Create progress bar
            String progressBar = createProgressBar(progress, 20);

            // Send skill line
            // player.sendMessage(String.format("§7%s %s §aLv.%d §8[§7%s§8] §e%.1f%%", 
            //     skill.getIcon(), skill.getDisplayName(), level, progressBar, progress));
        }

        // player.sendMessage("");
        // player.sendMessage("§6Power Level: §e" + totalPowerLevel);
        // player.sendMessage("§6§l================================");
    }

    /**
     * Create visual progress bar
     */
    private String createProgressBar(double percent, int bars) {
        int filled = (int) ((percent / 100.0) * bars);
        StringBuilder bar = new StringBuilder();
        
        for (int i = 0; i < bars; i++) {
            if (i < filled) {
                bar.append("■");
            } else {
                bar.append("□");
            }
        }
        
        return bar.toString();
    }
}
