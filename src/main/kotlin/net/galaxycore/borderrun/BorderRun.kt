package net.galaxycore.borderrun

import org.bukkit.plugin.java.JavaPlugin

class BorderRun : JavaPlugin() {
    override fun onEnable() {
        logger.info("Hello")
    }

    override fun onDisable() {
        logger.info("Bye")
    }
}