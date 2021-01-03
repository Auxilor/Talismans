package com.willfp.talismans.talismans;


import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableList;
import com.willfp.eco.util.config.updating.annotations.ConfigUpdater;
import com.willfp.talismans.talismans.talismans.relic.AlchemyRelic;
import com.willfp.talismans.talismans.talismans.relic.ArcheryRelic;
import com.willfp.talismans.talismans.talismans.relic.EndRelic;
import com.willfp.talismans.talismans.talismans.relic.ExperienceRelic;
import com.willfp.talismans.talismans.talismans.relic.ExtractionRelic;
import com.willfp.talismans.talismans.talismans.relic.FeatherRelic;
import com.willfp.talismans.talismans.talismans.relic.FlameRelic;
import com.willfp.talismans.talismans.talismans.relic.FluxRelic;
import com.willfp.talismans.talismans.talismans.relic.HealingRelic;
import com.willfp.talismans.talismans.talismans.relic.NetherRelic;
import com.willfp.talismans.talismans.talismans.relic.PoseidonRelic;
import com.willfp.talismans.talismans.talismans.relic.RaidRelic;
import com.willfp.talismans.talismans.talismans.relic.ResistanceRelic;
import com.willfp.talismans.talismans.talismans.relic.SharpnessRelic;
import com.willfp.talismans.talismans.talismans.relic.StrengthRelic;
import com.willfp.talismans.talismans.talismans.ring.AlchemyRing;
import com.willfp.talismans.talismans.talismans.ring.ArcheryRing;
import com.willfp.talismans.talismans.talismans.ring.EndRing;
import com.willfp.talismans.talismans.talismans.ring.ExperienceRing;
import com.willfp.talismans.talismans.talismans.ring.ExtractionRing;
import com.willfp.talismans.talismans.talismans.ring.FeatherRing;
import com.willfp.talismans.talismans.talismans.ring.FlameRing;
import com.willfp.talismans.talismans.talismans.ring.FluxRing;
import com.willfp.talismans.talismans.talismans.ring.HealingRing;
import com.willfp.talismans.talismans.talismans.ring.NetherRing;
import com.willfp.talismans.talismans.talismans.ring.PoseidonRing;
import com.willfp.talismans.talismans.talismans.ring.RaidRing;
import com.willfp.talismans.talismans.talismans.ring.ResistanceRing;
import com.willfp.talismans.talismans.talismans.ring.SharpnessRing;
import com.willfp.talismans.talismans.talismans.ring.StrengthRing;
import com.willfp.talismans.talismans.talismans.talisman.AlchemyTalisman;
import com.willfp.talismans.talismans.talismans.talisman.ArcheryTalisman;
import com.willfp.talismans.talismans.talismans.talisman.CreeperTalisman;
import com.willfp.talismans.talismans.talismans.talisman.EndTalisman;
import com.willfp.talismans.talismans.talismans.talisman.ExperienceTalisman;
import com.willfp.talismans.talismans.talismans.talisman.ExtractionTalisman;
import com.willfp.talismans.talismans.talismans.talisman.FeatherTalisman;
import com.willfp.talismans.talismans.talismans.talisman.FlameTalisman;
import com.willfp.talismans.talismans.talismans.talisman.FluxTalisman;
import com.willfp.talismans.talismans.talismans.talisman.HealingTalisman;
import com.willfp.talismans.talismans.talismans.talisman.ImmunityTalisman;
import com.willfp.talismans.talismans.talismans.talisman.NetherTalisman;
import com.willfp.talismans.talismans.talismans.talisman.PoseidonTalisman;
import com.willfp.talismans.talismans.talismans.talisman.RaidTalisman;
import com.willfp.talismans.talismans.talismans.talisman.ResistanceTalisman;
import com.willfp.talismans.talismans.talismans.talisman.SharpnessTalisman;
import com.willfp.talismans.talismans.talismans.talisman.SkeletonTalisman;
import com.willfp.talismans.talismans.talismans.talisman.SpiderResistanceTalisman;
import com.willfp.talismans.talismans.talismans.talisman.SpiderTalisman;
import com.willfp.talismans.talismans.talismans.talisman.StrengthTalisman;
import com.willfp.talismans.talismans.talismans.talisman.ZombieResistanceTalisman;
import com.willfp.talismans.talismans.talismans.talisman.ZombieTalisman;
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
    public static final Talisman FEATHER_TALISMAN = new FeatherTalisman();
    public static final Talisman FLAME_TALISMAN = new FlameTalisman();
    public static final Talisman ARCHERY_TALISMAN = new ArcheryTalisman();
    public static final Talisman POSEIDON_TALISMAN = new PoseidonTalisman();
    public static final Talisman EXPERIENCE_TALISMAN = new ExperienceTalisman();
    public static final Talisman EXTRACTION_TALISMAN = new ExtractionTalisman();
    public static final Talisman SHARPNESS_TALISMAN = new SharpnessTalisman();
    public static final Talisman ZOMBIE_RESISTANCE_TALISMAN = new ZombieResistanceTalisman();
    public static final Talisman SPIDER_RESISTANCE_TALISMAN = new SpiderResistanceTalisman();
    public static final Talisman RESISTANCE_TALISMAN = new ResistanceTalisman();
    public static final Talisman STRENGTH_TALISMAN = new StrengthTalisman();
    public static final Talisman ARCHERY_RING = new ArcheryRing();
    public static final Talisman END_RING = new EndRing();
    public static final Talisman EXPERIENCE_RING = new ExperienceRing();
    public static final Talisman EXTRACTION_RING = new ExtractionRing();
    public static final Talisman FEATHER_RING = new FeatherRing();
    public static final Talisman FLAME_RING = new FlameRing();
    public static final Talisman NETHER_RING = new NetherRing();
    public static final Talisman POSEIDON_RING = new PoseidonRing();
    public static final Talisman RESISTANCE_RING = new ResistanceRing();
    public static final Talisman SHARPNESS_RING = new SharpnessRing();
    public static final Talisman STRENGTH_RING = new StrengthRing();
    public static final Talisman ARCHERY_RELIC = new ArcheryRelic();
    public static final Talisman END_RELIC = new EndRelic();
    public static final Talisman EXPERIENCE_RELIC = new ExperienceRelic();
    public static final Talisman EXTRACTION_RELIC = new ExtractionRelic();
    public static final Talisman FEATHER_RELIC = new FeatherRelic();
    public static final Talisman FLAME_RELIC = new FlameRelic();
    public static final Talisman NETHER_RELIC = new NetherRelic();
    public static final Talisman POSEIDON_RELIC = new PoseidonRelic();
    public static final Talisman RESISTANCE_RELIC = new ResistanceRelic();
    public static final Talisman SHARPNESS_RELIC = new SharpnessRelic();
    public static final Talisman STRENGTH_RELIC = new StrengthRelic();
    public static final Talisman RAID_RING = new RaidRing();
    public static final Talisman RAID_RELIC = new RaidRelic();
    public static final Talisman ALCHEMY_TALISMAN = new AlchemyTalisman();
    public static final Talisman ALCHEMY_RING = new AlchemyRing();
    public static final Talisman ALCHEMY_RELIC = new AlchemyRelic();
    public static final Talisman IMMUNITY_TALISMAN = new ImmunityTalisman();
    public static final Talisman FLUX_TALISMAN = new FluxTalisman();
    public static final Talisman FLUX_RING = new FluxRing();
    public static final Talisman FLUX_RELIC = new FluxRelic();
    public static final Talisman HEALING_TALISMAN = new HealingTalisman();
    public static final Talisman HEALING_RING = new HealingRing();
    public static final Talisman HEALING_RELIC = new HealingRelic();


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
