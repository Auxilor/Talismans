package com.willfp.talismans.talismans.talismans.talisman;

import com.willfp.talismans.talismans.Talisman;
import com.willfp.talismans.talismans.Talismans;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Spider;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;

public class SpiderResistanceTalisman extends Talisman {
    public SpiderResistanceTalisman() {
        super("spider_resistance_talisman");
    }

    @Override
    public void onDamageByEntity(@NotNull final Player victim,
                                 @NotNull final Entity attacker,
                                 @NotNull final EntityDamageByEntityEvent event) {
        if (!(attacker instanceof Spider)) {
            return;
        }

        event.setDamage(event.getDamage() * (1 - (this.getConfig().getDouble(Talismans.CONFIG_LOCATION + "percent-less-damage")) / 100));
    }
}
