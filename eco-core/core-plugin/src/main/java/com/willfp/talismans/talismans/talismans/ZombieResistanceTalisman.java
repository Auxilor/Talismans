package com.willfp.talismans.talismans.talismans;

import com.willfp.talismans.talismans.Talisman;
import com.willfp.talismans.talismans.Talismans;
import com.willfp.talismans.talismans.meta.TalismanStrength;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;

public class ZombieResistanceTalisman extends Talisman {
    public ZombieResistanceTalisman(@NotNull final TalismanStrength strength) {
        super("zombie_resistance", strength);
    }

    @Override
    public void onDamageByEntity(@NotNull final Player victim,
                                 @NotNull final Entity attacker,
                                 @NotNull final EntityDamageByEntityEvent event) {
        if (!(attacker instanceof Zombie)) {
            return;
        }

        event.setDamage(event.getDamage() * (1 - (this.getConfig().getDouble(Talismans.CONFIG_LOCATION + "percent-less-damage")) / 100));
    }
}
