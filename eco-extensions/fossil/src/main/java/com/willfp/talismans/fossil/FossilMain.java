package com.willfp.talismans.fossil;

import com.willfp.eco.util.extensions.Extension;
import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import com.willfp.talismans.talismans.Talisman;
import com.willfp.talismans.talismans.talismans.ExperienceTalisman;
import com.willfp.talismans.talismans.talismans.FeatherTalisman;
import com.willfp.talismans.talismans.talismans.FlameTalisman;
import com.willfp.talismans.talismans.talismans.FluxTalisman;
import com.willfp.talismans.talismans.talismans.HealingTalisman;
import com.willfp.talismans.talismans.talismans.ResistanceTalisman;
import com.willfp.talismans.talismans.talismans.SpeedTalisman;
import com.willfp.talismans.talismans.talismans.StrengthTalisman;
import org.jetbrains.annotations.NotNull;

public class FossilMain extends Extension {
    public static final FossilConfig CONFIG = new FossilConfig();

    public static final Talisman EXPERIENCE_FOSSIL = new ExperienceTalisman(FossilStrength.FOSSIL);
    public static final Talisman FEATHER_FOSSIL = new FeatherTalisman(FossilStrength.FOSSIL);
    public static final Talisman FLAME_FOSSIL = new FlameTalisman(FossilStrength.FOSSIL);
    public static final Talisman FLUX_FOSSIL = new FluxTalisman(FossilStrength.FOSSIL);
    public static final Talisman HEALING_FOSSIL = new HealingTalisman(FossilStrength.FOSSIL);
    public static final Talisman RESISTANCE_FOSSIL = new ResistanceTalisman(FossilStrength.FOSSIL);
    public static final Talisman SPEED_FOSSIL = new SpeedTalisman(FossilStrength.FOSSIL);
    public static final Talisman STRENGTH_FOSSIL = new StrengthTalisman(FossilStrength.FOSSIL);

    /**
     * Instantiate fossil extension.
     *
     * @param plugin Instance of Talismans.
     */
    public FossilMain(@NotNull final AbstractEcoPlugin plugin) {
        super(plugin);
    }

    @Override
    protected void onEnable() {

    }

    @Override
    protected void onDisable() {

    }
}
