package com.willfp.talismans.talismans.talismans;

import com.willfp.talismans.talismans.Talisman;
import com.willfp.talismans.talismans.TalismanLevel;
import com.willfp.talismans.talismans.Talismans;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Boss;
import org.bukkit.entity.ElderGuardian;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("deprecation")
public class BossTalisman extends Talisman {
    public BossTalisman() {
        super("boss");
    }

    @Override
    public void onAnyAttack(@NotNull final TalismanLevel level,
                            @NotNull final Player attacker,
                            @NotNull final LivingEntity victim,
                            @NotNull final EntityDamageEvent event) {
        if (!(victim instanceof Boss || victim instanceof ElderGuardian) && !victim.getPersistentDataContainer().has(new NamespacedKey("ecobosses", "boss"), PersistentDataType.STRING)) {
            return;
        }

        event.setDamage(event.getDamage() * (1 + (level.getConfig().getDouble(Talismans.CONFIG_LOCATION + "percent-more-damage")) / 100));
    }
}
