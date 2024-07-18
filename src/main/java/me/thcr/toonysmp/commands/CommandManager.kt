package me.thcr.toonysmp.commands

import me.thcr.toonysmp.config.ConfigCommand
import me.thcr.toonysmp.config.ConfigWrapper
import net.kyori.adventure.text.minimessage.MiniMessage

class CommandManager(mm: MiniMessage, config_wrapper: ConfigWrapper, PLUGIN_NAME: String) {
    init {
        RuleCommand(mm)
        ConfigCommand(config_wrapper, PLUGIN_NAME)
    }
}