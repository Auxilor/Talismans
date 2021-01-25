package com.willfp.talismans.talismans.talismans;

import com.willfp.talismans.talismans.Talisman;
import com.willfp.talismans.talismans.Talismans;
import com.willfp.talismans.talismans.meta.TalismanStrength;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;

public class FeatherTalisman extends Talisman {
    public FeatherTalisman(@NotNull final TalismanStrength strength) {
        super("feather", strength);
    }

    @Override
    public void onDamage(@NotNull final Player victim,
                         @NotNull final EntityDamageEvent event) {
        if (event.getCause() != EntityDamageEvent.DamageCause.FALL) {
            return;
        }

        event.setDamage(event.getDamage() * this.getConfig().getDouble(Talismans.CONFIG_LOCATION + "multiplier"));
    }
}
