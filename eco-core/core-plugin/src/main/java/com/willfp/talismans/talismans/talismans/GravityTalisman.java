package com.willfp.talismans.talismans.talismans;

import com.willfp.talismans.talismans.Talisman;
import com.willfp.talismans.talismans.TalismanLevel;
import com.willfp.talismans.talismans.Talismans;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;

public class GravityTalisman extends Talisman {
    public GravityTalisman() {
        super("gravity");
    }

    @Override
    public void onDamage(@NotNull final TalismanLevel level,
                         @NotNull final Player victim,
                         @NotNull final EntityDamageEvent event) {
        if (victim.getLocation().getY() > 64) {
            return;
        }

        double below64 = 64 - victim.getLocation().getY();
        double multiplier = below64 / 64;
        double multiplierMultiplier = level.getConfig().getDouble(Talismans.CONFIG_LOCATION + "height-multiplier");
        multiplier *= multiplierMultiplier;
        multiplier += 1;

        event.setDamage(event.getDamage() * (1 / multiplier));
    }
}
