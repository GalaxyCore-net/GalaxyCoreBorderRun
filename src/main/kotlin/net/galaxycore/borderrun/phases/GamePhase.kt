package net.galaxycore.borderrun.phases

import com.okkero.skedule.SynchronizationContext
import com.okkero.skedule.schedule
import net.galaxycore.borderrun.PluginInstance
import net.galaxycore.borderrun.components.CancelGameIfTooLittlePlayersComponent
import net.galaxycore.borderrun.components.PlayerFailComponent
import net.galaxycore.borderrun.components.PlayerFinishComponent
import net.galaxycore.borderrun.game.Phase
import net.galaxycore.borderrun.game.game
import net.galaxycore.borderrun.runnables.task
import net.galaxycore.borderrun.utils.*
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class GamePhase : Phase() {

    private val showArrow: Boolean = try {
        PluginInstance.configNamespace["game.show_arrow"]!!.toString().equals("true", ignoreCase = false)
    } catch (exception: NullPointerException) {
        true
    }

    private val clearAbove: Int = try {
        PluginInstance.configNamespace["game.arrow.clear_above"]!!.toInt()
    } catch (exception: Exception) {
        when (exception) {
            is NullPointerException, is NumberFormatException -> {
                10
            }
            else -> throw exception
        }
    }

    private val type: String = try {
        PluginInstance.configNamespace["game.arrow.material"]!!
    } catch (exception: java.lang.NullPointerException) {
        "GOLD_BLOCK"
    }

    private val borderSize: Double = getDoubleFromConfig("game.border.size", 100000.0)

    private val damageAmount: Double = getDoubleFromConfig("game.border.damage", 20.0)

    private val initialBorderSpeed: Double = getDoubleFromConfig("game.border.initial_speed", 10.0) / 10

    private val maxSpeed: Double = getDoubleFromConfig("game.border.max_border_speed", 70.0) / 10 // A bit lower than Player Sprint + jump speed / block

    val range: Double = getDoubleFromConfig("game.range", 7500.0)

    private fun getDoubleFromConfig(key: String, default: Double): Double {
        return try {
            PluginInstance.configNamespace[key]!!.toDouble()
        } catch (exception: Exception) {
            when (exception) {
                is NullPointerException, is NumberFormatException -> {
                    default
                }
                else -> throw exception
            }
        }
    }

    val diedPlayers: HashMap<Player, Pair<Int, Int>> = hashMapOf() // Player, Block, Time

    val finishedPlayers: HashMap<Player, Int> = hashMapOf() // Player, Time

    var timer = 0
    private var stop: Boolean = false
    override fun enable() {
        listenWith(
            PlayerFailComponent::class.java,
            PlayerFinishComponent::class.java,
        )
        CancelGameIfTooLittlePlayersComponent.currentMinPlayers = (PluginInstance.lobbyPhase as LobbyPhase).neededPlayers
        startWorldBorder()
        if (showArrow) setArrow()

        d("Game Phase enabled")
        startTimer()
        Bukkit.getOnlinePlayers().forEach {
            it.gameMode = GameMode.SURVIVAL
            it.health = 20.0
            it.foodLevel = 20
            it.inventory.clear()

            val web = ItemStack(Material.COBWEB)
            web.amount = 5

            val stick = ItemStack(Material.STICK)
            stick.addUnsafeEnchantment(Enchantment.KNOCKBACK, 2)

            it.inventory.addItem(ItemStack(Material.ENDER_PEARL), web, stick, ItemStack(Material.WATER_BUCKET))
        }
    }

    private fun startTimer() {
        Bukkit.getScheduler().schedule(PluginInstance, SynchronizationContext.ASYNC) {
            repeating(1L.seconds)
            while (!stop) {
                timer++
                Bukkit.getOnlinePlayers().forEach {
                    it.sendActionBar(
                        "phase.game.counter.actionbar".gI18N(
                            it, hashMapOf(
                                "time" to Component.text("phase.game.counter.actionbar".gI18N(it, hashMapOf("" to "")).substring(0, 2) + buildTimer(timer))
                            )
                        )
                    )
                }
                yield()
            }
        }
    }

    private fun startWorldBorder() {
        val prepPhase: PreparePhase = (PluginInstance.prepPhase as PreparePhase)
        val world: World = prepPhase.world
        world.worldBorder.center = prepPhase.randomSpawn.clone().add(0.0, 0.0, (borderSize / 2) - (prepPhase.borderSize * 2))
        world.worldBorder.size = borderSize
        world.worldBorder.damageAmount = damageAmount
        var borderSpeed = initialBorderSpeed
        task(sync = true, period = 1) {
            world.worldBorder.center = world.worldBorder.center.add(0.0, 0.0, borderSpeed / 20)
        }

        task(sync = false, period = 1L.minutes) {
            if (borderSpeed <= maxSpeed) {
                borderSpeed = clamp(borderSpeed + .3, 0, maxSpeed).toDouble()
            }
        }
    }

    private fun setArrow() {
        clearBlocks(-3, 0, 3, 6)
        setBlockRelative(0, 0)
        setBlockRelative(0, 1)
        setBlockRelative(0, 2)

        setBlockRelative(3, 3)
        setBlockRelative(0, 3)
        setBlockRelative(-3, 3)

        setBlockRelative(2, 4)
        setBlockRelative(-2, 4)

        setBlockRelative(1, 5)
        setBlockRelative(-1, 5)

        setBlockRelative(0, 6)
    }

    private fun setBlockRelative(dX: Int, dZ: Int) {
        val prepPhase: PreparePhase = (PluginInstance.prepPhase as PreparePhase)
        val block = prepPhase.world.getBlockAt(prepPhase.randomSpawn.clone().add(dX.toDouble(), -1.0, dZ.toDouble()))
        // Delete i blocks above this location
        clearBlocks(dX, dZ, dX, dZ)
        // Change Block type
        block.type = Material.getMaterial(type) ?: Material.GOLD_BLOCK
    }

    private fun clearBlocks(dx1: Int, dz1: Int, dx2: Int, dz2: Int) {
        val prepPhase: PreparePhase = (PluginInstance.prepPhase as PreparePhase)
        val loc = prepPhase.randomSpawn.clone().subtract(0.0, 1.0, 0.0)
        for (x in dx1..dx2) {
            for (z in dz1..dz2) {
                for (y in 1..clearAbove) {
                    loc.clone().add(x.toDouble(), y.toDouble(), z.toDouble()).block.type = Material.AIR
                }
            }
        }
    }

    fun finishGame() {
        val bobTheBuilder: StringBuilder = StringBuilder()
        stop = true

        var place = 0
        d("$finishedPlayers")
        d("$diedPlayers")
        for (element in finishedPlayers.entries.reversed()) {
            place++
            val name = (element.key.displayName() as TextComponent).content()
            val time = buildTimer(element.value)
            bobTheBuilder.append("§e$place. §f$name §e(§f$time§e)\n")
        }

        for (element in diedPlayers.entries.reversed()) {
            place++
            val name = (element.key.displayName() as TextComponent).content()
            val time = buildTimer(element.value.second)
            bobTheBuilder.append("§e$place. §f$name §e(§f$time§e)\n")
        }

        broadcast(
            "phase.end.leaderboard.title", hashMapOf(
                "leaderboard" to bobTheBuilder.toString()
            )
        )

        game.skip()
    }

    fun sendDoneText(player: Player, i18nKey: String) {
        Bukkit.getOnlinePlayers().forEach {
            if (it == player) {
                it.sendTitle(
                    "phase.game.$i18nKey.title".gI18N(
                        it, hashMapOf(
                            "player" to player.name,
                            "time" to buildTimer(timer),
                            "position" to player.location.blockZ.toString()
                        )
                    ),
                    "phase.game.$i18nKey.subtitle".gI18N(
                        it, hashMapOf(
                            "player" to player.name,
                            "time" to buildTimer(timer),
                            "position" to player.location.blockZ.toString()
                        )
                    ),
                    10,
                    40,
                    10
                )
            } else {
                "phase.game.player_$i18nKey".sI18N(
                    it, hashMapOf(
                        "player" to player.name,
                        "time" to buildTimer(timer),
                        "position" to player.location.blockZ.toString()
                    )
                )
            }
        }
    }

    override fun disable() {
        d("Game Phase disabled")
    }

}