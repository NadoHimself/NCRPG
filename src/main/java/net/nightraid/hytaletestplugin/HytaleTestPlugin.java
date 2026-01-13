package net.nightraid.hytaletestplugin;

import com.hypixel.hytale.server.core.plugin.AbstractPlugin;
import net.nightraid.hytaletestplugin.listeners.PlayerListener;
import net.nightraid.hytaletestplugin.listeners.BlockListener;

public class HytaleTestPlugin extends AbstractPlugin {
    
    @Override
    public void onLoad() {
        getLogger().info("HytaleTestPlugin wird geladen...");
    }
    
    @Override
    public void onEnable() {
        getLogger().info("HytaleTestPlugin aktiviert!");
        
        // Listener registrieren
        getEventBus().register(new PlayerListener(this));
        getEventBus().register(new BlockListener(this));
        
        getLogger().info("Listener erfolgreich registriert!");
    }
    
    @Override
    public void onDisable() {
        getLogger().info("HytaleTestPlugin deaktiviert!");
    }
}
