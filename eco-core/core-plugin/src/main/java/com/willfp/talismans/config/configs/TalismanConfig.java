package com.willfp.talismans.config.configs;

import com.willfp.eco.util.config.ExtendableConfig;
import com.willfp.talismans.TalismansPlugin;
import com.willfp.talismans.talismans.meta.TalismanStrength;
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
     * @param strength The strength of the talisman.
     * @param source   The provider of the talisman.
     */
    public TalismanConfig(@NotNull final String name,
                          @NotNull final TalismanStrength strength,
                          @NotNull final Class<?> source) {
        super(name, true, TalismansPlugin.getInstance(), source, "talismans/" + strength.name().toLowerCase() + "/");
        this.name = name;
    }
}
