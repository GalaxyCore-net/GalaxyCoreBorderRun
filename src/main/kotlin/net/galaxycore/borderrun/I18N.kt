package net.galaxycore.borderrun

import net.galaxycore.borderrun.utils.i18nDE
import net.galaxycore.borderrun.utils.i18nEN

const val time = "%time%"

fun registerI18NDE() {
    fun i18n(key: String, value: String, prefix: Boolean) {
        i18nDE(key, value, prefix)
    }

    i18n("nopermission", "§c✗§7 Du hast hierfür keine Rechte", true)

    i18n("phase.lobby.counter", "§eDas Spiel start in $time", true)
    i18n("phase.lobby.counter.actionbar", "§e$time", false)
    i18n("phase.lobby.actionbar", "§e(§a%current%§e/§a%of%§e)", false)

    i18n("phase.prep.counter", "§eDu kannst in $time loslaufen", true)
    i18n("phase.prep.counter.actionbar", "§eBereite dich vor! | $time", false)
    i18n("phase.prep.target", "§eDu musst zu Z: %target% rennen", true)

    i18n("phase.game.counter", "§eDas Spiel endet spätestens in $time", true)
    i18n("phase.game.counter.actionbar", "§e$time", false)
    i18n("phase.game.fail.title", "§c✗§eDu hast verloren", false)
    i18n("phase.game.fail.subtitle", "§eDu hast §f%time% überlebt", false)
    i18n("phase.game.player_fail", "§f%player% §ehat §f%time% überlebt, ist nun aber gestorben", false)
    i18n("phase.game.done.title", "§a✓§eDu hast es geschafft", false)
    i18n("phase.game.done.subtitle", "§eDu hast §f%time% gebraucht", false)
    i18n("phase.game.player_done", "§f%player% §ehat §f%time% gebraucht, um ins Ziel zu kommen", false)

    i18n("phase.end.counter", "§eDer Server schließt in $time", true)
    i18n("phase.end.counter.actionbar", "§eDer Server schließt in $time", false)
    i18n("phase.end.leaderboard.title", "§eDas Spiel ist zu Ende! Dies ist die Platzverteilung:\n%leaderboard%", false)

    i18n("command.start.success", "§e✓§7 Das Spiel wurde gestartet!", true)
    i18n("command.start.failure", "§c✗§7 Das Spiel ist bereits gestartet!", true)
}

fun registerI18NEN() {
    fun i18n(key: String, value: String, prefix: Boolean) {
        i18nEN(key, value, prefix)
    }

    i18n("nopermission", "§c✗§7 You don't have permission to do this", true)

    i18n("phase.lobby.counter", "§eStarting in $time", true)
    i18n("phase.lobby.counter.actionbar", "§e$time", false)
    i18n("phase.lobby.actionbar", "§e(§a%current%§e/§a%of%§e)", false)

    i18n("phase.prep.counter", "§eYou can start running in $time", true)
    i18n("phase.prep.counter.actionbar", "§ePrepare yourself! | $time", false)
    i18n("phase.prep.target", "§eYou have to run to Z: %target%", true)

    i18n("phase.game.counter", "§eThe game ends at most in $time", true)
    i18n("phase.game.counter.actionbar", "§e$time", false)
    i18n("phase.game.fail.title", "§c✗§eYou failed", false)
    i18n("phase.game.fail.subtitle", "§eYou survived §f%time%", false)
    i18n("phase.game.player_fail", "§f%player% §efailed in §f%time%", false)
    i18n("phase.game.done.title", "§a✓§eYou finished", false)
    i18n("phase.game.done.subtitle", "§eYou needed §f%time%", false)
    i18n("phase.game.player_done", "§f%player% §eneeded §f%time% to finish", false)

    i18n("phase.end.counter", "§eThe server closes in $time", true)
    i18n("phase.end.counter.actionbar", "§eThe server closes in $time", false)
    i18n("phase.end.leaderboard.title", "§eThe Game is Done! This is the current Leaderboard:\n%leaderboard%", false)

    i18n("command.start.success", "§e✓§7 Started the game!", true)
    i18n("command.start.failure", "§c✗§7 The game is already started!", true)
}