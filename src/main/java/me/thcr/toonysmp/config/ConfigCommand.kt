package me.thcr.toonysmp.config

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.BooleanArgument
import dev.jorel.commandapi.arguments.CustomArgument
import dev.jorel.commandapi.arguments.GreedyStringArgument
import dev.jorel.commandapi.executors.CommandExecutor

class ConfigCommand(private val config_wrapper: ConfigWrapper<GeneralConfig>, private val PLUGIN_NAME: String) {
    init {
        val command = CommandAPICommand("config")
        val loadCommand = CommandAPICommand("load").executes(CommandExecutor { _, _ -> config_wrapper.read_from_file() })
        command.withSubcommand(loadCommand)
        add_subcommands(command)
        command.withPermission("$PLUGIN_NAME:config").register()
    }

    private fun add_subcommands(command: CommandAPICommand) {
        for (entry in GeneralConfig.entries) {
            val executor = CommandExecutor { sender, args ->
                val value = args.get("value")!!
                sender.sendMessage("Successfully set ${entry.name} to $value")
                config_wrapper.set(entry, value)
            }
            val type: Class<*> = config_wrapper.get_type(entry)
            val subcommand = CommandAPICommand(entry.name)
            when (type) {
                List::class.java -> {
                    val argument = CustomArgument(GreedyStringArgument("value")) { info ->
                        info.input.split(' ')
                    }
                    subcommand.withArguments(argument).executes(executor)
                }
                Boolean::class.java -> {
                    subcommand.withArguments(BooleanArgument("value")).executes(executor)
                }
                else -> {
                    println("Unhandled Type: ${entry.javaClass} Value: ${config_wrapper.get<Any>(entry)}")
                    subcommand.executes(CommandExecutor { sender, _ ->
                        sender.sendMessage("Something went wrong :(")
                    })
                }
            }
            command.withSubcommand(subcommand)
        }
    }
}