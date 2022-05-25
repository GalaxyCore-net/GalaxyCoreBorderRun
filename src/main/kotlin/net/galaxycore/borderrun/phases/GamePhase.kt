package net.galaxycore.borderrun.phases

import net.galaxycore.borderrun.components.NoHealthModificationComponent
import net.galaxycore.borderrun.game.Phase
import net.galaxycore.borderrun.utils.d

class GamePhase : Phase() {
    override fun onEnable() {
        listenWith(
            NoHealthModificationComponent::class.java
        )
        d("Game Phase enabled")
    }

    override fun onDisable() {
        d("Game Phase disabled")
    }
}