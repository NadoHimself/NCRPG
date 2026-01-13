package net.nightraid.ncrpg.commands;

import net.nightraid.ncrpg.NCRPG;
import net.nightraid.ncrpg.models.SkillType;

import java.util.List;
import java.util.Map;

/**
 * Command to view leaderboards
 * Usage: /mcrank [skill]
 * NOTE: Replace with Hytale API command executor
 */
public class MCRankCommand /* implements CommandExecutor */ {

    private final NCRPG plugin;

    public MCRankCommand(NCRPG plugin) {
        this.plugin = plugin;
    }

    // NOTE: Replace with Hytale API
    // @Override
    // public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    //     if (args.length == 0) {
    //         // Show power level leaderboard
    //         displayPowerLevelLeaderboard(sender);
    //     } else {
    //         // Show specific skill leaderboard
    //         SkillType skill = SkillType.fromString(args[0]);
    //         if (skill == null) {
    //             sender.sendMessage("§cInvalid skill!");
    //             return true;
    //         }
    //         displaySkillLeaderboard(sender, skill);
    //     }
    //     return true;
    // }

    /**
     * Display power level leaderboard
     */
    private void displayPowerLevelLeaderboard(Object sender) {
        List<Map.Entry<String, Integer>> leaderboard = plugin.getDatabaseManager().getPowerLevelLeaderboard(10);

        // NOTE: Replace with Hytale API messaging
        // sender.sendMessage("§6§l========== §e§lPOWER LEVEL TOP 10 §6§l==========");
        // sender.sendMessage("");

        int rank = 1;
        for (Map.Entry<String, Integer> entry : leaderboard) {
            String medal = getMedal(rank);
            // sender.sendMessage(String.format("%s §7#%d §e%s §8- §a%d", medal, rank, entry.getKey(), entry.getValue()));
            rank++;
        }

        // sender.sendMessage("§6§l================================");
    }

    /**
     * Display skill leaderboard
     */
    private void displaySkillLeaderboard(Object sender, SkillType skill) {
        List<Map.Entry<String, Integer>> leaderboard = plugin.getDatabaseManager().getSkillLeaderboard(skill, 10);

        // NOTE: Replace with Hytale API messaging
        // sender.sendMessage("§6§l===== §e§l" + skill.getDisplayName().toUpperCase() + " TOP 10 §6§l=====");
        // sender.sendMessage("");

        int rank = 1;
        for (Map.Entry<String, Integer> entry : leaderboard) {
            String medal = getMedal(rank);
            // sender.sendMessage(String.format("%s §7#%d §e%s §8- §aLv.%d", medal, rank, entry.getKey(), entry.getValue()));
            rank++;
        }

        // sender.sendMessage("§6§l================================");
    }

    /**
     * Get medal emoji for top 3
     */
    private String getMedal(int rank) {
        return switch (rank) {
            case 1 -> "🥇";
            case 2 -> "🥈";
            case 3 -> "🥉";
            default -> "";
        };
    }
}
