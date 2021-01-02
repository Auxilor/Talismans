package com.willfp.talismans.data;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.willfp.talismans.config.TalismansConfigs;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class PlayerData {
    /**
     * All currently loaded data.
     */
    private static final BiMap<UUID, PlayerData> PLAYER_DATA = HashBiMap.create();

    /**
     * The UUID of the player that owns this data.
     */
    @Getter
    private final UUID uuid;

    /**
     * The player's talisman inventory.
     */
    @Getter
    @Setter
    private Map<Integer, ItemStack> inventory = new HashMap<>();

    private PlayerData(@NotNull final UUID uuid) {
        this.uuid = uuid;
    }

    /**
     * Save all player data to data.yml.
     */
    public static void save() {
        PLAYER_DATA.forEach((uuid, playerData) -> {
            TalismansConfigs.DATA.getConfig().set(uuid.toString() + ".inventory", null);
            playerData.inventory.forEach((integer, itemStack) -> {
                TalismansConfigs.DATA.getConfig().set(uuid.toString() + ".inventory." + integer, itemStack);
            });
        });

        try {
            TalismansConfigs.DATA.getConfig().save(TalismansConfigs.DATA.getFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get a player's data.
     *
     * @param player The player to get data for.
     * @return The data.
     */
    public static PlayerData get(@NotNull final OfflinePlayer player) {
        if (PLAYER_DATA.containsKey(player.getUniqueId())) {
            return PLAYER_DATA.get(player.getUniqueId());
        }

        PlayerData data = new PlayerData(player.getUniqueId());

        if (TalismansConfigs.DATA.getConfig().contains(player.getUniqueId().toString())) {
            TalismansConfigs.DATA.getConfig().getConfigurationSection(player.getUniqueId().toString() + ".inventory").getKeys(false).forEach(s -> {
                ItemStack inventoryStack = TalismansConfigs.DATA.getConfig().getItemStack(player.getUniqueId().toString() + ".inventory." + s);

                data.inventory.put(Integer.parseInt(s), inventoryStack);
            });

            return data;
        }

        PLAYER_DATA.put(player.getUniqueId(), new PlayerData(player.getUniqueId()));
        return get(player);
    }
}
