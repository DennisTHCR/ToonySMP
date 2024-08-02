package me.thcr.toonysmp.build_mode

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.executors.CommandExecutor
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket
import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.animal.Cow
import org.bukkit.craftbukkit.CraftWorld
import org.bukkit.craftbukkit.entity.CraftPlayer
import org.bukkit.entity.Player

class NMSTest {
    val command_api_command = CommandAPICommand("nms_test").executes(CommandExecutor { sender, _ ->
        val connection = ((sender as Player) as CraftPlayer).handle.connection
        val entity = Cow(EntityType.COW, (sender.world as CraftWorld).handle)
        // val entity = BlockDisplay(EntityType.BLOCK_DISPLAY, (sender.world as CraftWorld).handle)
        entity.setPos(sender.x, sender.y, sender.z)
        val glowEffect = MobEffectInstance(MobEffects.GLOWING, -1, 0, false, false)
        entity.addEffect(glowEffect)
        /* entity.setTransformation(Transformation(Matrix4f(0.9F, 0.0F, 0.0F, 0.0F, 0.0F,
            0.9F, 0.0F, 0.0F, 0.0F, 0.0F, 0.9F, 0.0F, 0.05F, 0.05F, 0.05F, 1.0F)))
        */
        val spawnPacket = ClientboundAddEntityPacket(entity)
        val effectPacket = ClientboundUpdateMobEffectPacket(entity.id, glowEffect, true)
        connection.send(spawnPacket)
        connection.send(effectPacket)
    }).register()
}