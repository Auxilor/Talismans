package com.willfp.talismans.talismans.util;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.PluginDependent;
import com.willfp.talismans.talismans.Talisman;
import com.willfp.talismans.talismans.Talismans;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.jetbrains.annotations.NotNull;

public class TalismanCraftListener extends PluginDependent implements Listener {
    /**
     * Create new talisman craft listener.
     *
     * @param plugin Instance of talismans.
     */
    public TalismanCraftListener(@NotNull final EcoPlugin plugin) {
        super(plugin);
    }

    /**
     * Called on item craft.
     *
     * @param event The event to listen for.
     */
    @EventHandler
    public void onCraft(@NotNull final PrepareItemCraftEvent event) {
        if (!(event.getRecipe() instanceof ShapedRecipe recipe)) {
            return;
        }

        NamespacedKey key = this.getPlugin().getNamespacedKeyFactory().create(recipe.getKey().getKey().replace("_displayed", ""));

        Talisman talisman = Talismans.getByKey(key);

        if (talisman == null) {
            return;
        }

        if (event.getViewers().isEmpty()) {
            return;
        }

        Player player = (Player) event.getViewers().get(0);

        String permission = "talismans.fromtable." + key.getKey().replace("_", "");

        if (!player.hasPermission(permission)) {
            event.getInventory().setResult(new ItemStack(Material.AIR));
        }
    }

    /**
     * Called on item craft.
     *
     * @param event The event to listen for.
     */
    @EventHandler
    public void onCraft(@NotNull final CraftItemEvent event) {
        if (!(event.getRecipe() instanceof ShapedRecipe recipe)) {
            return;
        }

        NamespacedKey key = this.getPlugin().getNamespacedKeyFactory().create(recipe.getKey().getKey().replace("_displayed", ""));

        Talisman talisman = Talismans.getByKey(key);

        if (talisman == null) {
            return;
        }

        if (event.getViewers().isEmpty()) {
            return;
        }

        Player player = (Player) event.getViewers().get(0);

        String permission = "talismans.fromtable." + key.getKey().replace("_", "");

        if (!player.hasPermission(permission)) {
            event.getInventory().setResult(new ItemStack(Material.AIR));
            event.setCancelled(true);
        }
    }
}
