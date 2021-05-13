package com.willfp.talismans.talismans.util.equipevent;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.PluginDependent;
import com.willfp.talismans.talismans.Talisman;
import com.willfp.talismans.talismans.TalismanLevel;
import com.willfp.talismans.talismans.Talismans;
import com.willfp.talismans.talismans.util.TalismanChecks;
import com.willfp.talismans.talismans.util.TalismanUtils;
import org.bukkit.Bukkit;
import org.bukkit.Tag;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class TalismanEquipEventListeners extends PluginDependent implements Listener {
    /**
     * Initialize new listeners and link them to a plugin.
     *
     * @param plugin The plugin to link to.
     */
    public TalismanEquipEventListeners(@NotNull final EcoPlugin plugin) {
        super(plugin);
    }

    /**
     * Called on item pickup.
     *
     * @param event The event to listen for.
     */
    @EventHandler
    public void onItemPickup(@NotNull final EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        if (!Tag.SHULKER_BOXES.isTagged(event.getItem().getItemStack().getType()) && !TalismanUtils.isTalismanMaterial(event.getItem().getItemStack().getType())) {
            return;
        }

        refreshPlayer((Player) event.getEntity());
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

        for (Talisman talisman : Talismans.values()) {
            for (TalismanLevel level : talisman.getLevels()) {
                Bukkit.getPluginManager().callEvent(new TalismanEquipEvent(player, level, EquipType.UNEQUIP));
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
        if (!Tag.SHULKER_BOXES.isTagged(event.getItemDrop().getItemStack().getType()) && !TalismanUtils.isTalismanMaterial(event.getItemDrop().getItemStack().getType())) {
            return;
        }

        refreshPlayer(event.getPlayer(), event.getItemDrop().getItemStack());
    }

    /**
     * Called on place shulker box.
     *
     * @param event The event to listen for.
     */
    @EventHandler
    public void onShulkerPlace(@NotNull final BlockPlaceEvent event) {
        ItemStack itemStack = event.getItemInHand();
        if (!Tag.SHULKER_BOXES.isTagged(itemStack.getType())) {
            return;
        }
        //refreshPlayer(event.getPlayer(), itemStack);
    }

    @EventHandler
    public void onSwitchHands(@NotNull final PlayerSwapHandItemsEvent event) {
        refreshPlayer(event.getPlayer(), event.getPlayer().getInventory().getItemInOffHand());
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

    private void refreshPlayer(@NotNull final Player player,
                               @NotNull final ItemStack... extra) {
        Set<TalismanLevel> inCache = TalismanChecks.getTalismansOnPlayer(player, false, extra);

        this.getPlugin().getScheduler().runLater(() -> {
            Set<TalismanLevel> newSet = TalismanChecks.getTalismansOnPlayer(player, false);

            for (TalismanLevel talisman : new HashSet<>(newSet)) {
                if (inCache.contains(talisman)) {
                    newSet.remove(talisman);
                    inCache.remove(talisman);
                }
            }

            newSet.removeAll(inCache);

            for (TalismanLevel talisman : newSet) {
                if (talisman.getTalisman().isEnabled()) {
                    Bukkit.getPluginManager().callEvent(new TalismanEquipEvent(player, talisman, EquipType.EQUIP));
                }
            }

            inCache.removeAll(newSet);
            for (TalismanLevel talisman : inCache) {
                Bukkit.getPluginManager().callEvent(new TalismanEquipEvent(player, talisman, EquipType.UNEQUIP));
            }
        }, 1);
    }
}
