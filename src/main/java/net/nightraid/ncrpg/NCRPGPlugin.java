package net.nightraid.ncrpg;

import net.nightraid.ncrpg.listeners.PlayerListener;
import net.nightraid.ncrpg.listeners.BlockListener;

public class NCRPGPlugin {
    
    private static NCRPGPlugin instance;
    
    public NCRPGPlugin() {
        instance = this;
    }
    
    public void onEnable() {
        System.out.println("[NCRPG] Plugin wird aktiviert...");
        
        // Listener registrieren (Platzhalter - echte Registrierung abhängig von Hytale API)
        PlayerListener playerListener = new PlayerListener();
        BlockListener blockListener = new BlockListener();
        
        System.out.println("[NCRPG] Plugin erfolgreich aktiviert!");
    }
    
    public void onDisable() {
        System.out.println("[NCRPG] Plugin wird deaktiviert...");
    }
    
    public static NCRPGPlugin getInstance() {
        return instance;
    }
}
