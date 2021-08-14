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
import java.util.UUID;

public class VibranceTalisman extends Talisman {
    private Map<TalismanLevel, AttributeModifier> modifiers;
    private final AttributeModifier legacyModifier = new AttributeModifier(
            UUID.nameUUIDFromBytes(this.getKey().getKey().getBytes()),
            this.getKey().getKey(),
            0,
            AttributeModifier.Operation.ADD_NUMBER
    );

    public VibranceTalisman() {
        super("vibrance");
    }

    @Override
    protected void postUpdate() {
        modifiers = new HashMap<>();
        for (TalismanLevel level : this.getLevels()) {
            modifiers.put(
                    level,
                    new AttributeModifier(
                            level.getUuid(),
                            level.getKey().getKey(),
                            level.getConfig().getDouble(Talismans.CONFIG_LOCATION + "bonus-hearts") / 2,
                            AttributeModifier.Operation.ADD_NUMBER
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

        AttributeInstance maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        assert maxHealth != null;

        AttributeModifier modifier = modifiers.get(event.getTalisman());

        maxHealth.removeModifier(legacyModifier);

        if (event.getType() == EquipType.EQUIP) {
            if (this.getDisabledWorlds().contains(player.getWorld())) {
                maxHealth.removeModifier(modifier);
            } else {
                if (!maxHealth.getModifiers().contains(modifier)) {
                    maxHealth.removeModifier(modifier);
                    maxHealth.addModifier(modifier);

                    player.setHealth(maxHealth.getValue());
                }
            }
        } else {
            maxHealth.removeModifier(modifier);
        }
    }
}
