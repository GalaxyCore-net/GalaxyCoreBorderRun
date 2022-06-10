package net.galaxycore.borderrun

import net.galaxycore.borderrun.cache.OnlinePlayerCache
import net.galaxycore.borderrun.commands.StartCommand
import net.galaxycore.borderrun.game.GamePhaseSystem
import net.galaxycore.borderrun.game.ListenerPool
import net.galaxycore.borderrun.game.Phase
import net.galaxycore.borderrun.game.getNewGame
import net.galaxycore.borderrun.phases.EndPhase
import net.galaxycore.borderrun.phases.GamePhase
import net.galaxycore.borderrun.phases.LobbyPhase
import net.galaxycore.borderrun.phases.PreparePhase
import net.galaxycore.borderrun.runnables.KRunnableHolder
import net.galaxycore.borderrun.runnables.firstSync
import net.galaxycore.borderrun.utils.d
import net.galaxycore.borderrun.utils.forCommandToExecutor
import net.galaxycore.borderrun.utils.i
import net.galaxycore.galaxycorecore.GalaxyCoreCore
import net.galaxycore.galaxycorecore.configuration.ConfigNamespace
import org.bukkit.Bukkit
import org.bukkit.plugin.PluginManager
import org.bukkit.plugin.java.JavaPlugin
import java.util.logging.Logger
import kotlin.properties.Delegates

class BorderRun : JavaPlugin() {

    private val kRunnableHolderProperty = lazy { KRunnableHolder }
    internal lateinit var lobbyPhase: Phase
    internal lateinit var prepPhase: Phase
    internal lateinit var gamePhase: Phase
    internal lateinit var endPhase: Phase
    internal lateinit var game: GamePhaseSystem
    internal lateinit var listenerPool: ListenerPool
    internal val kRunnableHolder by kRunnableHolderProperty
    internal lateinit var configNamespace: ConfigNamespace
    internal var gamePhaseSize by Delegates.notNull<Int>()

    override fun onEnable() {
        instance = this
        log = logger

        val core = Bukkit.getServicesManager().load(GalaxyCoreCore::class.java)
        i("Using Core: $core")

        configNamespace = core?.databaseConfiguration?.getNamespace("borderrun")!!
        configNamespace.setDefault("lobby.needed_players", "1")
        configNamespace.setDefault("lobby.spawn", "world@100.0;100.0;100.0;0.0;180.0")
        configNamespace.setDefault("game.range", "7500")
        configNamespace.setDefault("game.world_name", "world")
        configNamespace.setDefault("game.border_size", "25")
        configNamespace.setDefault("game.show_arrow", "true")
        configNamespace.setDefault("game.arrow.clear_above", "10")
        configNamespace.setDefault("game.arrow.material", "GOLD_BLOCK")
        configNamespace.setDefault("game.border.size", "100000")
        configNamespace.setDefault("game.border.damage", "20")
        configNamespace.setDefault("game.border.initial_speed", "10")
        configNamespace.setDefault("game.border.max_border_speed", "70")
        configNamespace.setDefault("game.finish_line_size", "500")

        d("Launching BorderRun")
        registerI18NDE()
        registerI18NEN()

        d("Starting Caching System")
        PluginManagerInst.registerEvents(OnlinePlayerCache.instance, this)

        d("Introducing Game Phases")
        game = getNewGame()
        gamePhaseSize = game.baseGamePhases.size

        d("Initializing Phases")
        listenerPool = ListenerPool()
        lobbyPhase = LobbyPhase()
        prepPhase = PreparePhase()
        gamePhase = GamePhase()
        endPhase = EndPhase()

        firstSync {
            d("Running delayed task")
            lobbyPhase.onEnable()
        }.execute()

        d("Loading Command Map")
        forCommandToExecutor("start", StartCommand())

        i("BorderRun was loaded")
    }

    companion object {
        lateinit var log: Logger
        lateinit var instance: BorderRun
    }
}

val PluginInstance: BorderRun
    get() = BorderRun.instance

val PluginManagerInst: PluginManager
    get() = Bukkit.getPluginManager()