package net.galaxycore.borderrun.components

import net.galaxycore.borderrun.PluginInstance
import net.galaxycore.borderrun.game.game
import net.galaxycore.borderrun.phases.LobbyPhase
import net.galaxycore.borderrun.utils.broadcast
import net.galaxycore.galaxycorecore.vanish.isVanished
import net.kyori.adventure.text.TextComponent
import org.bukkit.GameMode
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class LobbyJoinComponent : Listener {

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        if (event.player.isVanished) return
        event.player.gameMode = GameMode.ADVENTURE
        val lobbyPhase = PluginInstance.lobbyPhase as LobbyPhase
        lobbyPhase.players += 1

        broadcast(
            "phase.lobby.joined",
            hashMapOf(
                "player" to (event.player.displayName() as TextComponent).content(),
                "players" to lobbyPhase.players.toString(),
                "needed_players" to lobbyPhase.neededPlayers.toString(),
            )
        ) // TODO: Stop GalaxyCoreCore from also sending this message

        if (lobbyPhase.players >= lobbyPhase.neededPlayers) game.begin()
    }

}