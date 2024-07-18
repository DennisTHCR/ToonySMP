package me.thcr.toonysmp

import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPIBukkitConfig
import me.thcr.toonysmp.commands.CommandManager
import me.thcr.toonysmp.config.ConfigManager
import me.thcr.toonysmp.event_handlers.EventHandlerManager
import me.thcr.toonysmp.files.FileManager
import me.thcr.toonysmp.luckperms.LuckPermsManager
import me.thcr.toonysmp.serde.Serde
import me.thcr.toonysmp.webserver.Webserver
import net.kyori.adventure.text.minimessage.MiniMessage
import net.luckperms.api.LuckPerms
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

@Suppress("UNUSED_VARIABLE", "unused", "PrivatePropertyName")
class Main : JavaPlugin() {

    companion object {
        lateinit var instance: Main
        private set
    }
    val PLUGIN_NAME = name
    private lateinit var web_server: Webserver
    private lateinit var thread: Thread
    override fun onLoad() {
        CommandAPI.onLoad(CommandAPIBukkitConfig(this).silentLogs(true))
    }
    override fun onEnable() {
        instance = this
        CommandAPI.onEnable()
        val api = Bukkit.getServicesManager().getRegistration(LuckPerms::class.java)?.provider!!
        val mm = MiniMessage.miniMessage()
        val file_manager = FileManager()
        val serde = Serde()
        val config_manager = ConfigManager(file_manager, serde, "plugins/$PLUGIN_NAME/config.json", logger)
        val luck_perms_manager = LuckPermsManager(api, this, mm)
        val command_manager = CommandManager(mm, config_manager.config_wrapper, PLUGIN_NAME, server.scoreboardManager.mainScoreboard)
        web_server = Webserver(PLUGIN_NAME, this, config_manager.config_wrapper, file_manager)
        val event_handler_manager = EventHandlerManager(server, this, mm, config_manager.config_wrapper, luck_perms_manager.group_manager)
    }

    override fun onDisable() {
        CommandAPI.onDisable()
        web_server.stop()
    }
}
