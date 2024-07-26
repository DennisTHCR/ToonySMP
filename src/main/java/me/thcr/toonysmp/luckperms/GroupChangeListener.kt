package me.thcr.toonysmp.luckperms

import me.thcr.toonysmp.Main
import net.kyori.adventure.text.minimessage.MiniMessage
import net.luckperms.api.LuckPerms
import net.luckperms.api.event.node.NodeAddEvent
import net.luckperms.api.model.user.User
import org.bukkit.OfflinePlayer
import org.bukkit.scoreboard.Team

class GroupChangeListener(api: LuckPerms, plugin: Main, mm: MiniMessage) {
    init {
        val event_bus = api.eventBus
        val server = plugin.server
        val scoreboard = server.scoreboardManager.mainScoreboard
        event_bus.subscribe(plugin, NodeAddEvent::class.java) { event ->
            if (event.isUser) {
                val target: User = (event.target) as User
                val player = server.getPlayer(target.uniqueId)
                if (scoreboard.getPlayerTeam(player as OfflinePlayer) is Team) scoreboard.getPlayerTeam(player as OfflinePlayer)!!.removePlayer(player as OfflinePlayer)
                val team_name = Groups.valueOf(target.primaryGroup.uppercase()).team_name
                val team = scoreboard.getTeam(team_name)!!
                team.addPlayer(player as OfflinePlayer)
                player.sendMessage(mm.deserialize("<gold>Your Rank has been updated to </gold>").append(mm.deserialize("${target.primaryGroup}!").color(Groups.valueOf(target.primaryGroup.uppercase()).color)))
            }
        }
    }
}