package com.willfp.talismans.talismans.talismans;

import com.willfp.talismans.talismans.Talisman;
import com.willfp.talismans.talismans.TalismanLevel;
import com.willfp.talismans.talismans.Talismans;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;

public class NecromanceRing extends Talisman {
    public NecromanceRing() {
        super("necromance");
    }

    @Override
    public void onAnyAttack(@NotNull final TalismanLevel level,
                            @NotNull final Player attacker,
                            @NotNull final LivingEntity victim,
                            @NotNull final EntityDamageEvent event) {
        int count = 0;
        double distance = level.getConfig().getDouble(Talismans.CONFIG_LOCATION + "distance");
        for (Entity nearbyEntity : attacker.getNearbyEntities(distance, distance, distance)) {
            if (!(nearbyEntity instanceof Zombie || nearbyEntity instanceof Skeleton)) {
                continue;
            }

            count++;
        }

        if (count == 0) {
            return;
        }

        double multiplier = level.getConfig().getDouble(Talismans.CONFIG_LOCATION + "multiplier-per-mob");
        multiplier *= count;
        multiplier += 1;

        event.setDamage(event.getDamage() * multiplier);
    }
}
