package com.willfp.talismans.talismans.util;

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

import java.util.Arrays;

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
     * Called on item craft.
     * @param event The event to listen for.
     */
    @EventHandler
    public void prepareCraftTalismanListener(@NotNull final PrepareItemCraftEvent event) {
        if (!(event.getRecipe() instanceof ShapedRecipe)) {
            return;
        }

        ShapedRecipe recipe = (ShapedRecipe) event.getRecipe();

        if (!recipe.getKey().getNamespace().equals("talismans")) {
            return;
        }

        if (event.getViewers().isEmpty()) {
            return;
        }

        Talisman[] overlay = new Talisman[9];

        for (int i = 0; i < 9; i++) {
            ItemStack itemStack = event.getInventory().getMatrix()[i];


            if (itemStack == null) {
                continue;
            }

            if (itemStack.getType() != Material.PLAYER_HEAD) {
                continue;
            }

            Talisman matchedTalisman = TalismanChecks.getTalismanOnItem(itemStack);

            if (matchedTalisman == null) {
                continue;
            }

            overlay[i] = matchedTalisman;
        }

        boolean empty = true;
        for (Talisman overlayTalisman : overlay) {
            if (overlayTalisman != null) {
                empty = false;
                break;
            }
        }

        boolean needsHeads = false;
        for (ItemStack itemStack : recipe.getIngredientMap().values()) {
            if (itemStack.getType() == Material.PLAYER_HEAD) {
                needsHeads = true;
                break;
            }
        }

        if (empty && needsHeads) {
            event.getInventory().setResult(new ItemStack(Material.AIR));
            return;
        }

        Talisman talisman = Talismans.values().stream().filter(talisman1 -> Arrays.equals(overlay, talisman1.getRecipeTalismanOverlay())).findFirst().orElse(null);

        if (talisman == null) {
            event.getInventory().setResult(new ItemStack(Material.AIR));
        } else {
            event.getInventory().setResult(talisman.getItemStack());
        }
    }

    /**
     * Called on item craft.
     * @param event The event to listen for.
     */
    @EventHandler
    public void prepareCraftTalismanListener(@NotNull final CraftItemEvent event) {
        if (!(event.getRecipe() instanceof ShapedRecipe)) {
            return;
        }

        ShapedRecipe recipe = (ShapedRecipe) event.getRecipe();

        if (!recipe.getKey().getNamespace().equals("talismans")) {
            return;
        }

        if (event.getViewers().isEmpty()) {
            return;
        }

        Talisman[] overlay = new Talisman[9];

        for (int i = 0; i < 9; i++) {
            ItemStack itemStack = event.getInventory().getMatrix()[i];

            if (itemStack == null) {
                continue;
            }

            if (itemStack.getType() != Material.PLAYER_HEAD) {
                continue;
            }

            Talisman matchedTalisman = TalismanChecks.getTalismanOnItem(itemStack);

            if (matchedTalisman == null) {
                continue;
            }

            overlay[i] = matchedTalisman;
        }

        boolean empty = true;
        for (Talisman overlayTalisman : overlay) {
            if (overlayTalisman != null) {
                empty = false;
                break;
            }
        }

        boolean needsHeads = false;
        for (ItemStack itemStack : recipe.getIngredientMap().values()) {
            if (itemStack.getType() == Material.PLAYER_HEAD) {
                needsHeads = true;
                break;
            }
        }

        if (empty && needsHeads) {
            event.getInventory().setResult(new ItemStack(Material.AIR));
            return;
        }

        Talisman talisman = Talismans.values().stream().filter(talisman1 -> Arrays.equals(overlay, talisman1.getRecipeTalismanOverlay())).findFirst().orElse(null);

        if (talisman == null) {
            event.getInventory().setResult(new ItemStack(Material.AIR));
            event.setCancelled(true);
        } else {
            event.getInventory().setResult(talisman.getItemStack());
        }
    }
}
