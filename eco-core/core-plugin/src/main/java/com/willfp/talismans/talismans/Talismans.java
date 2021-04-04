package com.willfp.talismans.talismans;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableList;
import com.willfp.eco.core.config.ConfigUpdater;
import com.willfp.talismans.talismans.meta.TalismanStrength;
import com.willfp.talismans.talismans.talismans.AlchemyTalisman;
import com.willfp.talismans.talismans.talismans.ArcheryTalisman;
import com.willfp.talismans.talismans.talismans.AttackSpeedTalisman;
import com.willfp.talismans.talismans.talismans.BossTalisman;
import com.willfp.talismans.talismans.talismans.CreeperTalisman;
import com.willfp.talismans.talismans.talismans.EndTalisman;
import com.willfp.talismans.talismans.talismans.ExperienceTalisman;
import com.willfp.talismans.talismans.talismans.ExtractionTalisman;
import com.willfp.talismans.talismans.talismans.FeatherTalisman;
import com.willfp.talismans.talismans.talismans.FlameTalisman;
import com.willfp.talismans.talismans.talismans.FluxTalisman;
import com.willfp.talismans.talismans.talismans.GravityTalisman;
import com.willfp.talismans.talismans.talismans.HealingTalisman;
import com.willfp.talismans.talismans.talismans.ImmunityTalisman;
import com.willfp.talismans.talismans.talismans.NetherTalisman;
import com.willfp.talismans.talismans.talismans.PoseidonTalisman;
import com.willfp.talismans.talismans.talismans.RaidTalisman;
import com.willfp.talismans.talismans.talismans.ResistanceTalisman;
import com.willfp.talismans.talismans.talismans.SharpnessTalisman;
import com.willfp.talismans.talismans.talismans.SkeletonTalisman;
import com.willfp.talismans.talismans.talismans.SpeedTalisman;
import com.willfp.talismans.talismans.talismans.SpiderResistanceTalisman;
import com.willfp.talismans.talismans.talismans.SpiderTalisman;
import com.willfp.talismans.talismans.talismans.StrengthTalisman;
import com.willfp.talismans.talismans.talismans.ZombieResistanceTalisman;
import com.willfp.talismans.talismans.talismans.ZombieTalisman;
import com.willfp.talismans.talismans.util.TalismanUtils;
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

    public static final Talisman ZOMBIE_TALISMAN = new ZombieTalisman(TalismanStrength.TALISMAN);
    public static final Talisman SKELETON_TALISMAN = new SkeletonTalisman(TalismanStrength.TALISMAN);
    public static final Talisman CREEPER_TALISMAN = new CreeperTalisman(TalismanStrength.TALISMAN);
    public static final Talisman SPIDER_TALISMAN = new SpiderTalisman(TalismanStrength.TALISMAN);
    public static final Talisman RAID_TALISMAN = new RaidTalisman(TalismanStrength.TALISMAN);
    public static final Talisman NETHER_TALISMAN = new NetherTalisman(TalismanStrength.TALISMAN);
    public static final Talisman END_TALISMAN = new EndTalisman(TalismanStrength.TALISMAN);
    public static final Talisman FEATHER_TALISMAN = new FeatherTalisman(TalismanStrength.TALISMAN);
    public static final Talisman FLAME_TALISMAN = new FlameTalisman(TalismanStrength.TALISMAN);
    public static final Talisman ARCHERY_TALISMAN = new ArcheryTalisman(TalismanStrength.TALISMAN);
    public static final Talisman POSEIDON_TALISMAN = new PoseidonTalisman(TalismanStrength.TALISMAN);
    public static final Talisman EXPERIENCE_TALISMAN = new ExperienceTalisman(TalismanStrength.TALISMAN);
    public static final Talisman EXTRACTION_TALISMAN = new ExtractionTalisman(TalismanStrength.TALISMAN);
    public static final Talisman SHARPNESS_TALISMAN = new SharpnessTalisman(TalismanStrength.TALISMAN);
    public static final Talisman ZOMBIE_RESISTANCE_TALISMAN = new ZombieResistanceTalisman(TalismanStrength.TALISMAN);
    public static final Talisman SPIDER_RESISTANCE_TALISMAN = new SpiderResistanceTalisman(TalismanStrength.TALISMAN);
    public static final Talisman RESISTANCE_TALISMAN = new ResistanceTalisman(TalismanStrength.TALISMAN);
    public static final Talisman STRENGTH_TALISMAN = new StrengthTalisman(TalismanStrength.TALISMAN);
    public static final Talisman ARCHERY_RING = new ArcheryTalisman(TalismanStrength.RING);
    public static final Talisman END_RING = new EndTalisman(TalismanStrength.RING);
    public static final Talisman EXPERIENCE_RING = new ExperienceTalisman(TalismanStrength.RING);
    public static final Talisman EXTRACTION_RING = new ExtractionTalisman(TalismanStrength.RING);
    public static final Talisman FEATHER_RING = new FeatherTalisman(TalismanStrength.RING);
    public static final Talisman FLAME_RING = new FlameTalisman(TalismanStrength.RING);
    public static final Talisman NETHER_RING = new NetherTalisman(TalismanStrength.RING);
    public static final Talisman POSEIDON_RING = new PoseidonTalisman(TalismanStrength.RING);
    public static final Talisman RESISTANCE_RING = new ResistanceTalisman(TalismanStrength.RING);
    public static final Talisman SHARPNESS_RING = new SharpnessTalisman(TalismanStrength.RING);
    public static final Talisman STRENGTH_RING = new StrengthTalisman(TalismanStrength.RING);
    public static final Talisman ARCHERY_RELIC = new ArcheryTalisman(TalismanStrength.RELIC);
    public static final Talisman END_RELIC = new EndTalisman(TalismanStrength.RELIC);
    public static final Talisman EXPERIENCE_RELIC = new ExperienceTalisman(TalismanStrength.RELIC);
    public static final Talisman EXTRACTION_RELIC = new ExtractionTalisman(TalismanStrength.RELIC);
    public static final Talisman FEATHER_RELIC = new FeatherTalisman(TalismanStrength.RELIC);
    public static final Talisman FLAME_RELIC = new FlameTalisman(TalismanStrength.RELIC);
    public static final Talisman NETHER_RELIC = new NetherTalisman(TalismanStrength.RELIC);
    public static final Talisman POSEIDON_RELIC = new PoseidonTalisman(TalismanStrength.RELIC);
    public static final Talisman RESISTANCE_RELIC = new ResistanceTalisman(TalismanStrength.RELIC);
    public static final Talisman SHARPNESS_RELIC = new SharpnessTalisman(TalismanStrength.RELIC);
    public static final Talisman STRENGTH_RELIC = new StrengthTalisman(TalismanStrength.RELIC);
    public static final Talisman RAID_RING = new RaidTalisman(TalismanStrength.RING);
    public static final Talisman RAID_RELIC = new RaidTalisman(TalismanStrength.RELIC);
    public static final Talisman ALCHEMY_TALISMAN = new AlchemyTalisman(TalismanStrength.TALISMAN);
    public static final Talisman ALCHEMY_RING = new AlchemyTalisman(TalismanStrength.RING);
    public static final Talisman ALCHEMY_RELIC = new AlchemyTalisman(TalismanStrength.RELIC);
    public static final Talisman IMMUNITY_TALISMAN = new ImmunityTalisman(TalismanStrength.TALISMAN);
    public static final Talisman FLUX_TALISMAN = new FluxTalisman(TalismanStrength.TALISMAN);
    public static final Talisman FLUX_RING = new FluxTalisman(TalismanStrength.RING);
    public static final Talisman FLUX_RELIC = new FluxTalisman(TalismanStrength.RELIC);
    public static final Talisman HEALING_TALISMAN = new HealingTalisman(TalismanStrength.TALISMAN);
    public static final Talisman HEALING_RING = new HealingTalisman(TalismanStrength.RING);
    public static final Talisman HEALING_RELIC = new HealingTalisman(TalismanStrength.RELIC);
    public static final Talisman SPEED_TALISMAN = new SpeedTalisman(TalismanStrength.TALISMAN);
    public static final Talisman SPEED_RING = new SpeedTalisman(TalismanStrength.RING);
    public static final Talisman SPEED_RELIC = new SpeedTalisman(TalismanStrength.RELIC);
    public static final Talisman BOSS_TALISMAN = new BossTalisman(TalismanStrength.TALISMAN);
    public static final Talisman BOSS_RING = new BossTalisman(TalismanStrength.RING);
    public static final Talisman BOSS_RELIC = new BossTalisman(TalismanStrength.RELIC);
    public static final Talisman GRAVITY_TALISMAN = new GravityTalisman(TalismanStrength.TALISMAN);
    public static final Talisman GRAVITY_RING = new GravityTalisman(TalismanStrength.RING);
    public static final Talisman GRAVITY_RELIC = new GravityTalisman(TalismanStrength.RELIC);
    public static final Talisman ATTACK_SPEED_TALISMAN = new AttackSpeedTalisman(TalismanStrength.TALISMAN);
    public static final Talisman ATTACK_SPEED_RING = new AttackSpeedTalisman(TalismanStrength.RING);
    public static final Talisman ATTACK_SPEEED_TALISMAN = new AttackSpeedTalisman(TalismanStrength.RELIC);

    /**
     * Get all registered {@link Talisman}s.
     *
     * @return A list of all {@link Talisman}s.
     */
    public static List<Talisman> values() {
        return ImmutableList.copyOf(BY_KEY.values());
    }

    /**
     * Get {@link NamespacedKey}s for all registered {@link Talisman}s.
     *
     * @return A list of all {@link Talisman}s.
     */
    public static List<NamespacedKey> keySet() {
        return ImmutableList.copyOf(BY_KEY.keySet());
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
        TalismanUtils.clearTalismanMaterials();
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
