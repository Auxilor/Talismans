package com.willfp.talismans.talismans.talismans.talisman;

import com.willfp.talismans.talismans.Talisman;
import com.willfp.talismans.talismans.meta.TalismanStrength;
import com.willfp.talismans.talismans.util.equipevent.EquipType;
import com.willfp.talismans.talismans.util.equipevent.TalismanEquipEvent;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.jetbrains.annotations.NotNull;

public class SpeedTalisman extends Talisman {
    private static final AttributeModifier MODIFIER = new AttributeModifier("speed_talisman", 0.2, AttributeModifier.Operation.ADD_NUMBER);

    public SpeedTalisman() {
        super("speed_talisman", TalismanStrength.TALISMAN);
    }

    @EventHandler
    public void onExpChange(@NotNull final TalismanEquipEvent event) {
        Player player = event.getPlayer();

        if (!event.getTalisman().equals(this)) {
            return;
        }

        if (event.getType() == EquipType.EQUIP) {
            if (this.getDisabledWorlds().contains(player.getWorld())) {
                player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).removeModifier(MODIFIER);
            } else {
                player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).addModifier(MODIFIER);
            }
        } else {
            Bukkit.getLogger().info("bruh");
            player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).removeModifier(MODIFIER);
        }
    }
}
