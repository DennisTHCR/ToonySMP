package me.thcr.toonysmp

import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPIBukkitConfig
import me.thcr.toonysmp.commands.CommandManager
import me.thcr.toonysmp.config.ConfigManager
import me.thcr.toonysmp.event_handlers.EventHandlerManager
import me.thcr.toonysmp.files.FileManager
import me.thcr.toonysmp.groups.GroupManager
import me.thcr.toonysmp.serde.Serde
import me.thcr.toonysmp.webserver.Webserver
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.plugin.java.JavaPlugin

@Suppress("UNUSED_VARIABLE", "unused", "PrivatePropertyName")
class Main : JavaPlugin() {
    private val PLUGIN_NAME = name
    private lateinit var web_server: Webserver
    private lateinit var thread: Thread
    override fun onLoad() {
        CommandAPI.onLoad(CommandAPIBukkitConfig(this).silentLogs(true))
    }
    override fun onEnable() {
        CommandAPI.onEnable()
        val mm = MiniMessage.miniMessage()
        val file_manager = FileManager()
        val serde = Serde()
        val config_manager = ConfigManager(file_manager, serde, "plugins/$PLUGIN_NAME/config.json", logger)
        val group_manager = GroupManager(this, server, mm)
        val command_manager = CommandManager(mm, config_manager.config_wrapper, PLUGIN_NAME)
        web_server = Webserver(PLUGIN_NAME, this, config_manager.config_wrapper, file_manager)
        val event_handler_manager = EventHandlerManager(server, this, mm, config_manager.config_wrapper)
    }

    override fun onDisable() {
        CommandAPI.onDisable()
        web_server.stop()
    }
}
