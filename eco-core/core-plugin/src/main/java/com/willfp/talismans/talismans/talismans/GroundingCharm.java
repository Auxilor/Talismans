package com.willfp.talismans.talismans.talismans;

import com.willfp.eco.core.integrations.mcmmo.McmmoManager;
import com.willfp.talismans.talismans.Talisman;
import com.willfp.talismans.talismans.util.TalismanChecks;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

public class GroundingCharm extends Talisman {
    public GroundingCharm() {
        super("grounding");
    }

    @EventHandler
    public void listener(@NotNull final EntityPotionEffectEvent event) {
        if (McmmoManager.isFake(event)) {
            return;
        }

        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        if (!TalismanChecks.hasTalisman(player, this)) {
            return;
        }

        if (event.getNewEffect() == null) {
            return;
        }

        if (!event.getNewEffect().getType().equals(PotionEffectType.LEVITATION)) {
            return;
        }

        if (event.isCancelled()) {
            return;
        }

        if (this.getDisabledWorlds().contains(player.getWorld())) {
            return;
        }

        event.setCancelled(true);
    }
}
