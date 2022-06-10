package net.galaxycore.borderrun.phases

import com.okkero.skedule.SynchronizationContext
import com.okkero.skedule.schedule
import net.galaxycore.borderrun.PluginInstance
import net.galaxycore.borderrun.cache.OnlinePlayerCache
import net.galaxycore.borderrun.components.*
import net.galaxycore.borderrun.game.Phase
import net.galaxycore.borderrun.game.game
import net.galaxycore.borderrun.utils.d
import net.galaxycore.borderrun.utils.gI18N
import net.galaxycore.borderrun.utils.w
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit

class LobbyPhase : Phase() {

    var neededPlayers: Int = 0

    override fun enable() {
        listenWith(
            NoBlockModificationComponent::class.java,
            StartGameIfEnoughPlayersComponent::class.java,
            TeleportToSpawnComponent::class.java,
            NoHealthModificationComponent::class.java,
            CancelGameIfTooLittlePlayersComponent::class.java,
        )

        neededPlayers = try {
            PluginInstance.configNamespace.get("lobby.needed_players").toInt()
        } catch (nfe: java.lang.NumberFormatException) {
            w("Minumum needed Players for Lobby Phase are not set")
            2
        }
        d("Lobby Phase enabled")


        CancelGameIfTooLittlePlayersComponent.currentMinPlayers = neededPlayers
        CancelGameIfTooLittlePlayersComponent.shouldEnd = false

        startActionBar()

        Bukkit.getOnlinePlayers().forEach { it.inventory.clear() }
    }

    override fun disable() {
        d("Lobby Phase disabled")
    }

    fun onCountdownStart() {
        PluginInstance.listenerPool.activate(mutableListOf(CancelGameIfTooLittlePlayersComponent::class.java).toTypedArray())
        PluginInstance.listenerPool.deactivate(mutableListOf(StartGameIfEnoughPlayersComponent::class.java).toTypedArray())
    }

    fun onCountdownCancel() {
        PluginInstance.listenerPool.deactivate(mutableListOf(CancelGameIfTooLittlePlayersComponent::class.java).toTypedArray())
        PluginInstance.listenerPool.activate(mutableListOf(StartGameIfEnoughPlayersComponent::class.java).toTypedArray())
        startActionBar()
    }

    private fun startActionBar() {
        Bukkit.getScheduler().schedule(PluginInstance, SynchronizationContext.ASYNC) {
            repeating(20)

            while (!game.isRunning) {

                Bukkit.getOnlinePlayers().forEach {
                    it.sendActionBar(
                        "phase.lobby.actionbar".gI18N(
                            it, hashMapOf(
                                "current" to Component.text(OnlinePlayerCache.instance.onlinePlayers.size),
                                "of" to Component.text(neededPlayers)
                            )
                        )
                    )
                }

                yield()
            }
        }
    }
}