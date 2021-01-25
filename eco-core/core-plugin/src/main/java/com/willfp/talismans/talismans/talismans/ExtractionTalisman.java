package com.willfp.talismans.talismans.talismans;

import com.willfp.eco.util.drops.DropQueue;
import com.willfp.talismans.talismans.Talisman;
import com.willfp.talismans.talismans.Talismans;
import com.willfp.talismans.talismans.meta.TalismanStrength;
import com.willfp.talismans.talismans.util.TalismanUtils;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.jetbrains.annotations.NotNull;

public class ExtractionTalisman extends Talisman {
    public ExtractionTalisman(@NotNull final TalismanStrength strength) {
        super("extraction_talisman", strength);
    }

    @Override
    public void onBlockBreak(@NotNull final Player player,
                             @NotNull final Block block,
                             @NotNull final BlockBreakEvent event) {
        if (!TalismanUtils.passedChance(this)) {
            return;
        }

        new DropQueue(player)
                .addXP(this.getConfig().getInt(Talismans.CONFIG_LOCATION + "xp-amount"))
                .setLocation(block.getLocation())
                .push();
    }
}
