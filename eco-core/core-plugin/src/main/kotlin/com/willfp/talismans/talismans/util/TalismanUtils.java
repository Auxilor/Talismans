package com.willfp.talismans.talismans.util;


import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class TalismanUtils {
    /**
     * All valid materials for talismans.
     */
    private static final Set<Material> TALISMAN_MATERIALS = new HashSet<>();

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
}
