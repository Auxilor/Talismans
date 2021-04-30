package com.willfp.talismans.talismans.util.equipevent;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.talismans.talismans.TalismanLevel;
import com.willfp.talismans.talismans.util.TalismanChecks;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class SyncTalismanEquipEventTask {
    /**
     * Cache for the scheduler.
     */
    private static final Map<UUID, Set<TalismanLevel>> syncCache = Collections.synchronizedMap(new HashMap<>());

    /**
     * Schedule sync repeating updater task.
     *
     * @param plugin The plugin to schedule for.
     */
    public static void scheduleAutocheck(@NotNull final EcoPlugin plugin) {
        plugin.getScheduler().syncRepeating(() -> {
            for (Player player : plugin.getServer().getOnlinePlayers()) {
                UUID uuid = player.getUniqueId();

                Set<TalismanLevel> buildingBefore = syncCache.get(uuid);
                if (buildingBefore == null) {
                    buildingBefore = new HashSet<>();
                }

                Set<TalismanLevel> before = buildingBefore;

                Set<TalismanLevel> after = TalismanChecks.getTalismansOnPlayer(player);

                for (TalismanLevel talisman : new HashSet<>(before)) {
                    if (after.contains(talisman)) {
                        before.remove(talisman);
                        after.remove(talisman);
                    }
                }

                syncCache.put(uuid, after);

                after.removeAll(before);
                for (TalismanLevel talisman : after) {
                    if (talisman.getTalisman().isEnabled()) {
                        Bukkit.getPluginManager().callEvent(new TalismanEquipEvent(player, talisman, EquipType.EQUIP));
                    }
                }

                before.removeAll(after);
                for (TalismanLevel talisman : before) {
                    Bukkit.getPluginManager().callEvent(new TalismanEquipEvent(player, talisman, EquipType.UNEQUIP));
                }
            }
        }, 80, 80);
    }
}
