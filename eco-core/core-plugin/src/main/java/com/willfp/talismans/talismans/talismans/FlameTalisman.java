package com.willfp.talismans.talismans.talismans;

import com.willfp.talismans.talismans.Talisman;
import com.willfp.talismans.talismans.TalismanLevel;
import com.willfp.talismans.talismans.Talismans;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;

public class FlameTalisman extends Talisman {
    public FlameTalisman() {
        super("flame");
    }

    @Override
    public void onDamage(@NotNull final TalismanLevel level,
                         @NotNull final Player victim,
                         @NotNull final EntityDamageEvent event) {
        if (event.getCause() != EntityDamageEvent.DamageCause.FIRE && event.getCause() != EntityDamageEvent.DamageCause.FIRE_TICK
        && event.getCause() != EntityDamageEvent.DamageCause.LAVA && event.getCause() != EntityDamageEvent.DamageCause.HOT_FLOOR) {
            return;
        }

        event.setDamage(event.getDamage() * level.getConfig().getDouble(Talismans.CONFIG_LOCATION + "multiplier"));
    }
}
