package com.willfp.talismans.talismans.meta;

import com.willfp.eco.util.config.Configs;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public enum TalismanStrength {
    TALISMAN(() -> Configs.CONFIG.getString("strengths.talisman.color")),
    RING(() -> Configs.CONFIG.getString("strengths.ring.color")),
    RELIC(() -> Configs.CONFIG.getString("strengths.relic.color"));

    @Getter
    private String color;
    private final Supplier<String> colorSupplier;

    TalismanStrength(@NotNull final Supplier<String> colorSupplier) {
        this.colorSupplier = colorSupplier;
        this.color = colorSupplier.get();
    }

    public static void update() {
        for (TalismanStrength strength : TalismanStrength.values()) {
            strength.color = strength.colorSupplier.get();
        }
    }
}
