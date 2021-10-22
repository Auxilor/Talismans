package com.willfp.talismans.display;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.display.DisplayModule;
import com.willfp.eco.core.display.DisplayPriority;
import com.willfp.eco.util.SkullUtils;
import com.willfp.talismans.talismans.TalismanLevel;
import com.willfp.talismans.talismans.util.TalismanChecks;
import com.willfp.talismans.talismans.util.TalismanUtils;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TalismanDisplay extends DisplayModule {
    /**
     * Instantiate talisman display.
     *
     * @param plugin Instance of Talismans.
     */
    public TalismanDisplay(@NotNull final EcoPlugin plugin) {
        super(plugin, DisplayPriority.LOWEST);
    }

    @Override
    public void display(@NotNull final ItemStack itemStack,
                        @NotNull final Object... args) {
        if (!TalismanUtils.isTalismanMaterial(itemStack.getType())) {
            return;
        }

        ItemMeta meta = itemStack.getItemMeta();

        assert meta != null;

        List<String> itemLore = new ArrayList<>();

        if (meta.hasLore()) {
            itemLore = meta.getLore();
        }

        if (itemLore == null) {
            itemLore = new ArrayList<>();
        }

        TalismanLevel talisman = TalismanChecks.getTalismanOnItem(itemStack);

        if (talisman == null) {
            return;
        }

        if (meta instanceof SkullMeta) {
            SkullUtils.setSkullTexture((SkullMeta) meta, talisman.getSkullBase64());
        }

        meta.setDisplayName(talisman.getName());

        meta.setCustomModelData(talisman.getCustomModelData());

        List<String> lore = new ArrayList<>();

        lore.addAll(talisman.getFormattedDescription());

        lore.addAll(itemLore);
        meta.setLore(lore);
        itemStack.setItemMeta(meta);
    }
}
