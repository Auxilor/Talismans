package com.willfp.talismans.talismans;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableSet;
import com.willfp.eco.core.config.ConfigUpdater;
import com.willfp.talismans.talismans.talismans.AlchemyTalisman;
import com.willfp.talismans.talismans.talismans.ArcheryTalisman;
import com.willfp.talismans.talismans.talismans.AttackSpeedTalisman;
import com.willfp.talismans.talismans.talismans.BossTalisman;
import com.willfp.talismans.talismans.talismans.CreeperTalisman;
import com.willfp.talismans.talismans.talismans.DayCrystal;
import com.willfp.talismans.talismans.talismans.EndTalisman;
import com.willfp.talismans.talismans.talismans.ExperienceTalisman;
import com.willfp.talismans.talismans.talismans.ExtractionTalisman;
import com.willfp.talismans.talismans.talismans.FeatherTalisman;
import com.willfp.talismans.talismans.talismans.FlameTalisman;
import com.willfp.talismans.talismans.talismans.FluxTalisman;
import com.willfp.talismans.talismans.talismans.GravityTalisman;
import com.willfp.talismans.talismans.talismans.GroundingCharm;
import com.willfp.talismans.talismans.talismans.HealingTalisman;
import com.willfp.talismans.talismans.talismans.ImmunityTalisman;
import com.willfp.talismans.talismans.talismans.NecromanceRing;
import com.willfp.talismans.talismans.talismans.NetherTalisman;
import com.willfp.talismans.talismans.talismans.NightCrystal;
import com.willfp.talismans.talismans.talismans.PoseidonTalisman;
import com.willfp.talismans.talismans.talismans.RaidTalisman;
import com.willfp.talismans.talismans.talismans.ResistanceTalisman;
import com.willfp.talismans.talismans.talismans.SharpnessTalisman;
import com.willfp.talismans.talismans.talismans.SkeletonTalisman;
import com.willfp.talismans.talismans.talismans.SpeedTalisman;
import com.willfp.talismans.talismans.talismans.SpiderResistanceTalisman;
import com.willfp.talismans.talismans.talismans.SpiderTalisman;
import com.willfp.talismans.talismans.talismans.StormRing;
import com.willfp.talismans.talismans.talismans.StrengthTalisman;
import com.willfp.talismans.talismans.talismans.VibranceTalisman;
import com.willfp.talismans.talismans.talismans.ZombieResistanceTalisman;
import com.willfp.talismans.talismans.talismans.ZombieTalisman;
import com.willfp.talismans.talismans.util.TalismanUtils;
import lombok.experimental.UtilityClass;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

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
    public static final Talisman ALCHEMY_TALISMAN = new AlchemyTalisman();
    public static final Talisman IMMUNITY_TALISMAN = new ImmunityTalisman();
    public static final Talisman FLUX_TALISMAN = new FluxTalisman();
    public static final Talisman HEALING_TALISMAN = new HealingTalisman();
    public static final Talisman SPEED_TALISMAN = new SpeedTalisman();
    public static final Talisman BOSS_TALISMAN = new BossTalisman();
    public static final Talisman GRAVITY_TALISMAN = new GravityTalisman();
    public static final Talisman ATTACK_SPEED_TALISMAN = new AttackSpeedTalisman();
    public static final Talisman DAY_CRYSTAL = new DayCrystal();
    public static final Talisman NIGHT_CRYSTAL = new NightCrystal();
    public static final Talisman NECROMANCE_RING = new NecromanceRing();
    public static final Talisman GROUNDING_CHARM = new GroundingCharm();
    public static final Talisman STORM_RING = new StormRing();
    public static final Talisman VIBRANCE_TALISMAN = new VibranceTalisman();

    /**
     * Get all registered {@link Talisman}s.
     *
     * @return A list of all {@link Talisman}s.
     */
    public static Set<Talisman> values() {
        return ImmutableSet.copyOf(BY_KEY.values());
    }

    /**
     * Get {@link NamespacedKey}s for all registered {@link Talisman}s.
     *
     * @return A list of all {@link Talisman}s.
     */
    public static Set<NamespacedKey> keySet() {
        return ImmutableSet.copyOf(BY_KEY.keySet());
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
