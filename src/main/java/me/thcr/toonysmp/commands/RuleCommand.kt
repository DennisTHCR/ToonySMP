package me.thcr.toonysmp.commands

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.executors.CommandExecutor
import net.kyori.adventure.text.minimessage.MiniMessage

class RuleCommand(private val mm: MiniMessage) {
    init {
        CommandAPICommand("rules").executes(CommandExecutor { sender, _ ->
            sender.sendMessage(mm.deserialize("You can find the Rules <blue><click:open_url:\"https://discord.com/channels/888074247845605376/1262371237062119495\">Here</click></blue>!"))
        }).register()
    }
}