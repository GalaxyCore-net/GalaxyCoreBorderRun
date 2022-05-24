package net.galaxycore.borderrun.game

import net.galaxycore.borderrun.PluginInstance
import org.bukkit.event.Listener

abstract class Phase {
    fun listenWith(vararg listeners: Class<out Listener>) {
        PluginInstance.listenerPool.push(listeners)
    }

    abstract fun onEnable()
    abstract fun onDisable()
}