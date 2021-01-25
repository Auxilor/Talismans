package com.willfp.talismans.talismans.util;

import com.willfp.eco.util.internal.PluginDependent;
import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import com.willfp.talismans.talismans.Talisman;
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
    public DiscoverRecipeListener(@NotNull final AbstractEcoPlugin plugin) {
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
                    player.discoverRecipe(talisman.getKey());
                }
            }
        }
    }
}
