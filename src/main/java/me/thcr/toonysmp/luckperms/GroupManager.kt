package me.thcr.toonysmp.luckperms

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.minimessage.MiniMessage
import net.luckperms.api.LuckPerms
import net.luckperms.api.model.group.Group
import org.bukkit.entity.Player
import org.bukkit.scoreboard.Scoreboard
import org.bukkit.scoreboard.Team

class GroupManager(private val scoreboard: Scoreboard, private val mm: MiniMessage, private val api: LuckPerms) {
    init {
        api.groupManager.loadedGroups.forEach { group ->
            val prefix = get_group_prefix(group)
            val groupEnum = Groups.valueOf(group.name.uppercase())
            val name = groupEnum.team_name
            if (scoreboard.getTeam(name) !is Team) scoreboard.registerNewTeam(name)
            scoreboard.getTeam(name)?.prefix(prefix)
            scoreboard.getTeam(name)?.color(groupEnum.color)
        }
    }

    fun get_color_from_team_name(name: String): NamedTextColor {
        return Groups.valueOf(name.uppercase()).color
    }

    fun get_group_prefix(group: Group): Component {
        return if (group.cachedData.metaData.prefix is String) {
            mm.deserialize("<white>${group.cachedData.metaData.prefix!!}</white>")
        } else {
            mm.deserialize("")
        }
    }

    fun get_team(group: Group): Team {
        if (scoreboard.getTeam(group.name) !is Team) scoreboard.registerNewTeam(group.name)
        return scoreboard.getTeam(group.name)!!
    }

    fun get_group(name: String): Group {
        return api.groupManager.getGroup(name)!!
    }

    fun get_group(player: Player): Group {
        return get_group(api.userManager.getUser(player.uniqueId)?.primaryGroup!!)
    }
}