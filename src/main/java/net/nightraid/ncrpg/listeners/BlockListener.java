package net.nightraid.ncrpg.listeners;

import com.hypixel.hytale.server.core.event.events.ecs.BreakBlockEvent;

public class BlockListener {
    
    public BlockListener() {
        System.out.println("[NCRPG] BlockListener initialisiert");
    }
    
    // Event Handler für Block Break (Platzhalter)
    // Die echte Registrierung hängt von der Hytale Event API ab
    public void onBlockBreak(BreakBlockEvent event) {
        System.out.println("[NCRPG] Block wurde abgebaut: " + event.getBlockType());
    }
}
