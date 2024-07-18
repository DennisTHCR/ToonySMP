package me.thcr.toonysmp.groups

import me.thcr.toonysmp.Main
import net.kyori.adventure.text.minimessage.MiniMessage
import net.luckperms.api.LuckPerms
import net.luckperms.api.event.node.NodeMutateEvent
import net.luckperms.api.model.user.User
import org.bukkit.OfflinePlayer
import org.bukkit.Server
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.scoreboard.Team

class GroupListeners(server: Server, private val api: LuckPerms, private val plugin: Main, mm: MiniMessage) : org.bukkit.event.Listener {
    init {
        server.pluginManager.registerEvents(this, plugin)
        val event_bus = api.eventBus
        event_bus.subscribe(plugin, NodeMutateEvent::class.java) { event ->
            if (event.isUser) {
                val target: User = (event.target) as User
                val player = plugin.server.getPlayer(target.uniqueId)
                if (plugin.server.scoreboardManager.mainScoreboard.getPlayerTeam(player as OfflinePlayer) is Team) plugin.server.scoreboardManager.mainScoreboard.getPlayerTeam(player as OfflinePlayer)!!.removePlayer(player as OfflinePlayer)
                val team = plugin.server.scoreboardManager.mainScoreboard.getTeam(target.primaryGroup)!!
                team.addPlayer(player as OfflinePlayer)
                player.sendMessage(mm.deserialize("<gold>Your Rank has been updated to</gold>${Groups.valueOf(team.name.uppercase()).chat_color}${target.primaryGroup}!"))
            }
        }
    }
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        if (!event.player.hasPlayedBefore() || plugin.server.scoreboardManager.mainScoreboard.getPlayerTeam(event.player as OfflinePlayer) !is Team) {
            plugin.server.scoreboardManager.mainScoreboard.getTeam(api.userManager.getUser(event.player.uniqueId)?.cachedData?.metaData?.primaryGroup!!)?.addPlayer(event.player)
        }
    }
}