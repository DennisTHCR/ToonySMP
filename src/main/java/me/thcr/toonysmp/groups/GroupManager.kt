package me.thcr.toonysmp.groups

import me.thcr.toonysmp.Main
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.minimessage.MiniMessage
import net.luckperms.api.LuckPerms
import org.bukkit.Bukkit
import org.bukkit.Server
import org.bukkit.scoreboard.Team

@Suppress("UNUSED_VARIABLE")
class GroupManager(plugin: Main, server: Server, mm: MiniMessage) {
    private val api: LuckPerms
    init {
        val provider = Bukkit.getServicesManager().getRegistration(LuckPerms::class.java)!!
        api = provider.provider
        api.groupManager.loadedGroups.forEach { group ->
            val prefix: Component = if (group.cachedData.metaData.prefix is String) {
                mm.deserialize("<white>${group.cachedData.metaData.prefix!!}</white>")
            } else {
                mm.deserialize("")
            }
            if (server.scoreboardManager.mainScoreboard.getTeam(group.name) !is Team) server.scoreboardManager.mainScoreboard.registerNewTeam(group.name)
            server.scoreboardManager.mainScoreboard.getTeam(group.name)?.prefix(prefix)
            server.scoreboardManager.mainScoreboard.getTeam(group.name)?.color(get_color_from_team_name(group.name))
        }
        val group_listeners = GroupListeners(server, api, plugin, mm)
    }

    private fun get_color_from_team_name(name: String): NamedTextColor {
        return Groups.valueOf(name.uppercase()).chat_color
    }
}