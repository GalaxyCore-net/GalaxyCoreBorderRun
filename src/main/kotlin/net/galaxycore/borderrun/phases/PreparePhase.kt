package net.galaxycore.borderrun.phases

import net.galaxycore.borderrun.components.NoHealthModificationComponent
import net.galaxycore.borderrun.game.Phase
import net.galaxycore.borderrun.utils.d

class PreparePhase : Phase() {
    override fun onEnable() {
        listenWith(
            NoHealthModificationComponent::class.java
        )
        d("Prep Phase enabled")
    }

    override fun onDisable() {
        d("Prep Phase disabled")
    }
}