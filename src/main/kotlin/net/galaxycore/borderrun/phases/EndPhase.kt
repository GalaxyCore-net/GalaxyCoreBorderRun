package net.galaxycore.borderrun.phases

import net.galaxycore.borderrun.components.CancelGameIfTooLittlePlayersComponent
import net.galaxycore.borderrun.components.NoHealthModificationComponent
import net.galaxycore.borderrun.components.ProhibitSpawnComponent
import net.galaxycore.borderrun.game.Phase
import net.galaxycore.borderrun.utils.d
import org.bukkit.Bukkit

class EndPhase : Phase() {

    override fun enable() {
        listenWith(
            NoHealthModificationComponent::class.java,
            ProhibitSpawnComponent::class.java,
        )
        CancelGameIfTooLittlePlayersComponent.currentMinPlayers = 0
        CancelGameIfTooLittlePlayersComponent.shouldEnd = false
        Bukkit.getOnlinePlayers().forEach { it.inventory.clear() }
        d("End Phase enabled")
    }

    override fun disable() {
        d("End Phase disabled")
        Bukkit.getServer().shutdown()
    }

}