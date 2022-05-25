package net.galaxycore.borderrun.components

import net.galaxycore.borderrun.PluginInstance
import net.galaxycore.borderrun.game.game
import net.galaxycore.borderrun.phases.LobbyPhase
import net.galaxycore.borderrun.utils.broadcast
import net.kyori.adventure.text.TextComponent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class LobbyJoinComponent : Listener {

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val lobbyPhase = PluginInstance.lobbyPhase as LobbyPhase
        lobbyPhase.players += 1

        broadcast(
            "phase.lobby.joined",
            hashMapOf(
                Pair("player", (event.player.displayName() as TextComponent).content()),
                Pair("players", lobbyPhase.players.toString()),
                Pair("needed_players", lobbyPhase.neededPlayers.toString()),
            )
        )

        if (lobbyPhase.players >= lobbyPhase.neededPlayers) game.begin()
    }

}