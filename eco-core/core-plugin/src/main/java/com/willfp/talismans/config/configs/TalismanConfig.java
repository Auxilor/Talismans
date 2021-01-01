package com.willfp.talismans.config.configs;

import com.willfp.talismans.config.TalismanYamlConfig;
import com.willfp.talismans.talismans.meta.TalismanStrength;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public class TalismanConfig extends TalismanYamlConfig {
    /**
     * The name of the config.
     */
    @Getter
    private final String name;

    /**
     * Instantiate a new config for a talisman.
     *
     * @param name     The name of the config.
     * @param strength The strength of the talisman.
     * @param plugin   The provider of the talisman.
     */
    public TalismanConfig(@NotNull final String name,
                          @NotNull final TalismanStrength strength,
                          @NotNull final Class<?> plugin) {
        super(name, strength, plugin);
        this.name = name;
    }
}
