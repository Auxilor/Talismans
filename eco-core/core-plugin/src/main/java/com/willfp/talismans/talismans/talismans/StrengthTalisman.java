package com.willfp.talismans.talismans.talismans;

import com.willfp.talismans.talismans.Talisman;
import com.willfp.talismans.talismans.TalismanLevel;
import com.willfp.talismans.talismans.Talismans;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;

public class StrengthTalisman extends Talisman {
    public StrengthTalisman() {
        super("strength");
    }

    @Override
    public void onAnyAttack(@NotNull final TalismanLevel level,
                            @NotNull final Player attacker,
                            @NotNull final LivingEntity victim,
                            @NotNull final EntityDamageEvent event) {
        event.setDamage(event.getDamage() * (1 + (level.getConfig().getDouble(Talismans.CONFIG_LOCATION + "percent-more-damage")) / 100));
    }
}
