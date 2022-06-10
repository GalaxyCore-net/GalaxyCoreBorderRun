@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package net.galaxycore.borderrun.game

import net.galaxycore.borderrun.runnables.KSpigotRunnable
import net.galaxycore.borderrun.runnables.task
import net.galaxycore.borderrun.utils.broadcast
import net.galaxycore.borderrun.utils.buildTimer
import net.galaxycore.borderrun.utils.d
import net.galaxycore.borderrun.utils.gI18N
import net.kyori.adventure.text.Component.text
import org.bukkit.Bukkit
import kotlin.math.floor

class GamePhaseSystem(vararg val baseGamePhases: BaseGamePhase) {
    var gamePhases = baseGamePhases.toMutableList()
    var isRunning = false
    var currentPhase: BaseGamePhase? = null
    fun begin() {
        isRunning = true
        currentPhase = gamePhases.removeAt(0)
        currentPhase!!.startIt(gamePhases)
    }

    fun cancel() {
        currentPhase?.stopIt()
        isRunning = false
        currentPhase = null
        gamePhases = baseGamePhases.toMutableList()
    }

    fun skip() {
        currentPhase?.next()
    }

    fun skip(num: Int) {
        for (i in 0 until num) {
            skip()
        }
    }
}

fun buildCounterMessageCallback(): (Long) -> String = { curSeconds ->
    buildTimer(curSeconds.toInt())
}

class BaseGamePhase(
    val length: Long,
    val start: (() -> Unit)?,
    val end: (() -> Unit)?,
    val cancel: (() -> Unit)?,
    val counterMessageKey: String?,
    val counterMessageActionBarKey: String?,
    val counterMessage: ((secondsLeft: Long) -> String),
) {
    private var runnable: KSpigotRunnable? = null

    fun startIt(phaseQueue: MutableList<BaseGamePhase>) {
        start?.invoke()
        runnable = task(period = 20, howOften = (length / 20) + 1, endCallback = {
            end?.invoke()

            if (phaseQueue.isNotEmpty()) phaseQueue.removeAt(0).startIt(phaseQueue)
        }, cancelCallback = cancel) {
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

    fun stopIt() {
        d("Stopping phase...")
        d("$runnable")
        runnable?.stopWithCallback()
    }

    fun next() {
        runnable?.stopWithEndCallback()
    }

}

private val Long.isCounterValue: Boolean
    get() {
        if (this <= 60) {
            return when (this) {
                1L, 2L, 3L, 4L, 5L, 10L, 15L, 20L, 30L -> true
                0L -> false
                else -> this % 60 == 0L
            }
        }

        val minTime: Float = (this / 60F)
        if (minTime != floor(minTime)) return false

        return when (val minutes = (this % 3600) / 60) {
            1L, 2L, 3L, 4L, 5L, 10L, 15L, 20L, 30L -> true
            0L -> false
            else -> minutes % 60 == 0L
        }
    }