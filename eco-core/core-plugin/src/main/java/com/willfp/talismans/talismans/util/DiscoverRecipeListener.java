package com.willfp.talismans.talismans.util;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.PluginDependent;
import com.willfp.talismans.talismans.Talisman;
import com.willfp.talismans.talismans.TalismanLevel;
import com.willfp.talismans.talismans.Talismans;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

public class DiscoverRecipeListener extends PluginDependent implements Listener {
    /**
     * Register listener.
     *
     * @param plugin Talismans.
     */
    public DiscoverRecipeListener(@NotNull final EcoPlugin plugin) {
        super(plugin);
    }

    /**
     * Unlock all recipes on player join.
     *
     * @param event The event to listen for.
     */
    @EventHandler
    public void onJoin(@NotNull final PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (this.getPlugin().getConfigYml().getBool("crafting.discover")) {
            for (Talisman talisman : Talismans.values()) {
                if (talisman.isEnabled()) {
                    for (TalismanLevel level : talisman.getLevels()) {
                        player.discoverRecipe(level.getKey());
                    }
                }
            }
        }
    }
}
