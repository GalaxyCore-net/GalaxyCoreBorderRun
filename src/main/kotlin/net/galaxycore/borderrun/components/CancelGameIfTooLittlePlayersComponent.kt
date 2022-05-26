package net.galaxycore.borderrun.components

import net.galaxycore.borderrun.PluginInstance
import net.galaxycore.borderrun.cache.OnlinePlayerCache
import net.galaxycore.borderrun.utils.i
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

class CancelGameIfTooLittlePlayersComponent : Listener {
    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        val currentPlayers = OnlinePlayerCache.instance.onlinePlayers
        if (currentPlayers.size >= currentMinPlayers) return

        i("Triggering game cancel")

        if (shouldEnd) {
            //TODO: implement that
            Bukkit.getServer().shutdown()
        } else PluginInstance.game.cancel()
    }

    companion object {
        var currentMinPlayers: Int = 2
        var shouldEnd: Boolean = false
    }
}