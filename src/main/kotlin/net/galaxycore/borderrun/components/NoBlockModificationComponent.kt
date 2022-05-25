package net.galaxycore.borderrun.components

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.PlayerInteractEvent

class NoBlockModificationComponent : Listener {
    @EventHandler fun onBlockPlace(event: BlockPlaceEvent) {
        event.isCancelled = true
    }

    @EventHandler fun onBlockBreak(event: BlockBreakEvent) {
        event.isCancelled = true
    }

    @EventHandler fun onInteract(event: PlayerInteractEvent) {
        event.isCancelled = true
    }
}