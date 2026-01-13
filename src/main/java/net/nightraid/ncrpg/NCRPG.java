package net.nightraid.ncrpg;

import com.hypixel.hytale.plugin.HytalePlugin;
import com.hypixel.hytale.event.EventManager;
import com.hypixel.hytale.command.CommandManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.nightraid.ncrpg.abilities.AbilityCooldownManager;
import net.nightraid.ncrpg.abilities.ActiveAbilityManager;
import net.nightraid.ncrpg.commands.MCRankCommand;
import net.nightraid.ncrpg.commands.PartyCommand;
import net.nightraid.ncrpg.commands.SkillsCommand;
import net.nightraid.ncrpg.commands.StatsCommand;
import net.nightraid.ncrpg.config.ConfigManager;
import net.nightraid.ncrpg.database.DatabaseManager;
import net.nightraid.ncrpg.listeners.*;
import net.nightraid.ncrpg.managers.PartyManager;
import net.nightraid.ncrpg.managers.PlayerDataManager;
import net.nightraid.ncrpg.managers.SkillManager;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Main plugin class for NCRPG (NightRaid RPG Skills System)
 * Complete mcMMO alternative for Hytale
 * 
 * @author NightRaid.net
 * @version 1.0.0
 */
public class NCRPG extends HytalePlugin {

    private static NCRPG instance;
    private static final Logger logger = LoggerFactory.getLogger("NCRPG");

    // Managers
    private ConfigManager configManager;
    private DatabaseManager databaseManager;
    private SkillManager skillManager;
    private PlayerDataManager playerDataManager;
    private PartyManager partyManager;
    private AbilityCooldownManager abilityCooldownManager;
    private ActiveAbilityManager activeAbilityManager;

    // Scheduler
    private ScheduledExecutorService scheduler;

    @Override
    public void onEnable() {
        instance = this;

        logger.info("========================================");
        logger.info("  NCRPG - NightRaid RPG Skills System  ");
        logger.info("  Version: 1.0.0                       ");
        logger.info("  Author: NightRaid.net                ");
        logger.info("========================================");

        try {
            // Initialize managers
            initializeManagers();

            // Register commands
            registerCommands();

            // Register listeners
            registerListeners();

            // Start auto-save task
            startAutoSaveTask();

            logger.info("NCRPG has been enabled successfully!");
        } catch (Exception e) {
            logger.error("Failed to enable NCRPG!", e);
            throw new RuntimeException("Plugin initialization failed", e);
        }
    }

    @Override
    public void onDisable() {
        logger.info("Saving all player data...");

        try {
            // Save all cached player data
            if (playerDataManager != null) {
                playerDataManager.saveAll();
            }

            // Shutdown scheduler
            if (scheduler != null && !scheduler.isShutdown()) {
                scheduler.shutdown();
                if (!scheduler.awaitTermination(10, TimeUnit.SECONDS)) {
                    scheduler.shutdownNow();
                }
            }

            // Close database connection
            if (databaseManager != null) {
                databaseManager.close();
            }

            logger.info("NCRPG has been disabled successfully!");
        } catch (Exception e) {
            logger.error("Error during plugin shutdown", e);
        }
    }

    /**
     * Initialize all manager systems
     */
    private void initializeManagers() {
        logger.info("Initializing managers...");

        // Config manager
        configManager = new ConfigManager(this);
        configManager.loadConfig();

        // Database manager
        databaseManager = new DatabaseManager(this);
        databaseManager.connect();
        databaseManager.createTables();

        // Skill manager
        skillManager = new SkillManager(this);

        // Player data manager
        playerDataManager = new PlayerDataManager(this);

        // Party manager
        partyManager = new PartyManager(this);

        // Ability managers
        abilityCooldownManager = new AbilityCooldownManager();
        activeAbilityManager = new ActiveAbilityManager();

        logger.info("All managers initialized successfully!");
    }

    /**
     * Register all commands with Hytale's command system
     */
    private void registerCommands() {
        logger.info("Registering commands...");

        CommandManager commandManager = getServer().getCommandManager();
        
        commandManager.registerCommand("skills", new SkillsCommand(this));
        commandManager.registerCommand("stats", new StatsCommand(this));
        commandManager.registerCommand("mcrank", new MCRankCommand(this));
        commandManager.registerCommand("party", new PartyCommand(this));

        logger.info("Commands registered successfully!");
    }

    /**
     * Register all event listeners with Hytale's event system
     */
    private void registerListeners() {
        logger.info("Registering event listeners...");

        EventManager eventManager = getServer().getEventManager();
        
        eventManager.registerListener(new BlockBreakListener(this));
        eventManager.registerListener(new EntityDamageListener(this));
        eventManager.registerListener(new PlayerFishListener(this));
        eventManager.registerListener(new PlayerHarvestListener(this));
        eventManager.registerListener(new PlayerJoinListener(this));
        eventManager.registerListener(new PlayerQuitListener(this));

        logger.info("Event listeners registered successfully!");
    }

    /**
     * Start auto-save task (every 5 minutes by default)
     */
    private void startAutoSaveTask() {
        int intervalMinutes = configManager.getInt("general.auto-save-interval", 5);

        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            logger.info("Auto-saving player data...");
            try {
                playerDataManager.saveAll();
                logger.info("Auto-save complete!");
            } catch (Exception e) {
                logger.error("Error during auto-save", e);
            }
        }, intervalMinutes, intervalMinutes, TimeUnit.MINUTES);

        logger.info("Auto-save task started (every " + intervalMinutes + " minutes)");
    }

    // Getters

    public static NCRPG getInstance() {
        return instance;
    }

    public static Logger getPluginLogger() {
        return logger;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public SkillManager getSkillManager() {
        return skillManager;
    }

    public PlayerDataManager getPlayerDataManager() {
        return playerDataManager;
    }

    public PartyManager getPartyManager() {
        return partyManager;
    }

    public AbilityCooldownManager getAbilityCooldownManager() {
        return abilityCooldownManager;
    }

    public ActiveAbilityManager getActiveAbilityManager() {
        return activeAbilityManager;
    }
}
