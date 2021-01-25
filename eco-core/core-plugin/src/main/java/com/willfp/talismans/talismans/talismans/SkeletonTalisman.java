package com.willfp.talismans.talismans.talismans;

import com.willfp.talismans.talismans.Talisman;
import com.willfp.talismans.talismans.Talismans;
import com.willfp.talismans.talismans.meta.TalismanStrength;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Trident;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;

public class SkeletonTalisman extends Talisman {
    public SkeletonTalisman(@NotNull final TalismanStrength strength) {
        super("skeleton_talisman", strength);
    }

    @Override
    public void onMeleeAttack(@NotNull final Player attacker,
                              @NotNull final LivingEntity victim,
                              @NotNull final EntityDamageByEntityEvent event) {
        if (!(victim instanceof Skeleton)) {
            return;
        }

        event.setDamage(event.getDamage() * (1 + (this.getConfig().getDouble(Talismans.CONFIG_LOCATION + "percent-more-damage")) / 100));
    }

    @Override
    public void onArrowDamage(@NotNull final Player attacker,
                              @NotNull final LivingEntity victim,
                              @NotNull final Arrow arrow,
                              @NotNull final EntityDamageByEntityEvent event) {
        if (!(victim instanceof Skeleton)) {
            return;
        }

        event.setDamage(event.getDamage() * (1 + (this.getConfig().getDouble(Talismans.CONFIG_LOCATION + "percent-more-damage")) / 100));
    }

    @Override
    public void onTridentDamage(@NotNull final Player attacker,
                                @NotNull final LivingEntity victim,
                                @NotNull final Trident trident,
                                @NotNull final EntityDamageByEntityEvent event) {
        if (!(victim instanceof Skeleton)) {
            return;
        }

        event.setDamage(event.getDamage() * (1 + (this.getConfig().getDouble(Talismans.CONFIG_LOCATION + "percent-more-damage")) / 100));
    }
}
