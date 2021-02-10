package com.willfp.talismans.talismans.util;

import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import com.willfp.talismans.talismans.Talisman;
import com.willfp.talismans.talismans.Talismans;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.jetbrains.annotations.NotNull;

public class TalismanCraftListener implements Listener {
    /**
     * Called on item craft.
     * @param event The event to listen for.
     */
    @EventHandler
    public void onCraft(@NotNull final PrepareItemCraftEvent event) {
        if (!(event.getRecipe() instanceof ShapedRecipe)) {
            return;
        }

        ShapedRecipe recipe = (ShapedRecipe) event.getRecipe();

        Talisman talisman = Talismans.getByKey(recipe.getKey());

        if (talisman == null) {
            return;
        }

        if (event.getViewers().isEmpty()) {
            return;
        }

        Player player = (Player) event.getViewers().get(0);

        if (!player.hasPermission("talismans.fromtable." + recipe.getKey().getKey().replace("_", ""))) {
            event.getInventory().setResult(new ItemStack(Material.AIR));
        }
    }

    /**
     * Prevents using talismans in recipes.
     * @param event The event to listen for.
     */
    @EventHandler
    public void preventUseTalismanInCraft(@NotNull final PrepareItemCraftEvent event) {
        if (!(event.getRecipe() instanceof ShapedRecipe)) {
            return;
        }

        ShapedRecipe recipe = (ShapedRecipe) event.getRecipe();

        if (AbstractEcoPlugin.LOADED_ECO_PLUGINS.contains(recipe.getKey().getNamespace())) {
            return;
        }

        for (ItemStack itemStack : event.getInventory().getMatrix()) {
            if (TalismanChecks.getTalismanOnItem(itemStack) != null) {
                event.getInventory().setResult(new ItemStack(Material.AIR));
                return;
            }
        }
    }

    /**
     * Called on item craft.
     * @param event The event to listen for.
     */
    @EventHandler
    public void onCraft(@NotNull final CraftItemEvent event) {
        if (!(event.getRecipe() instanceof ShapedRecipe)) {
            return;
        }

        ShapedRecipe recipe = (ShapedRecipe) event.getRecipe();

        Talisman talisman = Talismans.getByKey(recipe.getKey());

        if (talisman == null) {
            return;
        }

        if (event.getViewers().isEmpty()) {
            return;
        }

        Player player = (Player) event.getViewers().get(0);

        if (!player.hasPermission("talismans.fromtable." + recipe.getKey().getKey().replace("_", ""))) {
            event.getInventory().setResult(new ItemStack(Material.AIR));
            event.setCancelled(true);
        }
    }

    /**
     * Prevents using talismans in recipes.
     * @param event The event to listen for.
     */
    @EventHandler
    public void preventUseTalismanInCraft(@NotNull final CraftItemEvent event) {
        if (!(event.getRecipe() instanceof ShapedRecipe)) {
            return;
        }

        ShapedRecipe recipe = (ShapedRecipe) event.getRecipe();

        if (AbstractEcoPlugin.LOADED_ECO_PLUGINS.contains(recipe.getKey().getNamespace())) {
            return;
        }

        for (ItemStack itemStack : event.getInventory().getMatrix()) {
            if (TalismanChecks.getTalismanOnItem(itemStack) != null) {
                event.getInventory().setResult(new ItemStack(Material.AIR));
                event.setCancelled(true);
                return;
            }
        }
    }
}
