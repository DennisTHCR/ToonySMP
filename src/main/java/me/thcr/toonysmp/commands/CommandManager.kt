package me.thcr.toonysmp.commands

import me.thcr.toonysmp.config.ConfigCommand
import me.thcr.toonysmp.config.ConfigWrapper
import me.thcr.toonysmp.config.GeneralConfig
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.scoreboard.Scoreboard

class CommandManager(mm: MiniMessage, configWrapper: ConfigWrapper<GeneralConfig>, val PLUGIN_NAME: String, scoreboard: Scoreboard) {
    init {
        RuleCommand(mm)
        ConfigCommand(configWrapper, PLUGIN_NAME)
        DeleteTeamsCommand(PLUGIN_NAME, scoreboard)
    }
}