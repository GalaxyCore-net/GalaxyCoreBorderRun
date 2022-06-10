package net.galaxycore.borderrun.components

import net.galaxycore.borderrun.PluginInstance
import net.galaxycore.borderrun.phases.GamePhase
import net.galaxycore.borderrun.phases.PreparePhase
import net.galaxycore.borderrun.utils.d
import net.galaxycore.galaxycorecore.coins.CoinDAO
import net.galaxycore.galaxycorecore.configuration.PlayerLoader
import net.galaxycore.galaxycorecore.vanish.isVanished
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import kotlin.math.floor

class PlayerFinishComponent : Listener {

    private val baseMoney: Int = (try {
        PluginInstance.configNamespace["game.base_money"]!!
    } catch (exception: Exception) {
        when (exception) {
            is NullPointerException, is NumberFormatException -> "100"
            else -> throw exception
        }
    }).toInt()

    @EventHandler fun onMove(event: PlayerMoveEvent) {
        val gamePhase: GamePhase = (PluginInstance.gamePhase as GamePhase)
        val prepPhase: PreparePhase = (PluginInstance.prepPhase as PreparePhase)

        if (event.player.location.blockZ >= prepPhase.randomSpawn.blockZ + gamePhase.range && !event.player.isVanished && !gamePhase.finishedPlayers.containsKey(event.player)) {
            gamePhase.finishedPlayers[event.player] = gamePhase.timer

            val bonus = floor(((1800 - gamePhase.timer) / gamePhase.finishedPlayers.size).toDouble() / 10)
            d("${event.player.name} gets ${baseMoney + bonus} coins for finishing")
            val dao = CoinDAO(PlayerLoader.load(event.player), PluginInstance)
            dao.transact(null, -(baseMoney + bonus).toLong(), "BRUNGetCoinsForFinish")

            gamePhase.sendDoneText(event.player, "done")

            if ((gamePhase.diedPlayers.keys.filter { !it.isVanished }.size + gamePhase.finishedPlayers.keys.filter { !it.isVanished }.size) >= Bukkit.getOnlinePlayers().filter { !it.isVanished }.size) {
                gamePhase.finishGame()
            }

            event.player.gameMode = GameMode.SPECTATOR
        }
    }

}