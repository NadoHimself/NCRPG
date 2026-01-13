package net.nightraid.ncrpg;

import com.hypixel.hytale.server.core.plugin.AbstractPlugin;
import net.nightraid.ncrpg.listeners.PlayerListener;
import net.nightraid.ncrpg.listeners.BlockListener;

public class NCRPGPlugin extends AbstractPlugin {
    
    @Override
    public void onLoad() {
        getLogger().info("NCRPG wird geladen...");
    }
    
    @Override
    public void onEnable() {
        getLogger().info("NCRPG aktiviert!");
        
        // Listener registrieren
        getEventBus().register(new PlayerListener(this));
        getEventBus().register(new BlockListener(this));
        
        getLogger().info("NCRPG Listener erfolgreich registriert!");
    }
    
    @Override
    public void onDisable() {
        getLogger().info("NCRPG deaktiviert!");
    }
}
