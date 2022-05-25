package net.galaxycore.borderrun.phases

import net.galaxycore.borderrun.game.Phase
import net.galaxycore.borderrun.utils.d

class PreparePhase : Phase() {
    override fun onEnable() {
        d("Prep Phase enabled")
    }

    override fun onDisable() {
        d("Prep Phase disabled")
    }
}