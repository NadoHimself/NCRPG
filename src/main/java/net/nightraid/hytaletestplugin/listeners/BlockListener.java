package net.nightraid.hytaletestplugin.listeners;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.system.EcsEvent;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.ecs.BreakBlockEvent;
import com.hypixel.hytale.server.core.event.events.ecs.PlaceBlockEvent;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import net.nightraid.hytaletestplugin.HytaleTestPlugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * ECS Events (Entity Component System) work differently in Hytale.
 * They are triggered via ComponentAccessor.invoke() and must be registered
 * as part of an ECS System or Component.
 * 
 * This is an example handler that would be called by such a system.
 */
public class BlockListener {
    
    private final HytaleTestPlugin plugin;
    
    public BlockListener(HytaleTestPlugin plugin) {
        this.plugin = plugin;
    }
    
    /**
     * Called when a player breaks a block
     * Note: This must be registered via an ECS System
     */
    public void onBlockBreak(@Nonnull Ref<EntityStore> ref, @Nonnull EcsEvent ecsEvent) {
        if (!(ecsEvent instanceof BreakBlockEvent)) return;
        
        BreakBlockEvent event = (BreakBlockEvent) ecsEvent;
        
        Vector3i blockPos = event.getTargetBlock();
        BlockType blockType = event.getBlockType();
        ItemStack itemInHand = event.getItemInHand();
        
        plugin.getLogger().info("Block broken at " + blockPos + ": " + blockType.getKey());
        
        // Example: Prevent breaking certain blocks
        if (blockType.getKey().equals("hytale:bedrock")) {
            event.setCancelled(true);
            plugin.getLogger().info("Prevented bedrock breaking!");
        }
    }
    
    /**
     * Called when a player places a block
     * Note: This must be registered via an ECS System
     */
    public void onBlockPlace(@Nonnull Ref<EntityStore> ref, @Nonnull EcsEvent ecsEvent) {
        if (!(ecsEvent instanceof PlaceBlockEvent)) return;
        
        PlaceBlockEvent event = (PlaceBlockEvent) ecsEvent;
        
        Vector3i blockPos = event.getTargetBlock();
        ItemStack itemInHand = event.getItemInHand();
        
        plugin.getLogger().info("Block placed at " + blockPos);
        
        // Example: Limit building height
        if (blockPos.y > 256) {
            event.setCancelled(true);
            plugin.getLogger().info("Block placement too high!");
        }
    }
}
