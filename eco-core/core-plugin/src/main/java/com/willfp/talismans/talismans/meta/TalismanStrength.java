package com.willfp.talismans.talismans.meta;

import com.willfp.eco.util.config.updating.annotations.ConfigUpdater;
import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import com.willfp.talismans.TalismansPlugin;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public enum TalismanStrength {

    /**
     * Weakest.
     */
    TALISMAN(() ->  TalismansPlugin.getInstance().getConfigYml().getString("strengths.talisman.color")),

    /**
     * Middle.
     */
    RING(() -> TalismansPlugin.getInstance().getConfigYml().getString("strengths.ring.color")),

    /**
     * Strongest.
     */
    RELIC(() -> TalismansPlugin.getInstance().getConfigYml().getString("strengths.relic.color"));

    /**
     * The color.
     */
    @Getter
    private String color;

    /**
     * Supplier to get the color.
     */
    private final Supplier<String> colorSupplier;

    /**
     * Create a new strength.
     *
     * @param colorSupplier The color supplier.
     */
    TalismanStrength(@NotNull final Supplier<String> colorSupplier) {
        this.colorSupplier = colorSupplier;
        this.color = colorSupplier.get();
    }

    /**
     * Update values.
     */
    @ConfigUpdater
    public static void update() {
        for (TalismanStrength strength : TalismanStrength.values()) {
            strength.color = strength.colorSupplier.get();
        }
    }
}
