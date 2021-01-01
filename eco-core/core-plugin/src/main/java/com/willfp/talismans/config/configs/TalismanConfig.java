package com.willfp.talismans.config.configs;

import com.willfp.talismans.config.TalismanYamlConfig;
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
     * @param name   The name of the config.
     * @param plugin The provider of the talisman.
     */
    public TalismanConfig(@NotNull final String name,
                          @NotNull final Class<?> plugin) {
        super(name, plugin);
        this.name = name;
    }
}
