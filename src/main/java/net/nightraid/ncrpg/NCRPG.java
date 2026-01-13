package net.nightraid.ncrpg;

// NOTE: Replace with Hytale API imports
// import com.hytale.api.plugin.Plugin;
// import com.hytale.api.Server;

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
import java.util.logging.Logger;

/**
 * Main plugin class for NCRPG (NightRaid RPG Skills System)
 * Complete mcMMO alternative for Hytale
 */
public class NCRPG /* extends Plugin */ {

    private static NCRPG instance;
    private Logger logger;

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

    /**
     * Plugin enable method
     * NOTE: Replace with Hytale API onEnable()
     */
    public void onEnable() {
        instance = this;
        logger = getLogger();

        logger.info("========================================");
        logger.info("  NCRPG - NightRaid RPG Skills System  ");
        logger.info("  Version: 1.0.0                       ");
        logger.info("  Author: NightRaid.net                ");
        logger.info("========================================");

        // Initialize managers
        initializeManagers();

        // Register commands
        registerCommands();

        // Register listeners
        registerListeners();

        // Start auto-save task
        startAutoSaveTask();

        logger.info("NCRPG has been enabled successfully!");
    }

    /**
     * Plugin disable method
     * NOTE: Replace with Hytale API onDisable()
     */
    public void onDisable() {
        logger.info("Saving all player data...");

        // Save all cached player data
        if (playerDataManager != null) {
            playerDataManager.saveAll();
        }

        // Shutdown scheduler
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
            try {
                if (!scheduler.awaitTermination(10, TimeUnit.SECONDS)) {
                    scheduler.shutdownNow();
                }
            } catch (InterruptedException e) {
                scheduler.shutdownNow();
            }
        }

        // Close database connection
        if (databaseManager != null) {
            databaseManager.close();
        }

        logger.info("NCRPG has been disabled!");
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
     * Register all commands
     * NOTE: Replace with Hytale API command registration
     */
    private void registerCommands() {
        logger.info("Registering commands...");

        // NOTE: Replace with Hytale API
        // getCommand("skills").setExecutor(new SkillsCommand(this));
        // getCommand("stats").setExecutor(new StatsCommand(this));
        // getCommand("mcrank").setExecutor(new MCRankCommand(this));
        // getCommand("party").setExecutor(new PartyCommand(this));

        new SkillsCommand(this);
        new StatsCommand(this);
        new MCRankCommand(this);
        new PartyCommand(this);

        logger.info("Commands registered successfully!");
    }

    /**
     * Register all event listeners
     * NOTE: Replace with Hytale API event registration
     */
    private void registerListeners() {
        logger.info("Registering event listeners...");

        // NOTE: Replace with Hytale API
        // PluginManager pm = getServer().getPluginManager();
        // pm.registerEvents(new BlockBreakListener(this), this);
        // pm.registerEvents(new EntityDamageListener(this), this);
        // pm.registerEvents(new PlayerFishListener(this), this);
        // pm.registerEvents(new PlayerHarvestListener(this), this);
        // pm.registerEvents(new PlayerJoinListener(this), this);
        // pm.registerEvents(new PlayerQuitListener(this), this);

        new BlockBreakListener(this);
        new EntityDamageListener(this);
        new PlayerFishListener(this);
        new PlayerHarvestListener(this);
        new PlayerJoinListener(this);
        new PlayerQuitListener(this);

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
            playerDataManager.saveAll();
            logger.info("Auto-save complete!");
        }, intervalMinutes, intervalMinutes, TimeUnit.MINUTES);

        logger.info("Auto-save task started (every " + intervalMinutes + " minutes)");
    }

    // Getters

    public static NCRPG getInstance() {
        return instance;
    }

    public Logger getLogger() {
        // NOTE: Replace with Hytale API logger
        // return super.getLogger();
        return Logger.getLogger("NCRPG");
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
