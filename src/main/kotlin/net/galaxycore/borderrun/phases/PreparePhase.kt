package net.galaxycore.borderrun.phases

import net.galaxycore.borderrun.PluginInstance
import net.galaxycore.borderrun.components.CancelGameIfTooLittlePlayersComponent
import net.galaxycore.borderrun.components.NoHealthModificationComponent
import net.galaxycore.borderrun.game.Phase
import net.galaxycore.borderrun.utils.d

class PreparePhase : Phase() {
    override fun onEnable() {
        listenWith(
            NoHealthModificationComponent::class.java
        )
        CancelGameIfTooLittlePlayersComponent.currentMinPlayers = (PluginInstance.lobbyPhase as LobbyPhase).neededPlayers
        CancelGameIfTooLittlePlayersComponent.shouldEnd = true
        d("Prep Phase enabled")
    }

    override fun onDisable() {
        d("Prep Phase disabled")
    }
}