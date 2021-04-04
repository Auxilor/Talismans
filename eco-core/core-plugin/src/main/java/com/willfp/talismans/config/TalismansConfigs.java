package com.willfp.talismans.config;

import com.willfp.eco.core.config.ConfigUpdater;
import com.willfp.talismans.config.configs.TalismanConfig;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

@UtilityClass
public class TalismansConfigs {
    /**
     * All talisman-specific configs.
     */
    @Getter
    private static final Set<TalismanConfig> TALISMAN_CONFIGS = new HashSet<>();

    /**
     * Update all configs.
     */
    @ConfigUpdater
    public void updateConfigs() {
        TALISMAN_CONFIGS.forEach(TalismanConfig::update);
    }

    /**
     * Get TalismanConfig matching config name.
     *
     * @param configName The config name to match.
     * @return The matching {@link TalismanConfig}.
     */
    @Nullable
    public TalismanConfig getTalismanConfig(@NotNull final String configName) {
        for (TalismanConfig config : TALISMAN_CONFIGS) {
            if (config.getName().equalsIgnoreCase(configName)) {
                return config;
            }
        }

        return null;
    }

    /**
     * Adds new talisman config yml.
     *
     * @param config The config to add.
     */
    public void addTalismanConfig(@NotNull final TalismanConfig config) {
        TALISMAN_CONFIGS.add(config);
    }
}
