package com.willfp.talismans.talismans.meta;

import com.google.common.collect.ImmutableSet;
import com.willfp.eco.core.config.ConfigUpdater;
import com.willfp.talismans.TalismansPlugin;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class TalismanStrength {
    /**
     * All registered strengths.
     */
    private static final Map<String, TalismanStrength> REGISTERED = new HashMap<>();

    /**
     * Weakest.
     */
    public static final TalismanStrength TALISMAN = new TalismanStrength("talisman", () -> TalismansPlugin.getInstance().getConfigYml().getString("strengths.talisman.color"));

    /**
     * Middle.
     */
    public static final TalismanStrength RING = new TalismanStrength("ring", () -> TalismansPlugin.getInstance().getConfigYml().getString("strengths.ring.color"));

    /**
     * Strongest.
     */
    public static final TalismanStrength RELIC = new TalismanStrength("relic", () -> TalismansPlugin.getInstance().getConfigYml().getString("strengths.relic.color"));

    /**
     * The color.
     */
    @Getter
    private String color;

    /**
     * The name.
     */
    private final String name;

    /**
     * Supplier to get the color.
     */
    private final Supplier<String> colorSupplier;

    /**
     * Create a new strength.
     *
     * @param name          The name.
     * @param colorSupplier The color supplier.
     */
    protected TalismanStrength(@NotNull final String name,
                               @NotNull final Supplier<String> colorSupplier) {
        this.colorSupplier = colorSupplier;
        this.name = name;
        this.color = colorSupplier.get();
    }

    /**
     * Get the name of the strength.
     *
     * @return The name.
     */
    public String name() {
        return name;
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

    /**
     * Get talisman strength by name.
     *
     * @param name The name.
     * @return The strength, or null if not found.
     */
    @Nullable
    public static TalismanStrength valueOf(@NotNull final String name) {
        return REGISTERED.get(name);
    }

    /**
     * Get all registered talisman strengths.
     *
     * @return A collection of strengths.
     */
    public static Set<TalismanStrength> values() {
        return ImmutableSet.copyOf(REGISTERED.values());
    }
}
