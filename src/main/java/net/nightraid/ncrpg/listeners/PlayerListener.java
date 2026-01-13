package net.nightraid.ncrpg.listeners;

import com.hypixel.hytale.server.core.event.events.player.PlayerConnectEvent;

public class PlayerListener {
    
    public PlayerListener() {
        System.out.println("[NCRPG] PlayerListener initialisiert");
    }
    
    // Event Handler für Player Connect (Platzhalter)
    // Die echte Registrierung hängt von der Hytale Event API ab
    public void onPlayerConnect(PlayerConnectEvent event) {
        System.out.println("[NCRPG] Spieler verbunden: " + event.getPlayerRef().getUsername());
    }
}
