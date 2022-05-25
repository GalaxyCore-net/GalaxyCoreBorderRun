package net.galaxycore.borderrun.commands

import net.galaxycore.borderrun.game.game
import net.galaxycore.borderrun.utils.sI18N
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class StartCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {

        if (!sender.hasPermission("ctb.start")) {
            "nopermission".sI18N(sender as Player)
            return true
        }

        if (game.isRunning) {
            "command.start.failure".sI18N(sender as Player)
            return true
        }

        game.begin()
        "command.start.success".sI18N(sender as Player)

        return true
    }
}