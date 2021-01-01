package com.willfp.talismans.talismans.talismans.ring;

import com.willfp.eco.util.events.naturalexpgainevent.NaturalExpGainEvent;
import com.willfp.talismans.talismans.Talisman;
import com.willfp.talismans.talismans.Talismans;
import com.willfp.talismans.talismans.meta.TalismanStrength;
import com.willfp.talismans.talismans.util.TalismanChecks;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.jetbrains.annotations.NotNull;

public class ExperienceRing extends Talisman {
    public ExperienceRing() {
        super("experience_ring", TalismanStrength.RING);
    }

    @EventHandler
    public void onExpChange(@NotNull final NaturalExpGainEvent event) {
        Player player = event.getExpChangeEvent().getPlayer();

        if (event.getExpChangeEvent().getAmount() < 0) {
            return;
        }

        if (!TalismanChecks.hasTalisman(player, this)) {
            return;
        }

        if (this.getDisabledWorlds().contains(player.getWorld())) {
            return;
        }

        event.getExpChangeEvent().setAmount((int) Math.ceil(event.getExpChangeEvent().getAmount() * (1 + (this.getConfig().getDouble(Talismans.CONFIG_LOCATION + "percentage-bonus") / 100))));
    }
}
