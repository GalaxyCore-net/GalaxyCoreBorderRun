@file:Suppress("unused")

package net.galaxycore.borderrun.game

class GamePhaseSystemBuilder {
    private val baseGamePhases = mutableListOf<BaseGamePhase>()
    fun build() = GamePhaseSystem(*baseGamePhases.toTypedArray())
    fun phase(length: Long, builder: GamePhaseBuilder.() -> Unit) {
        baseGamePhases += GamePhaseBuilder(length).apply(builder).build()
    }
}

class GamePhaseBuilder(private val length: Long) {
    private var start: (() -> Unit)? = null
    private var end: (() -> Unit)? = null
    private var cancel: (() -> Unit)? = null
    private var counterMessage: ((secondsLeft: Long) -> String)? = null
    private var key: String? = null
    private var keyActionBar: String? = null
    fun start(callback: () -> Unit) {
        start = callback
    }

    fun end(callback: () -> Unit) {
        end = callback
    }

    fun cancel(callback: () -> Unit) {
        cancel = callback
    }

    fun counterMessageTime(callback: (secondsLeft: Long) -> String) {
        counterMessage = callback
    }

    fun counterMessageKey(key: String?) {
        this.key = key
    }

    fun counterActionBarMessageKey(key: String?) {
        this.keyActionBar = key
    }

    private fun createCounterMessageCallback(
    ) {
        counterMessage = buildCounterMessageCallback()
    }

    fun build(): BaseGamePhase {
        if (counterMessage == null) {
            createCounterMessageCallback()
        }

        return BaseGamePhase(length, start, end, cancel, key, keyActionBar, counterMessage!!)
    }
}

fun buildGame(builder: GamePhaseSystemBuilder.() -> Unit) = GamePhaseSystemBuilder().apply(builder).build()