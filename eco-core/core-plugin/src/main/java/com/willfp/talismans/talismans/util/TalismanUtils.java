package com.willfp.talismans.talismans.util;


import com.willfp.eco.util.NumberUtils;
import com.willfp.eco.util.StringUtils;
import com.willfp.eco.util.integrations.placeholder.PlaceholderEntry;
import com.willfp.eco.util.integrations.placeholder.PlaceholderManager;
import com.willfp.talismans.talismans.Talisman;
import com.willfp.talismans.talismans.Talismans;
import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;
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
    public static boolean passedChance(@NotNull final Talisman talisman) {
        return NumberUtils.randFloat(0, 1) < (talisman.getConfig().getDouble(Talismans.CONFIG_LOCATION + "chance") / 100);
    }

    /**
     * Register the placeholders for a talisman.
     *
     * @param talisman The talisman to register placeholders for.
     */
    public static void registerPlaceholders(@NotNull final Talisman talisman) {
        PlaceholderManager.registerPlaceholder(
                new PlaceholderEntry(
                        talisman.getConfigName() + "_" + "enabled",
                        player -> String.valueOf(talisman.isEnabled())
                )
        );

        talisman.getConfig().getConfig().getKeys(true).forEach(string -> {
            String key = string.replace("\\.", "_").replace("-", "_");
            Object object = talisman.getConfig().getConfig().get(string);

            PlaceholderManager.registerPlaceholder(
                    new PlaceholderEntry(
                            talisman.getConfigName() + "_" + key,
                            player -> StringUtils.internalToString(object)
                    )
            );
        });
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
}
