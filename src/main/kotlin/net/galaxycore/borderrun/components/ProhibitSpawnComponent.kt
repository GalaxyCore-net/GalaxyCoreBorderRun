package net.galaxycore.borderrun.components

import net.galaxycore.borderrun.utils.gI18N
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerKickEvent

class ProhibitSpawnComponent : Listener {
    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        event.player.kick("game.running".gI18N(event.player), PlayerKickEvent.Cause.PLUGIN)
    }
}