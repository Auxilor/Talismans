package com.willfp.talismans.talismans.util.equipevent;

import com.willfp.eco.util.internal.PluginDependent;
import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import com.willfp.talismans.talismans.Talisman;
import com.willfp.talismans.talismans.util.TalismanChecks;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class TalismanEquipEventListeners extends PluginDependent implements Listener {
    public TalismanEquipEventListeners(@NotNull final AbstractEcoPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onItemPickup(@NotNull final EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        if (event.getItem().getItemStack().getType() != Material.PLAYER_HEAD) {
            return;
        }

        refreshPlayer((Player) event.getEntity());
    }

    @EventHandler
    public void onPlayerJoin(@NotNull final PlayerJoinEvent event) {
        refresh();
    }

    @EventHandler
    public void onPlayerLeave(@NotNull final PlayerQuitEvent event) {
        refresh();
    }

    @EventHandler
    public void onInventoryDrop(@NotNull final EntityDropItemEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        if (event.getItemDrop().getItemStack().getType() != Material.PLAYER_HEAD) {
            return;
        }

        refreshPlayer((Player) event.getEntity());
    }

    @EventHandler
    public void onInventoryClick(@NotNull final InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        refreshPlayer((Player) event.getWhoClicked());
    }

    private void refresh() {
        this.getPlugin().getScheduler().runLater(() -> this.getPlugin().getServer().getOnlinePlayers().forEach(this::refreshPlayer), 1);
    }

    private void refreshPlayer(@NotNull final Player player) {
        Set<Talisman> inCache = TalismanChecks.CACHED_TALISMANS.get(player.getUniqueId());
        if (inCache == null) {
            inCache = new HashSet<>();
        }

        Set<Talisman> newSet = TalismanChecks.getTalismansOnPlayer(player);

        newSet.removeAll(inCache);
        for (Talisman talisman : newSet) {
            Bukkit.getPluginManager().callEvent(new TalismanEquipEvent(player, talisman, EquipType.EQUIP));
        }

        inCache.removeAll(newSet);
        for (Talisman talisman : inCache) {
            Bukkit.getPluginManager().callEvent(new TalismanEquipEvent(player, talisman, EquipType.UNEQUIP));
        }
    }
}
