package com.willfp.talismans.data.inventory;

import com.willfp.talismans.data.PlayerData;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class TalismanInventoryUtils {
    /**
     * Show talisman inventory to player.
     *
     * @param player The player to show the inventory to.
     * @return The created inventory.
     */
    public Inventory showInventory(@NotNull final Player player) {
        Inventory inventory = Bukkit.createInventory(null, 54, "Talisman Inventory");
        PlayerData.get(player).getInventory().forEach(inventory::setItem);
        player.openInventory(inventory);

        return inventory;
    }
}
