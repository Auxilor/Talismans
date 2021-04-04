package com.willfp.talismans.fossil;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.extensions.Extension;
import com.willfp.talismans.fossil.talismans.ExperienceFossil;
import com.willfp.talismans.fossil.talismans.FeatherFossil;
import com.willfp.talismans.fossil.talismans.FlameFossil;
import com.willfp.talismans.fossil.talismans.FluxFossil;
import com.willfp.talismans.fossil.talismans.HealingFossil;
import com.willfp.talismans.fossil.talismans.ResistanceFossil;
import com.willfp.talismans.fossil.talismans.SpeedFossil;
import com.willfp.talismans.fossil.talismans.StrengthFossil;
import com.willfp.talismans.talismans.Talisman;
import org.jetbrains.annotations.NotNull;

public class FossilMain extends Extension {
    public static final FossilConfig CONFIG = new FossilConfig();

    public static final Talisman EXPERIENCE_FOSSIL = new ExperienceFossil();
    public static final Talisman FEATHER_FOSSIL = new FeatherFossil();
    public static final Talisman FLAME_FOSSIL = new FlameFossil();
    public static final Talisman FLUX_FOSSIL = new FluxFossil();
    public static final Talisman HEALING_FOSSIL = new HealingFossil();
    public static final Talisman RESISTANCE_FOSSIL = new ResistanceFossil();
    public static final Talisman SPEED_FOSSIL = new SpeedFossil();
    public static final Talisman STRENGTH_FOSSIL = new StrengthFossil();

    /**
     * Instantiate fossil extension.
     *
     * @param plugin Instance of Talismans.
     */
    public FossilMain(@NotNull final EcoPlugin plugin) {
        super(plugin);
    }

    @Override
    protected void onEnable() {

    }

    @Override
    protected void onDisable() {

    }
}
