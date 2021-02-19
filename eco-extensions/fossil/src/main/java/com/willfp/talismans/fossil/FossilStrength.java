package com.willfp.talismans.fossil;

import com.willfp.talismans.talismans.meta.TalismanStrength;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class FossilStrength extends TalismanStrength {
    public static final TalismanStrength FOSSIL = new FossilStrength("fossil", () -> FossilMain.CONFIG.getString("fossil-color"));

    /**
     * Create a new strength.
     *
     * @param name          The name.
     * @param colorSupplier The color supplier.
     */
    protected FossilStrength(@NotNull final String name,
                             @NotNull final Supplier<String> colorSupplier) {
        super(name, colorSupplier);
    }
}
