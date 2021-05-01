package com.willfp.talismans.talismans.talismans;

import com.willfp.eco.core.events.NaturalExpGainEvent;
import com.willfp.talismans.talismans.Talisman;
import com.willfp.talismans.talismans.TalismanLevel;
import com.willfp.talismans.talismans.Talismans;
import com.willfp.talismans.talismans.util.TalismanChecks;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.jetbrains.annotations.NotNull;

public class ExperienceTalisman extends Talisman {
    public ExperienceTalisman() {
        super("experience");
    }

    @EventHandler
    public void onExpChange(@NotNull final NaturalExpGainEvent event) {
        Player player = event.getExpChangeEvent().getPlayer();

        if (event.getExpChangeEvent().getAmount() < 0) {
            return;
        }

        TalismanLevel level = TalismanChecks.getTalismanLevel(player, this);

        if (level == null) {
            return;
        }

        if (this.getDisabledWorlds().contains(player.getWorld())) {
            return;
        }

        event.getExpChangeEvent().setAmount((int) Math.ceil(event.getExpChangeEvent().getAmount() * (1 + (level.getConfig().getDouble(Talismans.CONFIG_LOCATION + "percentage-bonus") / 100))));
    }
}
