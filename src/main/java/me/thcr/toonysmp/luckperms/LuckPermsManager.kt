package me.thcr.toonysmp.luckperms

import me.thcr.toonysmp.Main
import net.kyori.adventure.text.minimessage.MiniMessage
import net.luckperms.api.LuckPerms

class LuckPermsManager(api: LuckPerms, plugin: Main, mm: MiniMessage) {
    val scoreboard = plugin.server.scoreboardManager.mainScoreboard
    val group_manager = GroupManager(scoreboard, mm, api)
    val group_change_listener = GroupChangeListener(api, plugin, mm)
}