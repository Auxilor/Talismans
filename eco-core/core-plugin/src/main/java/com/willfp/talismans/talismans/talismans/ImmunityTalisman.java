package com.willfp.talismans.talismans.talismans;

import com.willfp.talismans.talismans.Talisman;
import com.willfp.talismans.talismans.TalismanLevel;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;

public class ImmunityTalisman extends Talisman {
    public ImmunityTalisman() {
        super("immunity");
    }

    @Override
    public void onDamage(@NotNull final TalismanLevel level,
                         @NotNull final Player victim,
                         @NotNull final EntityDamageEvent event) {
        if (event.getCause() != EntityDamageEvent.DamageCause.POISON) {
            return;
        }

        event.setCancelled(true);
    }
}
