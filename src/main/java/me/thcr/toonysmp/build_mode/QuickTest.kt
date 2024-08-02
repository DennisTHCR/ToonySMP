package me.thcr.toonysmp.build_mode

import org.bukkit.entity.BlockDisplay
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.util.Transformation
import org.joml.AxisAngle4f
import org.joml.Vector3f

class QuickTest : Listener {
    @EventHandler
    fun onBlockPlace(event: BlockPlaceEvent) {
        val block = event.blockPlaced
        val blockData = block.blockData
        val world = block.world
        val location = block.location
        val blockDisplay = world.spawn(location, BlockDisplay::class.java)
        blockDisplay.transformation = Transformation(Vector3f(0.2F), AxisAngle4f(0F, 0F, 0F, 1F), Vector3f(0.6F), AxisAngle4f(0F, 0F, 0F, 1F))
        blockDisplay.block = blockData
        event.isCancelled = true
    }
}