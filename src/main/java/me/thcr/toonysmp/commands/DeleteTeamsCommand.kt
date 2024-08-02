package me.thcr.toonysmp.commands

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.executors.CommandExecutor
import org.bukkit.scoreboard.Scoreboard

class DeleteTeamsCommand(private val PLUGIN_NAME: String, scoreboard: Scoreboard) {
    init {
        CommandAPICommand("delete_teams").withPermission("$PLUGIN_NAME:delete_teams").executes(CommandExecutor { _, _ ->
            scoreboard.teams.forEach {
                it.unregister()
            }
        }).register()
    }
}