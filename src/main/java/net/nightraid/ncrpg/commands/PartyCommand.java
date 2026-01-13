package net.nightraid.ncrpg.commands;

import net.nightraid.ncrpg.NCRPG;
import net.nightraid.ncrpg.party.Party;

import java.util.UUID;

/**
 * Command for party management
 * Usage: /party <create|invite|accept|leave|disband>
 * NOTE: Replace with Hytale API command executor
 */
public class PartyCommand /* implements CommandExecutor */ {

    private final NCRPG plugin;

    public PartyCommand(NCRPG plugin) {
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
    //     
    //     if (args.length == 0) {
    //         sendHelp(player);
    //         return true;
    //     }
    //
    //     String subCommand = args[0].toLowerCase();
    //     switch (subCommand) {
    //         case "create" -> handleCreate(player);
    //         case "invite" -> handleInvite(player, args);
    //         case "accept" -> handleAccept(player);
    //         case "leave" -> handleLeave(player);
    //         case "disband" -> handleDisband(player);
    //         default -> sendHelp(player);
    //     }
    //
    //     return true;
    // }

    private void handleCreate(Object player) {
        // NOTE: Implement with Hytale API
        // UUID uuid = player.getUniqueId();
        // Party party = plugin.getPartyManager().createParty(uuid, player.getName());
        // if (party != null) {
        //     player.sendMessage("§aParty created! You are now the leader.");
        // } else {
        //     player.sendMessage("§cYou are already in a party!");
        // }
    }

    private void handleInvite(Object player, String[] args) {
        // NOTE: Implement with Hytale API
    }

    private void handleAccept(Object player) {
        // NOTE: Implement with Hytale API
    }

    private void handleLeave(Object player) {
        // NOTE: Implement with Hytale API
    }

    private void handleDisband(Object player) {
        // NOTE: Implement with Hytale API
    }

    private void sendHelp(Object player) {
        // NOTE: Replace with Hytale API messaging
        // player.sendMessage("§6§lPARTY COMMANDS:");
        // player.sendMessage("§7/party create §8- §aCreate a party");
        // player.sendMessage("§7/party invite <player> §8- §aInvite a player");
        // player.sendMessage("§7/party accept §8- §aAccept party invite");
        // player.sendMessage("§7/party leave §8- §aLeave your party");
        // player.sendMessage("§7/party disband §8- §aDisband your party (leader only)");
    }
}
