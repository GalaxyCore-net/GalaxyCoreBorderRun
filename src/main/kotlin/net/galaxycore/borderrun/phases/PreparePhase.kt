package net.galaxycore.borderrun.phases

import net.galaxycore.borderrun.PluginInstance
import net.galaxycore.borderrun.components.CancelGameIfTooLittlePlayersComponent
import net.galaxycore.borderrun.components.NoHealthModificationComponent
import net.galaxycore.borderrun.game.Phase
import net.galaxycore.borderrun.runnables.task
import net.galaxycore.borderrun.utils.d
import net.galaxycore.borderrun.utils.seconds
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import kotlin.math.pow
import kotlin.random.Random

class PreparePhase : Phase() {

    val world: World = Bukkit.getWorld(
        try {
            PluginInstance.configNamespace["game.world_name"]!!
        } catch (exception: NullPointerException) {
            "world"
        }
    )!!

    val borderSize: Double = (try {
        PluginInstance.configNamespace["game.border_size"]!!
    } catch (exception: NullPointerException) {
        "25"
    }).toDouble()

    private val finishLineSize: Int = (try {
        PluginInstance.configNamespace["game.finish_line_size"]!!
    } catch (exception: Exception) {
        when (exception) {
            is NullPointerException, is NumberFormatException -> "100"
            else -> throw exception
        }
    }).toInt()

    lateinit var randomSpawn: Location
    override fun enable() {
        listenWith(
            NoHealthModificationComponent::class.java
        )
        CancelGameIfTooLittlePlayersComponent.currentMinPlayers = (PluginInstance.lobbyPhase as LobbyPhase).neededPlayers
        CancelGameIfTooLittlePlayersComponent.shouldEnd = true
        randomSpawn = teleportPlayersToRandomSpawn()
        setWorldBorder(randomSpawn)
        task(delay = 3L.seconds) { setFinishLine(randomSpawn) }
        Bukkit.getOnlinePlayers().forEach { it.inventory.clear() }

        d("Prep Phase enabled")
    }

    private fun teleportPlayersToRandomSpawn(): Location {
        val x = Random.nextInt(-((2.0).pow(16.0)).toInt(), ((2.0).pow(16.0)).toInt())
        val z = Random.nextInt(-((2.0).pow(16.0)).toInt(), ((2.0).pow(16.0)).toInt())
        val y = world.getHighestBlockYAt(x, z) + 1
        val loc = Location(world, x.toDouble() + .5, y.toDouble(), z.toDouble() + .5, 0F, 0F)
        if (loc.clone().subtract(0.0, 1.0, 0.0).block.type == Material.WATER ||
            loc.block.biome.toString().contains("ocean", ignoreCase = true)
        ) {
            return teleportPlayersToRandomSpawn()
        }
        world.spawnLocation = loc
        Bukkit.getOnlinePlayers().forEach { it.teleport(loc) }
        return loc
    }

    private fun setWorldBorder(spawn: Location) {
        spawn.world.worldBorder.center = spawn
        spawn.world.worldBorder.size = borderSize
        spawn.world.worldBorder.damageAmount = 20.0
    }

    private fun setFinishLine(spawn: Location) {
        val gamePhase = (PluginInstance.gamePhase as GamePhase)
        val z = spawn.blockZ + gamePhase.range.toInt()
        for (i in -finishLineSize / 2 until finishLineSize / 2) {
            val x = spawn.blockX + i
            val y = spawn.world.getHighestBlockYAt(x, z)
            spawn.world.getBlockAt(x, y, z).type = Material.GOLD_BLOCK
        }
    }

    override fun disable() {
        d("Prep Phase disabled")
    }
}