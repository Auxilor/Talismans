package com.willfp.talismans.talismans.talismans;

import com.willfp.talismans.talismans.Talisman;
import com.willfp.talismans.talismans.TalismanLevel;
import com.willfp.talismans.talismans.Talismans;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;

public class DayCrystal extends Talisman {
    public DayCrystal() {
        super("day");
    }

    @Override
    public void onAnyAttack(@NotNull final TalismanLevel level,
                            @NotNull final Player attacker,
                            @NotNull final LivingEntity victim,
                            @NotNull final EntityDamageEvent event) {
        if (!attacker.getWorld().getEnvironment().equals(World.Environment.NORMAL)) {
            return;
        }

        if (!(attacker.getWorld().getTime() < 12300 && attacker.getWorld().getTime() > 23850)) {
            return;
        }

        event.setDamage(event.getDamage() * (1 + (level.getConfig().getDouble(Talismans.CONFIG_LOCATION + "percent-more-damage")) / 100));
    }
}
