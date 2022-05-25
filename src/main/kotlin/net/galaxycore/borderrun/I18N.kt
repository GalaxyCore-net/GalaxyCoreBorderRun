package net.galaxycore.borderrun

import net.galaxycore.borderrun.utils.i18nDE
import net.galaxycore.borderrun.utils.i18nEN

const val time = "%time%"

fun registerI18NDE() {
    fun i18n(key: String, value: String, prefix: Boolean) {
        i18nDE(key, value, prefix)
    }

    i18n("phase.lobby.counter", "§eDas Spiel start in $time", true)
    i18n("phase.lobby.counter.actionbar", "§e$time", false)
    i18n("phase.lobby.joined", "§a%player% §ejoined (§a%players%§e/§a%needed_players%§e)", true)

    i18n("phase.prep.counter", "§eDu kannst in $time loslaufen", true)
    i18n("phase.prep.counter.actionbar", "§eBereite dich vor! | $time", false)

    i18n("phase.game.counter", "§eDas Spiel endet in $time", true)
    i18n("phase.game.counter.actionbar", "§e$time", false)

    i18n("phase.end.counter", "§eDer Server schließt in $time", true)
    i18n("phase.end.counter.actionbar", "§eDer Server schließt in $time", false)
}

fun registerI18NEN() {
    fun i18n(key: String, value: String, prefix: Boolean) {
        i18nEN(key, value, prefix)
    }

    i18n("phase.lobby.counter", "§eStarting in $time", true)
    i18n("phase.lobby.counter.actionbar", "§e$time", false)
    i18n("phase.lobby.joined", "§a%player% §ehat den Server betreten (§a%players%§e/§a%needed_players%§e)", true)

    i18n("phase.prep.counter", "§eYou can start running in $time", true)
    i18n("phase.prep.counter.actionbar", "§ePrepare yourself! | $time", false)

    i18n("phase.game.counter", "§eThe game ends in $time", true)
    i18n("phase.game.counter.actionbar", "§e$time", false)

    i18n("phase.end.counter", "§eThe server closes in $time", true)
    i18n("phase.end.counter.actionbar", "§eThe server closes in $time", false)
}