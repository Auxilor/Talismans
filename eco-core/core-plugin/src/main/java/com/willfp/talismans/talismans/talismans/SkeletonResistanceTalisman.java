package com.willfp.talismans.talismans.talismans;

import com.willfp.talismans.talismans.Talisman;
import com.willfp.talismans.talismans.TalismanLevel;
import com.willfp.talismans.talismans.Talismans;
import lombok.val;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;

public class SkeletonResistanceTalisman extends Talisman {
    public SkeletonResistanceTalisman() {
        super("skeleton_resistance");
    }

    @Override
    public void onDamageByEntity(@NotNull final TalismanLevel level,
                                 @NotNull final Player victim,
                                 @NotNull final Entity attacker,
                                 @NotNull final EntityDamageByEntityEvent event) {
        if (!(attacker instanceof AbstractArrow arrow)) {
            return;
        }

        if (!(arrow.getShooter() instanceof Skeleton)) {
            return;
        }

        event.setDamage(event.getDamage() * (1 - (level.getConfig().getDouble(Talismans.CONFIG_LOCATION + "percent-less-damage")) / 100));
    }
}
