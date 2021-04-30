package com.willfp.talismans.talismans.talismans;

import com.willfp.eco.core.drops.DropQueue;
import com.willfp.talismans.talismans.Talisman;
import com.willfp.talismans.talismans.TalismanLevel;
import com.willfp.talismans.talismans.Talismans;
import com.willfp.talismans.talismans.util.TalismanUtils;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.jetbrains.annotations.NotNull;

public class ExtractionTalisman extends Talisman {
    public ExtractionTalisman() {
        super("extraction");
    }

    @Override
    public void onBlockBreak(@NotNull final TalismanLevel level,
                             @NotNull final Player player,
                             @NotNull final Block block,
                             @NotNull final BlockBreakEvent event) {
        if (!TalismanUtils.passedChance(level)) {
            return;
        }

        new DropQueue(player)
                .addXP(level.getConfig().getInt(Talismans.CONFIG_LOCATION + "xp-amount"))
                .setLocation(block.getLocation())
                .push();
    }
}
