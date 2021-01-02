package com.willfp.talismans.data.inventory;

import com.willfp.talismans.data.PlayerData;
import com.willfp.talismans.talismans.util.TalismanChecks;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TalismanInventoryListeners implements Listener {
    /**
     * On inventory close.
     *
     * @param event The event to listen for.
     */
    @EventHandler
    public void onTalismanInventoryClose(@NotNull final InventoryCloseEvent event) {
        if (!event.getView().getTitle().toLowerCase().contains("talisman")) {
            return;
        }
        Map<Integer, ItemStack> contents = new HashMap<>();
        for (int i = 0; i < event.getInventory().getSize(); i++) {
            contents.put(i, event.getInventory().getItem(i));
        }

        PlayerData.get((Player) event.getPlayer()).setInventory(contents);
    }

    /**
     * On add item to talisman inventory.
     *
     * @param event The event to listen for.
     */
    @EventHandler
    public void onPutItemInTalismanInventory(@NotNull final InventoryClickEvent event) {
        if (!event.getView().getTitle().toLowerCase().contains("talisman")) {
            return;
        }


        if (event.getClick() == ClickType.SHIFT_LEFT || event.getClick() == ClickType.SHIFT_RIGHT) {
            ItemStack item = event.getCurrentItem();
            if (TalismanChecks.getTalismanOnItem(item) == null) {
                event.setCancelled(true);
            }
        }

        if (Objects.equals(event.getClickedInventory(), event.getView().getTopInventory())) {
            ItemStack item = event.getCursor();
            if (TalismanChecks.getTalismanOnItem(item) == null) {
                event.setCancelled(true);
            }
        }
    }
}
