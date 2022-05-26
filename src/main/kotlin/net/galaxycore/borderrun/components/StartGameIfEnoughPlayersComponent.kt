package net.galaxycore.borderrun.components

import net.galaxycore.borderrun.cache.OnlinePlayerCache
import net.galaxycore.borderrun.game.game
import net.galaxycore.borderrun.utils.d
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class StartGameIfEnoughPlayersComponent : Listener {

    @EventHandler
    fun onCacheUpdate(event: OnlinePlayerCache.OnlinePlayerUpdateEvent) {
        d("Joined")
        if (event.players.size >= CancelGameIfTooLittlePlayersComponent.currentMinPlayers) {
            game.begin()
        }
    }

}