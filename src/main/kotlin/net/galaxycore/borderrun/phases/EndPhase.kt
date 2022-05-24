package net.galaxycore.borderrun.phases

import net.galaxycore.borderrun.game.Phase
import net.galaxycore.borderrun.utils.d
import org.bukkit.Bukkit

class EndPhase : Phase() {

    override fun onEnable() {
        d("End Phase enabled")
    }

    override fun onDisable() {
        Bukkit.getServer().shutdown()
    }

}