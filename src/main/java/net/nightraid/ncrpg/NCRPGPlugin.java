package net.nightraid.ncrpg;

import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.logging.Logger;

public class NCRPGPlugin extends JavaPlugin {
    
    private static NCRPGPlugin instance;
    private Logger logger;
    
    // WICHTIG: Constructor mit JavaPluginInit Parameter ist erforderlich!
    public NCRPGPlugin(JavaPluginInit init) {
        super(init);
    }
    
    @Override
    public void onEnable() {
        instance = this;
        logger = getLogger();
        
        logger.info("═══════════════════════════════════════════");
        logger.info("  NCRPG Plugin v1.0.0-SNAPSHOT");
        logger.info("  Author: NightRaid");
        logger.info("  Status: ENABLING...");
        logger.info("═══════════════════════════════════════════");
        
        try {
            logger.info("Initialisiere Plugin-Komponenten...");
            
            // TODO: Event Listener registrieren
            // TODO: Commands registrieren
            // TODO: Datenbank-Verbindung aufbauen
            
            logger.info("✓ Plugin-Komponenten erfolgreich initialisiert!");
        } catch (Exception e) {
            logger.severe("✗ Fehler bei der Initialisierung!");
            e.printStackTrace();
        }
        
        logger.info("═══════════════════════════════════════════");
        logger.info("  NCRPG ERFOLGREICH AKTIVIERT! ✓");
        logger.info("═══════════════════════════════════════════");
    }
    
    @Override
    public void onDisable() {
        logger.info("═══════════════════════════════════════════");
        logger.info("  NCRPG Plugin v1.0.0-SNAPSHOT");
        logger.info("  Status: DISABLING...");
        logger.info("═══════════════════════════════════════════");
        
        try {
            logger.info("Führe Cleanup durch...");
            
            // TODO: Datenbank-Verbindungen schließen
            // TODO: Tasks stoppen
            // TODO: Ressourcen freigeben
            
            logger.info("✓ Cleanup erfolgreich abgeschlossen!");
        } catch (Exception e) {
            logger.severe("✗ Fehler beim Cleanup!");
            e.printStackTrace();
        }
        
        logger.info("═══════════════════════════════════════════");
        logger.info("  NCRPG ERFOLGREICH DEAKTIVIERT! ✓");
        logger.info("═══════════════════════════════════════════");
        
        instance = null;
    }
    
    public static NCRPGPlugin getInstance() {
        return instance;
    }
    
    public Logger getPluginLogger() {
        return logger;
    }
}
