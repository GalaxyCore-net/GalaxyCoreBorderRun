package net.galaxycore.borderrun.cache

import net.galaxycore.borderrun.PluginManagerInst
import net.galaxycore.galaxycorecore.vanish.isVanished
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

data class OnlinePlayerCache(var onlinePlayers: MutableList<Player> = mutableListOf()) : Listener {

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        if (!event.player.isVanished) {
            onlinePlayers += event.player
            PluginManagerInst.callEvent(OnlinePlayerUpdateEvent(onlinePlayers))
        }
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        if (!event.player.isVanished) {
            onlinePlayers -= event.player
            PluginManagerInst.callEvent(OnlinePlayerUpdateEvent(onlinePlayers))
        }
    }

    @EventHandler
    fun onVanish(event: PlayerCommandPreprocessEvent) {
        if ((event.message.startsWith("/vanish") || event.message.startsWith("/v")) && event.player.hasPermission("core.vanish")) {
            if (event.player.isVanished) {
                onlinePlayers += event.player
                PluginManagerInst.callEvent(OnlinePlayerUpdateEvent(onlinePlayers))
            } else {
                onlinePlayers -= event.player
                PluginManagerInst.callEvent(OnlinePlayerUpdateEvent(onlinePlayers))
            }
        }
    }

    class OnlinePlayerUpdateEvent(val players: List<Player>) : Event() {
        override fun getHandlers(): HandlerList {
            return handlerList
        }

        companion object {
            @JvmStatic
            val handlerList = HandlerList()
        }
    }

    companion object {
        val instance = OnlinePlayerCache()
    }

}