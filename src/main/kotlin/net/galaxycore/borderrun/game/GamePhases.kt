@file:Suppress("MemberVisibilityCanBePrivate")

package net.galaxycore.borderrun.game

import net.galaxycore.borderrun.runnables.task
import net.galaxycore.borderrun.utils.broadcast
import net.galaxycore.borderrun.utils.gI18N
import net.galaxycore.borderrun.utils.plusAssign
import net.kyori.adventure.text.Component.text
import org.bukkit.Bukkit

class GamePhaseSystem(vararg baseGamePhases: BaseGamePhase) {
    val gamePhases = baseGamePhases.toMutableList()
    fun begin() = gamePhases.removeAt(0).startIt(gamePhases)
}

fun buildCounterMessageCallback(
    hour: String = "h",
    minutes: String = "m",
    seconds: String = "s",
): (Long) -> String = { curSeconds ->
    StringBuilder().apply {
        val hourTime = (curSeconds / 3600)
        val minuteTime = ((curSeconds % 3600) / 60)
        val secondsTime = (curSeconds % 60)

        if (hourTime > 0) {
            this += hourTime
            this += hour
            this += " "
        }

        if (minuteTime > 0) {
            this += minuteTime
            this += minutes
            this += " "
        }

        if (secondsTime > 0) {
            this += secondsTime
            this += seconds
            this += " "
        }
    }.toString()
}

class BaseGamePhase(
    val length: Long,
    val start: (() -> Unit)?,
    val end: (() -> Unit)?,
    val counterMessageKey: String?,
    val counterMessageActionBarKey: String?,
    val counterMessage: ((secondsLeft: Long) -> String),
) {
    fun startIt(phaseQueue: MutableList<BaseGamePhase>) {
        start?.invoke()
        task(period = 20, howOften = (length / 20) + 1, endCallback = {
            end?.invoke()

            if (phaseQueue.isNotEmpty()) phaseQueue.removeAt(0).startIt(phaseQueue)
        }) {
            val currentCounter = it.counterDownToZero
            if (currentCounter?.isCounterValue == true && counterMessageKey != null && counterMessageKey != "") {
                broadcast(
                    counterMessageKey,
                    hashMapOf("time" to counterMessage.invoke(currentCounter))
                )
            }

            if (counterMessageActionBarKey != null && currentCounter != null) {
                for (player in Bukkit.getOnlinePlayers()) {
                    player.sendActionBar(
                        text(
                            counterMessageActionBarKey.gI18N(
                                player,
                                hashMapOf(
                                    "time" to counterMessage.invoke(currentCounter)
                                ),
                            )
                        )
                    )
                }
            }
        }
    }
}

private val Long.isCounterValue: Boolean
    get() = when (this) {
        1L, 2L, 3L, 4L, 5L, 10L, 15L, 20L, 30L -> true
        0L -> false
        else -> this % 60 == 0L
    }