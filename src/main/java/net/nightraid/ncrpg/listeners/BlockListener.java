package net.nightraid.ncrpg.listeners;

import com.hypixel.hytale.server.core.event.Subscribe;
import com.hypixel.hytale.server.core.event.events.block.BreakBlockEvent;
import net.nightraid.ncrpg.NCRPGPlugin;

public class BlockListener {
    
    private final NCRPGPlugin plugin;
    
    public BlockListener(NCRPGPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Subscribe
    public void onBlockBreak(BreakBlockEvent event) {
        plugin.getLogger().info("[NCRPG] Block wurde abgebaut!");
    }
}
