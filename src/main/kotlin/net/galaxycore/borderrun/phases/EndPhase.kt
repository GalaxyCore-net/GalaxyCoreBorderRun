package net.galaxycore.borderrun.phases

import net.galaxycore.borderrun.components.NoHealthModificationComponent
import net.galaxycore.borderrun.game.Phase
import net.galaxycore.borderrun.utils.d
import org.bukkit.Bukkit

class EndPhase : Phase() {

    override fun onEnable() {
        listenWith(
            NoHealthModificationComponent::class.java,
        )
        d("End Phase enabled")
    }

    override fun onDisable() {
        d("End Phase disabled")
        Bukkit.getServer().shutdown()
    }

}