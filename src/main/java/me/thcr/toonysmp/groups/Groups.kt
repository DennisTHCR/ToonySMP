package me.thcr.toonysmp.groups

import net.kyori.adventure.text.format.NamedTextColor


enum class Groups(val chat_color: NamedTextColor) {
    OWNER(NamedTextColor.RED),
    MOD(NamedTextColor.LIGHT_PURPLE),
    TWITCH(NamedTextColor.DARK_PURPLE),
    DEFAULT(NamedTextColor.GREEN),
    ELITE(NamedTextColor.AQUA),
    HERO(NamedTextColor.GREEN),
    LEGEND(NamedTextColor.DARK_AQUA),
    MVP(NamedTextColor.WHITE),
    MVPPLUS(NamedTextColor.GOLD),
    NITRO(NamedTextColor.LIGHT_PURPLE),
    RUBE(NamedTextColor.RED),
    TIKTOK(NamedTextColor.AQUA),
    VIP(NamedTextColor.GRAY),
    VIPPLUS(NamedTextColor.BLACK),
    YOUTUBE(NamedTextColor.RED),
    AMETHYST(NamedTextColor.LIGHT_PURPLE);
}