package net.nightraid.ncrpg;

import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import net.nightraid.ncrpg.listeners.PlayerListener;
import net.nightraid.ncrpg.listeners.BlockListener;

public class NCRPGPlugin extends JavaPlugin {
    
    private static NCRPGPlugin instance;
    
    @Override
    public void onEnable() {
        instance = this;
        System.out.println("[NCRPG] Plugin wird aktiviert...");
        
        // Listener registrieren
        PlayerListener playerListener = new PlayerListener();
        BlockListener blockListener = new BlockListener();
        
        System.out.println("[NCRPG] Plugin erfolgreich aktiviert!");
    }
    
    @Override
    public void onDisable() {
        System.out.println("[NCRPG] Plugin wird deaktiviert...");
    }
    
    public static NCRPGPlugin getInstance() {
        return instance;
    }
}
