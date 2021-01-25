package com.willfp.talismans.talismans.talismans;

import com.willfp.talismans.talismans.Talisman;
import com.willfp.talismans.talismans.Talismans;
import com.willfp.talismans.talismans.meta.TalismanStrength;
import com.willfp.talismans.talismans.util.equipevent.EquipType;
import com.willfp.talismans.talismans.util.equipevent.TalismanEquipEvent;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class SpeedTalisman extends Talisman {
    private static final UUID MODIFIER_UUID = UUID.nameUUIDFromBytes("speed_talisman".getBytes());
    private static AttributeModifier MODIFIER = new AttributeModifier(MODIFIER_UUID, "speed_talisman", 0.05, AttributeModifier.Operation.MULTIPLY_SCALAR_1);

    public SpeedTalisman(@NotNull final TalismanStrength strength) {
        super("speed_talisman", strength);
    }

    @Override
    protected void postUpdate() {
        MODIFIER = new AttributeModifier(MODIFIER_UUID, "speed_talisman", this.getConfig().getDouble(Talismans.CONFIG_LOCATION + "percentage-bonus") / 100, AttributeModifier.Operation.MULTIPLY_SCALAR_1);
    }

    @EventHandler
    public void listener(@NotNull final TalismanEquipEvent event) {
        Player player = event.getPlayer();

        if (!event.getTalisman().equals(this)) {
            return;
        }

        AttributeInstance movementSpeed = player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
        assert movementSpeed != null;

        if (event.getType() == EquipType.EQUIP) {
            if (this.getDisabledWorlds().contains(player.getWorld())) {
                movementSpeed.removeModifier(MODIFIER);
            } else {
                if (!movementSpeed.getModifiers().contains(MODIFIER)) {
                    movementSpeed.addModifier(MODIFIER);
                }
            }
        } else {
            movementSpeed.removeModifier(MODIFIER);
        }
    }
}
