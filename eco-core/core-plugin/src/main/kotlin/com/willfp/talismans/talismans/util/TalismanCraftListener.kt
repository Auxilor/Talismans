package com.willfp.talismans.talismans.util

import com.willfp.talismans.talismans.Talismans.getByID
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.CraftItemEvent
import org.bukkit.event.inventory.PrepareItemCraftEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapedRecipe

class TalismanCraftListener : Listener {
    /**
     * Called on item craft.
     *
     * @param event The event to listen for.
     */
    @EventHandler
    fun onCraft(event: PrepareItemCraftEvent) {
        val recipe = event.recipe as? ShapedRecipe ?: return

        val key = recipe.key.key.replace("_displayed", "")
        val talisman = getByID(key) ?: return

        if (event.viewers.isEmpty()) {
            return
        }

        val player = event.viewers[0] as Player
        val permission = "talismans.fromtable.$key"
        if (!player.hasPermission(permission)) {
            event.inventory.result = ItemStack(Material.AIR)
        }
    }

    /**
     * Called on item craft.
     *
     * @param event The event to listen for.
     */
    @EventHandler
    fun onCraft(event: CraftItemEvent) {
        val recipe = event.recipe as? ShapedRecipe ?: return

        val key = recipe.key.key.replace("_displayed", "")
        val talisman = getByID(key) ?: return

        if (event.viewers.isEmpty()) {
            return
        }

        val player = event.viewers[0] as Player
        val permission = "talismans.fromtable.$key"
        if (!player.hasPermission(permission)) {
            event.inventory.result = ItemStack(Material.AIR)
            event.isCancelled = true
        }
    }
}