package com.willfp.talismans.config;

import com.willfp.eco.util.config.ExtendableConfig;
import com.willfp.talismans.TalismansPlugin;
import com.willfp.talismans.talismans.meta.TalismanStrength;
import org.jetbrains.annotations.NotNull;

public abstract class TalismanYamlConfig extends ExtendableConfig {
    /**
     * Create new talisman config yml.
     *
     * @param name     The config name.
     * @param strength The talisman strength.
     * @param source   The class of the main class of source or extension.
     */
    protected TalismanYamlConfig(@NotNull final String name,
                                 @NotNull final TalismanStrength strength,
                                 @NotNull final Class<?> source) {
        super(name, true, TalismansPlugin.getInstance(), source, "talismans/" + strength.name().toLowerCase() + "/");
    }
}
