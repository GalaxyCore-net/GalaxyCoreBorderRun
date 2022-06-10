package net.galaxycore.borderrun.components

import net.galaxycore.borderrun.PluginInstance
import net.galaxycore.borderrun.phases.GamePhase
import net.galaxycore.borderrun.phases.PreparePhase
import net.galaxycore.galaxycorecore.vanish.isVanished
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import kotlin.math.abs

class PlayerFailComponent : Listener {

    @EventHandler
    fun onPlayerDeath(event: EntityDeathEvent) {
        val gamePhase: GamePhase = (PluginInstance.gamePhase as GamePhase)
        val prepPhase: PreparePhase = (PluginInstance.prepPhase as PreparePhase)
        if (event.entity !is Player) {
            return
        }

        val player = event.entity as Player
        if (!player.isVanished) {
            val dist: Int = abs(player.location.blockZ - prepPhase.randomSpawn.blockZ)
            gamePhase.diedPlayers[player] = dist to gamePhase.timer

            gamePhase.sendDoneText(player, "fail")

            if ((gamePhase.diedPlayers.keys.filter { !it.isVanished }.size + gamePhase.finishedPlayers.keys.filter { !it.isVanished }.size) >= Bukkit.getOnlinePlayers().filter { !it.isVanished }.size) {
                gamePhase.finishGame()
            }

            player.gameMode = GameMode.SPECTATOR
            event.isCancelled = true

        }
    }

}