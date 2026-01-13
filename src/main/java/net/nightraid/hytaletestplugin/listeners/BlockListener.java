package net.nightraid.hytaletestplugin.listeners;

import com.hypixel.hytale.server.core.event.Subscribe;
import com.hypixel.hytale.server.core.event.events.block.BreakBlockEvent;
import net.nightraid.hytaletestplugin.HytaleTestPlugin;

public class BlockListener {
    
    private final HytaleTestPlugin plugin;
    
    public BlockListener(HytaleTestPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Subscribe
    public void onBlockBreak(BreakBlockEvent event) {
        plugin.getLogger().info("Block wurde abgebaut!");
    }
}
