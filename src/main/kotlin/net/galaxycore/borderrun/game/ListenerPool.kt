package net.galaxycore.borderrun.game

import net.galaxycore.borderrun.PluginInstance
import net.galaxycore.borderrun.PluginManagerInst
import net.galaxycore.borderrun.utils.d
import net.galaxycore.borderrun.utils.w
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener

class ListenerPool {
    private val listeners: MutableSet<Listener> = mutableSetOf()
    private val activeListeners: MutableSet<Class<out Listener>> = mutableSetOf()

    private fun activate(listeners: Array<Class<out Listener>>) {
        val toCreate = listeners.filter { listener -> this.listeners.none { it.javaClass == listener.javaClass } }

        d("Activating ${toCreate.size} listeners")

        toCreate.forEach {
            val listener: Listener = try {
                val listener: Listener = it.getConstructor().newInstance()
                listener
            } catch (ignored: NoSuchMethodException) {
                w("Constructor for Listener ${it.simpleName} not found")
                return@forEach
            }

            this.listeners.add(listener)
            this.activeListeners.add(it)
            PluginManagerInst.registerEvents(listener, PluginInstance)
            d("Activated new Listener ${it.simpleName}")
        }

        val toActivate = this.listeners
            .filter { listener -> this.activeListeners.none { it == listener.javaClass } }
            .filter { listener -> listeners.any { it.javaClass == listener.javaClass } }

        toActivate.forEach {
            this.activeListeners.add(it.javaClass)
            PluginManagerInst.registerEvents(it, PluginInstance)
            d("Reactivated new Listener ${it.javaClass.simpleName}")
        }
    }

    private fun deactivate(listeners: Array<Class<out Listener>>) {
        val toRemove = this.listeners.stream()
            .filter { listener -> listeners.any { listener.javaClass == it.javaClass } }.toList()

        toRemove.forEach {
            HandlerList.unregisterAll(it)
            this.activeListeners.remove(it::class.java)
            d("Deactivated Listener ${it.javaClass.simpleName}")
        }
    }

    fun push(listeners: Array<out Class<out Listener>>) {
        val toActivate = listeners.filter { listener -> this.activeListeners.none { it == listener } }
        val toDeactivate = this.listeners
            .filter { listener -> listeners.none { it == listener::class.java } }
            .map { it::class.java }

        activate(toActivate.toTypedArray())
        deactivate(toDeactivate.toTypedArray())
    }
}