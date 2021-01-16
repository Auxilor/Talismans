package com.willfp.talismans.talismans.talismans.ring;

import com.willfp.talismans.talismans.Talisman;
import com.willfp.talismans.talismans.Talismans;
import com.willfp.talismans.talismans.meta.TalismanStrength;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;

public class GravityRing extends Talisman {
    public GravityRing() {
        super("gravity_ring", TalismanStrength.RING);
    }

    @Override
    public void onDamage(@NotNull final Player victim,
                         @NotNull final EntityDamageEvent event) {
        if (victim.getLocation().getY() > 64 || victim.getLocation().getY() < 0) {
            return;
        }

        double below64 = 64 - victim.getLocation().getY();
        double multiplier = below64 / 64;
        double multiplierMultiplier = this.getConfig().getDouble(Talismans.CONFIG_LOCATION + "height-multiplier");
        multiplier *= multiplierMultiplier;
        multiplier += 1;

        event.setDamage(event.getDamage() * (1 / multiplier));
    }
}
