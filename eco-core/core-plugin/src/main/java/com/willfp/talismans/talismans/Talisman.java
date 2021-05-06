package com.willfp.talismans.talismans;

import com.google.common.collect.ImmutableSet;
import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.Prerequisite;
import com.willfp.talismans.TalismansPlugin;
import com.willfp.talismans.config.TalismanConfig;
import com.willfp.talismans.talismans.util.Watcher;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class Talisman implements Listener, Watcher {
    /**
     * Instance of Talismans for talismans to be able to access.
     */
    @Getter(AccessLevel.PROTECTED)
    private final EcoPlugin plugin = TalismansPlugin.getInstance();

    /**
     * The key to store talismans in meta.
     */
    @Getter
    private final NamespacedKey key;

    /**
     * The talisman's config.
     */
    @Getter
    private final TalismanConfig config;

    /**
     * The names of the worlds that this talisman is disabled in.
     */
    @Getter
    private final Set<String> disabledWorldNames = new HashSet<>();

    /**
     * The worlds that this talisman is disabled in.
     */
    @Getter
    private final List<World> disabledWorlds = new ArrayList<>();

    /**
     * If the talisman is enabled.
     */
    @Getter
    private boolean enabled;

    /**
     * All levels.
     */
    private final Map<Integer, TalismanLevel> levels = new HashMap<>();

    /**
     * Create a new Talisman.
     *
     * @param key           The key name of the talisman.
     * @param prerequisites Optional {@link Prerequisite}s that must be met.
     */
    protected Talisman(@NotNull final String key,
                       @NotNull final Prerequisite... prerequisites) {
        this.key = this.getPlugin().getNamespacedKeyFactory().create(key);
        this.config = new TalismanConfig(this.getKey().getKey(), this.getClass(), this.plugin);

        if (!Prerequisite.areMet(prerequisites)) {
            return;
        }

        Talismans.addNewTalisman(this);
        this.update();
    }

    /**
     * Update the talisman based off config values.
     * This can be overridden but may lead to unexpected behavior.
     */
    public void update() {
        config.update();
        disabledWorldNames.clear();
        disabledWorldNames.addAll(config.getStrings(Talismans.GENERAL_LOCATION + "disabled-in-worlds"));
        disabledWorlds.clear();

        this.levels.clear();
        for (String key : config.getSubsection("levels").getKeys(false)) {
            TalismanLevel level = new TalismanLevel(this, Integer.parseInt(key), config.getSubsection("levels." + key));
            this.levels.put(Integer.parseInt(key), level);
            level.update();
        }

        List<String> worldNames = Bukkit.getWorlds().stream().map(World::getName).map(String::toLowerCase).collect(Collectors.toList());
        List<String> disabledExistingWorldNames = disabledWorldNames.stream().filter(s -> worldNames.contains(s.toLowerCase())).collect(Collectors.toList());
        disabledWorlds.addAll(Bukkit.getWorlds().stream().filter(world -> disabledExistingWorldNames.contains(world.getName().toLowerCase())).collect(Collectors.toList()));
        enabled = config.getBool("enabled");

        postUpdate();
    }

    protected void postUpdate() {
        // Unused as some talismans may have postUpdate tasks, however most won't.
    }

    /**
     * Get level.
     *
     * @param level The level.
     * @return The level or null if not found.
     */
    public final TalismanLevel getLevel(final int level) {
        return levels.get(level);
    }

    /**
     * Get all level.
     *
     * @return The levels.
     */
    public final Set<TalismanLevel> getLevels() {
        return ImmutableSet.copyOf(levels.values());
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Talisman)) {
            return false;
        }

        Talisman talisman = (Talisman) o;
        return this.getKey().equals(talisman.getKey());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getKey());
    }

    @Override
    public String toString() {
        return "Talisman{"
                + this.getKey()
                + "}";
    }
}
