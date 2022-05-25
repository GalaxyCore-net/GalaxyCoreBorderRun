package net.galaxycore.borderrun.phases

import net.galaxycore.borderrun.PluginInstance
import net.galaxycore.borderrun.components.LobbyJoinComponent
import net.galaxycore.borderrun.components.NoBlockModificationComponent
import net.galaxycore.borderrun.components.NoHealthModificationComponent
import net.galaxycore.borderrun.components.TeleportToSpawnComponent
import net.galaxycore.borderrun.game.Phase
import net.galaxycore.borderrun.utils.d
import net.galaxycore.borderrun.utils.w

class LobbyPhase : Phase() {

    var players: Int = 0
    var neededPlayers: Int = 0

    override fun onEnable() {
        listenWith(
            NoBlockModificationComponent::class.java,
            LobbyJoinComponent::class.java,
            TeleportToSpawnComponent::class.java,
            NoHealthModificationComponent::class.java,
        )

        neededPlayers = try {
            PluginInstance.configNamespace.get("lobby.needed_players").toInt()
        } catch (nfe: java.lang.NumberFormatException) {
            w("Minumum needed Players for Lobby Phase are not set")
            2
        }
        d("Lobby Phase enabled")
    }

    override fun onDisable() {
        d("Lobby Phase disabled")
    }
}