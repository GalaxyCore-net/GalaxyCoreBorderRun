@file:Suppress("unused")

package net.galaxycore.borderrun

import net.galaxycore.galaxycorecore.configuration.internationalisation.I18N
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextReplacementConfig
import org.bukkit.entity.Player


fun getTraceInfo(): String {
    val trace = Thread.currentThread().stackTrace
    if (trace.size <= 1) {
        return "[${Thread.currentThread().name}/?] "
    }

    val elem = trace[3]
    val clazz = Class.forName(elem.className)
    val formattedPackageName: StringBuilder = StringBuilder()

    for (component: String in clazz.packageName.split(".")) {
        formattedPackageName += "${component.substring(0, 2)}."
    }

    return "[${Thread.currentThread().name}/$formattedPackageName${clazz.simpleName}:${elem.lineNumber}] "
}

operator fun java.lang.StringBuilder.plusAssign(s: String) {
    this.append(s)
}

fun i(string: String) {
    BorderRun.log.info(getTraceInfo() + string)
}

fun d(string: String) {
    BorderRun.log.fine(getTraceInfo() + string)
}

fun w(string: String) {
    BorderRun.log.warning(getTraceInfo() + string)
}

fun e(string: String) {
    BorderRun.log.severe(getTraceInfo() + string)
}

val Int.seconds: Int
    get() = this * 20

val Int.minutes: Int
    get() = this.seconds * 60

val Int.hours: Int
    get() = this.minutes * 60

fun <T> T.applyIfNotNull(block: (T.() -> Unit)?): T {
    if (block != null) apply(block)
    return this
}

fun i18nDE(key: String, value: String, prefix: Boolean) {
    I18N.setDefaultByLang("de_DE", "capturetheblock.$key", value, prefix)
}

fun i18nEN(key: String, value: String, prefix: Boolean) {
    I18N.setDefaultByLang("en_GB", "capturetheblock.$key", value, prefix)
}

fun String.sI18N(player: Player) {
    player.sendMessage(I18N.getC(player, "capturetheblock.$this"))
}

fun String.sI18N(player: Player, replaceable: HashMap<String, String>) {
    val toReplace = hashMapOf<String, Component>()
    replaceable.forEach { toReplace[it.key] = Component.text(it.value) }
    this.sI18N(player, toReplace)
}

@JvmName("sI18NComp")
fun String.sI18N(player: Player, replaceable: HashMap<String, Component>) {
    var component = I18N.getC(player, "capturetheblock.$this")
    for (entry in replaceable) {
        component = component.replaceText(TextReplacementConfig.builder().match("%${entry.key}%").replacement(entry.value).build())
    }
    player.sendMessage(component)
}