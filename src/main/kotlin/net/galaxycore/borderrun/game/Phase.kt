package net.galaxycore.borderrun.game

import net.galaxycore.borderrun.PluginInstance
import org.bukkit.event.Listener

abstract class Phase {
    private var activated = false
    private var wasLoaded = false
    private var wasDeactivated = false

    fun listenWith(vararg listeners: Class<out Listener>) {
        PluginInstance.listenerPool.push(listeners)
    }

    fun onEnable() {
        enable()
        wasLoaded = true
        activated = true
    }

    fun onDisable() {
        disable()
        wasDeactivated = false
        activated = false
    }

    abstract fun enable()
    abstract fun disable()
}