package com.willfp.talismans.talismans.util;

import com.willfp.talismans.talismans.Talisman;
import com.willfp.talismans.talismans.Talismans;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

public class DiscoverRecipeListener implements Listener {
    /**
     * Unlock all recipes on player join.
     *
     * @param event The event to listen for.
     */
    @EventHandler
    public void onJoin(@NotNull final PlayerJoinEvent event) {
        Player player = event.getPlayer();

        Talismans.values().stream().filter(Talisman::isEnabled).map(Talisman::getKey).forEach(player::discoverRecipe);
    }
}
