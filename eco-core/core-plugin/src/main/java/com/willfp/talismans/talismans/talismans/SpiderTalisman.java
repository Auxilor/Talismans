package com.willfp.talismans.talismans.talismans;

import com.willfp.talismans.talismans.Talisman;
import com.willfp.talismans.talismans.Talismans;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Spider;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;

public class SpiderTalisman extends Talisman {
    public SpiderTalisman() {
        super("spider_talisman");
    }

    @Override
    public void onMeleeAttack(@NotNull final Player attacker,
                              @NotNull final LivingEntity victim,
                              @NotNull final EntityDamageByEntityEvent event) {
        if (!(victim instanceof Spider)) {
            return;
        }

        event.setDamage(event.getDamage() * (1 + (this.getConfig().getDouble(Talismans.CONFIG_LOCATION + "percent-more-damage")) / 100));
    }
}
