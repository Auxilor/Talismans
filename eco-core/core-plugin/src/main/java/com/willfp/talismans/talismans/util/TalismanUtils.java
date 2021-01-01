package com.willfp.talismans.talismans.util;


import com.willfp.eco.util.NumberUtils;
import com.willfp.eco.util.StringUtils;
import com.willfp.eco.util.integrations.placeholder.PlaceholderEntry;
import com.willfp.eco.util.integrations.placeholder.PlaceholderManager;
import com.willfp.talismans.talismans.Talisman;
import com.willfp.talismans.talismans.Talismans;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class TalismanUtils {
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

        if (talisman.getConfig().getConfig().get(Talismans.CONFIG_LOCATION + "chance-per-level") != null) {
            PlaceholderManager.registerPlaceholder(
                    new PlaceholderEntry(
                            talisman.getConfigName() + "_" + "chance_per_level",
                            player -> NumberUtils.format(talisman.getConfig().getDouble(Talismans.CONFIG_LOCATION + "chance-per-level"))
                    )
            );
        }

        if (talisman.getConfig().getConfig().get(Talismans.CONFIG_LOCATION + "multiplier") != null) {
            PlaceholderManager.registerPlaceholder(
                    new PlaceholderEntry(
                            talisman.getConfigName() + "_" + "multiplier",
                            player -> NumberUtils.format(talisman.getConfig().getDouble(Talismans.CONFIG_LOCATION + "multiplier"))
                    )
            );
            PlaceholderManager.registerPlaceholder(
                    new PlaceholderEntry(
                            talisman.getConfigName() + "_" + "multiplier_percentage",
                            player -> NumberUtils.format(talisman.getConfig().getDouble(Talismans.CONFIG_LOCATION + "multiplier") * 100)
                    )
            );
        }
    }
}
