package me.thcr.toonysmp.event_handlers

import me.thcr.toonysmp.luckperms.GroupManager
import me.thcr.toonysmp.luckperms.Groups
import org.bukkit.OfflinePlayer
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.scoreboard.Scoreboard
import org.bukkit.scoreboard.Team

class TeamOnJoinHandler(private val scoreboard: Scoreboard, private val group_manager: GroupManager) : org.bukkit.event.Listener {
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        if (!event.player.hasPlayedBefore() || scoreboard.getPlayerTeam(event.player as OfflinePlayer) !is Team) {
            val group = Groups.valueOf(group_manager.get_group(event.player).name.uppercase())
            scoreboard.getTeam(group.team_name)?.addPlayer(event.player)
        }
    }
}