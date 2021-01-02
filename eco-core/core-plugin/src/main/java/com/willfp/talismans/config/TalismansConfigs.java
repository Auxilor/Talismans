package com.willfp.talismans.config;

import com.willfp.eco.util.config.updating.annotations.ConfigUpdater;
import com.willfp.talismans.config.configs.TalismanConfig;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

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
        TALISMAN_CONFIGS.forEach(TalismanYamlConfig::update);
    }

    /**
     * Get TalismanConfig matching config name.
     *
     * @param configName The config name to match.
     * @return The matching {@link TalismanConfig}.
     */
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public TalismanConfig getTalismanConfig(@NotNull final String configName) {
        return TALISMAN_CONFIGS.stream().filter(config -> config.getName().equalsIgnoreCase(configName)).findFirst().get();
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
