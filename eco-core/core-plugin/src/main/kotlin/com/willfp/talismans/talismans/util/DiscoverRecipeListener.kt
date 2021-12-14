package com.willfp.talismans.talismans.util

import com.willfp.eco.core.EcoPlugin
import com.willfp.talismans.talismans.Talismans.values
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class DiscoverRecipeListener(private val plugin: EcoPlugin) : Listener {
    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val player = event.player
        if (plugin.configYml.getBool("crafting.discover")) {
            for (talisman in values()) {
                player.discoverRecipe(talisman.recipe?.key ?: continue)
            }
        }
    }
}