package me.thcr.toonysmp.commands

import me.thcr.toonysmp.config.ConfigCommand
import me.thcr.toonysmp.config.ConfigWrapper
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.scoreboard.Scoreboard

class CommandManager(mm: MiniMessage, config_wrapper: ConfigWrapper, PLUGIN_NAME: String, scoreboard: Scoreboard) {
    init {
        RuleCommand(mm)
        ConfigCommand(config_wrapper, PLUGIN_NAME)
        DeleteTeamsCommand(PLUGIN_NAME, scoreboard)
    }
}