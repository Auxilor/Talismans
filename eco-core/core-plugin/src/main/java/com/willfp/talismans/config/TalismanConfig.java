package com.willfp.talismans.config;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.config.ExtendableConfig;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public class TalismanConfig extends ExtendableConfig {
    /**
     * The name of the config.
     */
    @Getter
    private final String name;

    /**
     * Instantiate a new config for a talisman.
     *
     * @param name     The name of the config.
     * @param source   The provider of the talisman.
     * @param plugin   Instance of talismans.
     */
    public TalismanConfig(@NotNull final String name,
                          @NotNull final Class<?> source,
                          @NotNull final EcoPlugin plugin) {
        super(name, true, plugin, source, "talismans/");
        this.name = name;
    }
}
