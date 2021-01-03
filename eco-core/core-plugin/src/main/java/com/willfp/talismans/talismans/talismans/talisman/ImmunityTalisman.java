package com.willfp.talismans.talismans.talismans.talisman;

import com.willfp.talismans.talismans.Talisman;
import com.willfp.talismans.talismans.meta.TalismanStrength;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;

public class ImmunityTalisman extends Talisman {
    public ImmunityTalisman() {
        super("immunity_talisman", TalismanStrength.TALISMAN);
    }

    @Override
    public void onDamage(@NotNull final Player victim,
                         @NotNull final EntityDamageEvent event) {
        if (event.getCause() != EntityDamageEvent.DamageCause.POISON) {
            return;
        }

        event.setCancelled(true);
    }
}
