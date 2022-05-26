package net.galaxycore.borderrun.game

import net.galaxycore.borderrun.PluginInstance
import net.galaxycore.borderrun.phases.LobbyPhase
import net.galaxycore.borderrun.utils.d
import net.galaxycore.borderrun.utils.seconds

fun getNewGame(): GamePhaseSystem {
    return buildGame {
        phase(30L.seconds) {
            counterMessageKey("phase.lobby.counter")
            counterActionBarMessageKey("phase.lobby.counter.actionbar")

            start {
                (PluginInstance.lobbyPhase as LobbyPhase).onCountdownStart()
            }

            cancel {
                d("Cancelled")
                (PluginInstance.lobbyPhase as LobbyPhase).onCountdownCancel()
            }

            end {
                PluginInstance.lobbyPhase.onDisable()
            }
        }

        phase(10L.seconds) {
            counterMessageKey("phase.prep.counter")
            counterActionBarMessageKey("phase.prep.counter.actionbar")

            start {
                PluginInstance.prepPhase.onEnable()
            }

            end {
                PluginInstance.prepPhase.onDisable()
            }
        }

        phase(30L.seconds) {
            counterMessageKey("phase.end.counter")
            counterActionBarMessageKey("phase.end.counter.actionbar")

            start {
                PluginInstance.endPhase.onEnable()
            }

            end {
                PluginInstance.endPhase.onDisable()
            }
        }
    }
}

val game: GamePhaseSystem
    get() = PluginInstance.game