package com.willfp.talismans.talismans.talismans;

import com.willfp.talismans.talismans.Talisman;
import com.willfp.talismans.talismans.TalismanLevel;
import com.willfp.talismans.talismans.Talismans;
import com.willfp.talismans.talismans.util.equipevent.EquipType;
import com.willfp.talismans.talismans.util.equipevent.TalismanEquipEvent;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class SpeedTalisman extends Talisman {
    private final Map<TalismanLevel, AttributeModifier> modifiers = new HashMap<>();

    public SpeedTalisman() {
        super("speed");
    }

    @Override
    protected void postUpdate() {
        modifiers.clear();
        for (TalismanLevel level : this.getLevels()) {
            modifiers.put(
                    level,
                    new AttributeModifier(
                            level.getUuid(),
                            level.getKey().getKey(),
                            level.getConfig().getDouble(Talismans.CONFIG_LOCATION + "percentage-bonus") / 100,
                            AttributeModifier.Operation.MULTIPLY_SCALAR_1
                    )
            );
        }
    }

    @EventHandler
    public void listener(@NotNull final TalismanEquipEvent event) {
        Player player = event.getPlayer();

        if (!event.getTalisman().getTalisman().equals(this)) {
            return;
        }

        AttributeInstance movementSpeed = player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
        assert movementSpeed != null;

        AttributeModifier modifier = modifiers.get(event.getTalisman());

        if (event.getType() == EquipType.EQUIP) {
            if (this.getDisabledWorlds().contains(player.getWorld())) {
                movementSpeed.removeModifier(modifier);
            } else {
                if (!movementSpeed.getModifiers().contains(modifier)) {
                    movementSpeed.addModifier(modifier);
                }
            }
        } else {
            movementSpeed.removeModifier(modifier);
        }
    }
}
