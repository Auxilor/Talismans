package com.willfp.talismans.talismans.util;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.PluginDependent;
import com.willfp.eco.core.events.ArmorChangeEvent;
import com.willfp.libreforge.LibReforgeUtils;
import com.willfp.libreforge.effects.ConfiguredEffect;
import com.willfp.talismans.talismans.Talisman;
import com.willfp.talismans.talismans.TalismanLevel;
import com.willfp.talismans.talismans.Talismans;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

public class TalismanEnableListeners extends PluginDependent<EcoPlugin> implements Listener {
    /**
     * Initialize new listeners and link them to a plugin.
     *
     * @param plugin The plugin to link to.
     */
    public TalismanEnableListeners(@NotNull final EcoPlugin plugin) {
        super(plugin);
    }

    /**
     * Called on item pickup.
     *
     * @param event The event to listen for.
     */
    @EventHandler
    public void onItemPickup(@NotNull final EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        if (!TalismanUtils.isTalismanMaterial(event.getItem().getItemStack().getType())) {
            return;
        }

        refreshPlayer(player);
    }

    /**
     * Called on player join.
     *
     * @param event The event to listen for.
     */
    @EventHandler
    public void onPlayerJoin(@NotNull final PlayerJoinEvent event) {
        refresh();
    }

    /**
     * Called on player leave.
     *
     * @param event The event to listen for.
     */
    @EventHandler
    public void onPlayerLeave(@NotNull final PlayerQuitEvent event) {
        refresh();

        Player player = event.getPlayer();

        for (Talisman value : Talismans.values()) {
            for (TalismanLevel level : value.getLevels()) {
                for (ConfiguredEffect effect : level.getEffects()) {
                    effect.getEffect().disableForPlayer(player);
                }
            }
        }
    }

    /**
     * Called on item drop.
     *
     * @param event The event to listen for.
     */
    @EventHandler
    public void onInventoryDrop(@NotNull final PlayerDropItemEvent event) {
        if (!TalismanUtils.isTalismanMaterial(event.getItemDrop().getItemStack().getType())) {
            return;
        }

        refreshPlayer(event.getPlayer());
    }

    /**
     * Called on slot change.
     *
     * @param event The event to listen for.
     */
    @EventHandler
    public void onChangeSlot(@NotNull final PlayerItemHeldEvent event) {
        refreshPlayer(event.getPlayer());

        this.getPlugin().getScheduler().run(() -> refreshPlayer(event.getPlayer()));
    }

    /**
     * Called on armor change.
     *
     * @param event The event to listen for.
     */
    @EventHandler
    public void onArmorChange(@NotNull final ArmorChangeEvent event) {
        refreshPlayer(event.getPlayer());
    }

    /**
     * Called on inventory click.
     *
     * @param event The event to listen for.
     */
    @EventHandler
    public void onInventoryClick(@NotNull final InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        refreshPlayer((Player) event.getWhoClicked());
    }

    /**
     * Force refresh all online players.
     * <p>
     * This is a very expensive method.
     */
    public void refresh() {
        this.getPlugin().getServer().getOnlinePlayers().forEach(this::refreshPlayer);
    }

    private void refreshPlayer(@NotNull final Player player) {
        TalismanChecks.clearCache(player);
        LibReforgeUtils.updateEffects(player);
    }
}
