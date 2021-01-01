package com.willfp.talismans.talismans;


import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableList;
import com.willfp.eco.util.config.updating.annotations.ConfigUpdater;
import com.willfp.talismans.talismans.talismans.CreeperTalisman;
import com.willfp.talismans.talismans.talismans.EndTalisman;
import com.willfp.talismans.talismans.talismans.NetherTalisman;
import com.willfp.talismans.talismans.talismans.RaidTalisman;
import com.willfp.talismans.talismans.talismans.SkeletonTalisman;
import com.willfp.talismans.talismans.talismans.SpiderTalisman;
import com.willfp.talismans.talismans.talismans.ZombieTalisman;
import lombok.experimental.UtilityClass;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@UtilityClass
@SuppressWarnings({"unused", "checkstyle:JavadocVariable"})
public class Talismans {
    public static final String CONFIG_LOCATION = "config.";
    public static final String OBTAINING_LOCATION = "obtaining.";
    public static final String GENERAL_LOCATION = "general-config.";

    private static final BiMap<NamespacedKey, Talisman> BY_KEY = HashBiMap.create();

    public static final Talisman ZOMBIE_TALISMAN = new ZombieTalisman();
    public static final Talisman SKELETON_TALISMAN = new SkeletonTalisman();
    public static final Talisman CREEPER_TALISMAN = new CreeperTalisman();
    public static final Talisman SPIDER_TALISMAN = new SpiderTalisman();
    public static final Talisman RAID_TALISMAN = new RaidTalisman();
    public static final Talisman NETHER_TALISMAN = new NetherTalisman();
    public static final Talisman END_TALISMAN = new EndTalisman();

    /**
     * Get all registered {@link Talisman}s.
     *
     * @return A list of all {@link Talisman}s.
     */
    public static List<Talisman> values() {
        return ImmutableList.copyOf(BY_KEY.values());
    }

    /**
     * Get {@link Talisman} matching display name.
     *
     * @param name The display name to search for.
     * @return The matching {@link Talisman}, or null if not found.
     */
    public static Talisman getByName(@NotNull final String name) {
        Optional<Talisman> matching = values().stream().filter(talisman -> talisman.getName().equalsIgnoreCase(name)).findFirst();
        return matching.orElse(null);
    }

    /**
     * Get {@link Talisman} matching config name.
     *
     * @param configName The config name to search for.
     * @return The matching {@link Talisman}, or null if not found.
     */
    public static Talisman getByConfig(@NotNull final String configName) {
        Optional<Talisman> matching = values().stream().filter(talisman -> talisman.getConfigName().equalsIgnoreCase(configName)).findFirst();
        return matching.orElse(null);
    }

    /**
     * Get {@link Talisman} matching key.
     *
     * @param key The NamespacedKey to search for.
     * @return The matching {@link Talisman}, or null if not found.
     */
    public static Talisman getByKey(@Nullable final NamespacedKey key) {
        if (key == null) {
            return null;
        }
        return BY_KEY.get(key);
    }

    /**
     * Update all {@link Talisman}s.
     */
    @ConfigUpdater
    public static void update() {
        for (Talisman talisman : new HashSet<>(values())) {
            talisman.update();
        }
    }

    /**
     * Add new {@link Talisman} to Talismans.
     * <p>
     * Only for internal use, talismans are automatically added in the constructor.
     *
     * @param talisman The {@link Talisman} to add.
     */
    public static void addNewTalisman(@NotNull final Talisman talisman) {
        BY_KEY.remove(talisman.getKey());
        BY_KEY.put(talisman.getKey(), talisman);
    }

    /**
     * Remove {@link Talisman} from Talismans.
     *
     * @param talisman The {@link Talisman} to remove.
     */
    public static void removeTalisman(@NotNull final Talisman talisman) {
        BY_KEY.remove(talisman.getKey());
    }
}
