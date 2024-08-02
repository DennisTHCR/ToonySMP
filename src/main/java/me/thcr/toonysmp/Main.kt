package me.thcr.toonysmp

import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPIBukkitConfig
import me.thcr.toonysmp.build_mode.NMSTest
import me.thcr.toonysmp.commands.CommandManager
import me.thcr.toonysmp.config.ConfigManager
import me.thcr.toonysmp.custom_recipes.RecipeManager
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
    val config_base_path = "plugins/$PLUGIN_NAME/config/"
    private lateinit var web_server: Webserver
    private lateinit var thread: Thread
    override fun onLoad() {
        CommandAPI.onLoad(CommandAPIBukkitConfig(this).silentLogs(true))
    }
    override fun onEnable() {
        instance = this
        CommandAPI.onEnable()
        val nmsTest = NMSTest()
        val api = Bukkit.getServicesManager().getRegistration(LuckPerms::class.java)?.provider!!
        val mm = MiniMessage.miniMessage()
        val fileManager = FileManager()
        val serde = Serde()
        val configManager = ConfigManager(fileManager, serde, config_base_path, logger)
        val luckPermsManager = LuckPermsManager(api, this, mm)
        val commandManager = CommandManager(mm, configManager.general_config, PLUGIN_NAME, server.scoreboardManager.mainScoreboard)
        web_server = Webserver(PLUGIN_NAME, this, configManager.general_config, fileManager)
        val eventHandlerManager = EventHandlerManager(server, this, mm, configManager.general_config, luckPermsManager.group_manager)
        val recipeManager = RecipeManager(server)
    }

    override fun onDisable() {
        CommandAPI.onDisable()
        web_server.stop()
    }
}
