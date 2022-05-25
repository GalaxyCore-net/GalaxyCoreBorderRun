package net.galaxycore.borderrun

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

    override fun onEnable() {
        instance = this
        log = logger

        val core = Bukkit.getServicesManager().load(GalaxyCoreCore::class.java)
        i("Using Core: $core")

        configNamespace = core?.databaseConfiguration?.getNamespace("borderrun")!!
        configNamespace.setDefault("lobby.needed_players", "1")
        configNamespace.setDefault("lobby.spawn", "orld@100.0;100.0;100.0;0.0;180.0")

        d("Launching BorderRun")
        registerI18NDE()
        registerI18NEN()

        d("Introducing Game Phases")
        game = getNewGame()

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