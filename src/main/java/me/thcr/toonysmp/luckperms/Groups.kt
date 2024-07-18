package me.thcr.toonysmp.luckperms

import net.kyori.adventure.text.format.NamedTextColor


enum class Groups(val color: NamedTextColor, val priority: String) {
    OWNER(NamedTextColor.RED, "A"),
    MOD(NamedTextColor.LIGHT_PURPLE, "B"),
    TWITCH(NamedTextColor.DARK_PURPLE, "C"),
    DEFAULT(NamedTextColor.GREEN, "M"),
    ELITE(NamedTextColor.AQUA, "G"),
    HERO(NamedTextColor.GREEN, "H"),
    LEGEND(NamedTextColor.DARK_AQUA, "F"),
    MVP(NamedTextColor.WHITE, "J"),
    MVPPLUS(NamedTextColor.GOLD, "I"),
    NITRO(NamedTextColor.LIGHT_PURPLE, "E"),
    RUBE(NamedTextColor.RED, "N"),
    TIKTOK(NamedTextColor.AQUA, "N"),
    VIP(NamedTextColor.GRAY, "L"),
    VIPPLUS(NamedTextColor.BLACK, "K"),
    YOUTUBE(NamedTextColor.RED, "D"),
    AMETHYST(NamedTextColor.LIGHT_PURPLE, "N");

    val team_name = priority + this.name
}