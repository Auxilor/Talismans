package com.willfp.talismans.talismans.util;


import com.willfp.eco.util.NumberUtils;
import com.willfp.talismans.TalismansPlugin;
import com.willfp.talismans.talismans.Talisman;
import com.willfp.talismans.talismans.TalismanLevel;
import com.willfp.talismans.talismans.Talismans;
import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

@UtilityClass
public class TalismanUtils {
    /**
     * All valid materials for talismans.
     */
    private static final Set<Material> TALISMAN_MATERIALS = new HashSet<>();

    /**
     * If the talisman has successfully passed its specified chance.
     *
     * @param talisman The talisman to query.
     * @return If the talisman should then be executed.
     */
    public static boolean passedChance(@NotNull final TalismanLevel talisman) {
        return NumberUtils.randFloat(0, 1) < (talisman.getConfig().getDouble(Talismans.CONFIG_LOCATION + "chance") / 100);
    }

    /**
     * Get limit for talisman reading.
     *
     * @param player The player to check.
     * @return The limit.
     */
    public static int getLimit(@NotNull final Player player) {
        String prefix = "talismans.limit.";
        for (PermissionAttachmentInfo permissionAttachmentInfo : player.getEffectivePermissions()) {
            String permission = permissionAttachmentInfo.getPermission();
            if (permission.startsWith(prefix)) {
                return Integer.parseInt(permission.substring(permission.lastIndexOf(".") + 1));
            }
        }

        return 100000;
    }

    /**
     * Get if a talisman could exist for a material.
     *
     * @param material The material.
     * @return If possible.
     */
    public static boolean isTalismanMaterial(@NotNull final Material material) {
        return TALISMAN_MATERIALS.contains(material);
    }

    /**
     * Register material as valid for talisman.
     *
     * @param material The material.
     */
    public static void registerTalismanMaterial(@NotNull final Material material) {
        TALISMAN_MATERIALS.add(material);
    }

    /**
     * Unregister all materials as valid for talismans.
     */
    public static void clearTalismanMaterials() {
        TALISMAN_MATERIALS.clear();
    }

    /**
     * Update legacy talisman to 3.0.0 talisman.
     *
     * @param itemStack The Talisman ItemStack.
     */
    public static void convertFromLegacy(@NotNull final ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();


        NamespacedKey talismanKey = container.getKeys().stream().filter(namespacedKey -> namespacedKey.getNamespace().equals("talismans")).findFirst().orElse(null);
        if (talismanKey == null) {
            return;
        }

        String key = talismanKey.getKey();

        int level = 0;

        if (key.endsWith("_talisman")) {
            level = 1;
        } else if (key.endsWith("_ring")) {
            level = 2;
        } else if (key.endsWith("_relic")) {
            level = 3;
        } else if (key.endsWith("_fossil")) {
            level = 4;
        } else {
            return;
        }

        if (level == 0) {
            return;
        }

        String newKey = key.replace("_talisman", "")
                .replace("_ring", "")
                .replace("_relic", "")
                .replace("_fossil", "");

        container.remove(talismanKey);

        NamespacedKey newTalismanKey = TalismansPlugin.getInstance().getNamespacedKeyFactory().create(newKey);

        container.set(newTalismanKey, PersistentDataType.INTEGER, level);

        itemStack.setItemMeta(meta);
    }
}
