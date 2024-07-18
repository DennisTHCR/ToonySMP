package me.thcr.toonysmp.event_handlers

import io.papermc.paper.chat.ChatRenderer
import io.papermc.paper.event.player.AsyncChatEvent
import me.thcr.toonysmp.luckperms.GroupManager
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class ChatPrefixHandler(private val mm: MiniMessage, private val group_manager: GroupManager) : Listener {
    @EventHandler
    fun onChat(event: AsyncChatEvent) {
        event.renderer(PrefixRenderer())
    }

    inner class PrefixRenderer : ChatRenderer {
        override fun render(
            source: Player,
            sourceDisplayName: Component,
            message: Component,
            viewer: Audience
        ): Component {
            val group = group_manager.get_group(source)
            val prefix = group.cachedData.metaData.prefix
            val color = group_manager.get_color_from_team_name(group.name)
            return mm.deserialize("$prefix")
                .append(sourceDisplayName.color(color))
                .append(mm.deserialize(": "))
                .append(message)
        }

    }
}