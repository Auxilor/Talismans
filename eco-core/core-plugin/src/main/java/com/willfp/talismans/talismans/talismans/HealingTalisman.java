package com.willfp.talismans.talismans.talismans;

import com.willfp.talismans.talismans.Talisman;
import com.willfp.talismans.talismans.TalismanLevel;
import com.willfp.talismans.talismans.Talismans;
import com.willfp.talismans.talismans.util.TalismanChecks;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.jetbrains.annotations.NotNull;

public class HealingTalisman extends Talisman {
    public HealingTalisman() {
        super("healing");
    }

    @EventHandler
    public void onHeal(@NotNull final EntityRegainHealthEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        if (!event.getRegainReason().equals(EntityRegainHealthEvent.RegainReason.SATIATED) && !event.getRegainReason().equals(EntityRegainHealthEvent.RegainReason.REGEN)) {
            return;
        }

        if (this.getDisabledWorlds().contains(player.getWorld())) {
            return;
        }

        TalismanLevel level = TalismanChecks.getTalismanLevel(player, this);

        if (level == null) {
            return;
        }

        double amount = level.getConfig().getDouble(Talismans.CONFIG_LOCATION + "multiplier");
        amount += 1;

        event.setAmount(event.getAmount() * amount);
    }
}
