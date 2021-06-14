package com.willfp.talismans.talismans.talismans;

import com.willfp.talismans.talismans.Talisman;
import com.willfp.talismans.talismans.TalismanLevel;
import com.willfp.talismans.talismans.Talismans;
import org.bukkit.World;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Trident;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;

public class NecromanceRing extends Talisman {
    public NecromanceRing() {
        super("necromance");
    }

    @Override
    public void onMeleeAttack(@NotNull final TalismanLevel level,
                              @NotNull final Player attacker,
                              @NotNull final LivingEntity victim,
                              @NotNull final EntityDamageByEntityEvent event) {
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

    @Override
    public void onArrowDamage(@NotNull final TalismanLevel level,
                              @NotNull final Player attacker,
                              @NotNull final LivingEntity victim,
                              @NotNull final Arrow arrow,
                              @NotNull final EntityDamageByEntityEvent event) {
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

    @Override
    public void onTridentDamage(@NotNull final TalismanLevel level,
                                @NotNull final Player attacker,
                                @NotNull final LivingEntity victim,
                                @NotNull final Trident trident,
                                @NotNull final EntityDamageByEntityEvent event) {
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
