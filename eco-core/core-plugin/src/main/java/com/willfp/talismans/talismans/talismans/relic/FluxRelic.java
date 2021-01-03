package com.willfp.talismans.talismans.talismans.relic;

import com.willfp.talismans.integrations.mcmmo.McmmoManager;
import com.willfp.talismans.talismans.Talisman;
import com.willfp.talismans.talismans.Talismans;
import com.willfp.talismans.talismans.meta.TalismanStrength;
import com.willfp.talismans.talismans.util.TalismanChecks;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;

public class FluxRelic extends Talisman {
    public FluxRelic() {
        super("flux_relic", TalismanStrength.RELIC);
    }

    @EventHandler
    public void onMeleeAttack(@NotNull final EntityDamageByEntityEvent event) {
        if (McmmoManager.isFake(event)) {
            return;
        }

        if (!(event.getDamager() instanceof Player)) {
            return;
        }

        if (event.isCancelled()) {
            return;
        }

        Player attacker = (Player) event.getDamager();

        if (this.getDisabledWorlds().contains(attacker.getWorld())) {
            return;
        }

        double distance = this.getConfig().getDouble(Talismans.CONFIG_LOCATION + "distance");

        for (Entity nearbyEntity : attacker.getNearbyEntities(distance, distance, distance)) {
            if (!(nearbyEntity instanceof Player)) {
                continue;
            }

            Player player = (Player) nearbyEntity;

            if (!TalismanChecks.hasTalisman(player, this)) {
                continue;
            }

            event.setDamage(event.getDamage() * (1 + (this.getConfig().getDouble(Talismans.CONFIG_LOCATION + "percent-more-damage")) / 100));
        }
    }
}
