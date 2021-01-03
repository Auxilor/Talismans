package com.willfp.talismans.talismans.talismans.talisman;

import com.willfp.talismans.talismans.Talisman;
import com.willfp.talismans.talismans.Talismans;
import com.willfp.talismans.talismans.meta.TalismanStrength;
import com.willfp.talismans.talismans.util.TalismanChecks;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.jetbrains.annotations.NotNull;

public class HealingTalisman extends Talisman {
    public HealingTalisman() {
        super("healing_talisman", TalismanStrength.TALISMAN);
    }

    @EventHandler
    public void onHeal(@NotNull final EntityRegainHealthEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        if (!event.getRegainReason().equals(EntityRegainHealthEvent.RegainReason.SATIATED) && !event.getRegainReason().equals(EntityRegainHealthEvent.RegainReason.REGEN)) {
            return;
        }

        Player player = (Player) event.getEntity();

        if (this.getDisabledWorlds().contains(player.getWorld())) {
            return;
        }

        if (!TalismanChecks.hasTalisman(player, this)) {
            return;
        }

        double amount = this.getConfig().getDouble(Talismans.CONFIG_LOCATION + "multiplier");
        amount += 1;

        event.setAmount(event.getAmount() * amount);
    }
}
