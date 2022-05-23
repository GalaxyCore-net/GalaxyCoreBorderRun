package net.galaxycore.borderrun

import org.bukkit.plugin.java.JavaPlugin
import java.util.logging.Logger

class BorderRun : JavaPlugin() {
    override fun onEnable() {
        log = logger
        i("Hello")
    }

    override fun onDisable() {
        i("Bye")
    }

    companion object {
        lateinit var log: Logger
    }
}