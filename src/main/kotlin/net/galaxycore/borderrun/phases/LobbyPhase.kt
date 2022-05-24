package net.galaxycore.borderrun.phases

import net.galaxycore.borderrun.components.NoDestroyLobbyComponent
import net.galaxycore.borderrun.game.Phase
import net.galaxycore.borderrun.utils.d

class LobbyPhase : Phase() {
    override fun onEnable() {
        listenWith(
            NoDestroyLobbyComponent::class.java
        )
    }

    override fun onDisable() {
        d("Lobby Phase disabled")
    }
}