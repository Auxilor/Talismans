package com.willfp.talismans.proxy.v1_16_R2;

import com.willfp.talismans.proxy.proxies.CooldownProxy;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class Cooldown implements CooldownProxy {
    @Override
    public double getAttackCooldown(@NotNull final Player player) {
        return player.getAttackCooldown();
    }
}
