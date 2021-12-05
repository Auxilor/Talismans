package com.willfp.talismans.talismans

import com.google.common.collect.BiMap
import com.google.common.collect.HashBiMap
import com.google.common.collect.ImmutableList
import com.willfp.eco.core.config.updating.ConfigUpdater
import com.willfp.talismans.TalismansPlugin

object Talismans {
    /**
     * Registered talismans.
     */
    private val BY_ID: BiMap<String, Talisman> = HashBiMap.create()

    /**
     * Get all registered [Talisman]s.
     *
     * @return A list of all [Talisman]s.
     */
    @JvmStatic
    fun values(): List<Talisman> {
        return ImmutableList.copyOf(BY_ID.values)
    }

    /**
     * Get [Talisman] matching id.
     *
     * @param name The id to search for.
     * @return The matching [Talisman], or null if not found.
     */
    @JvmStatic
    fun getByID(name: String): Talisman? {
        return BY_ID[name]
    }

    /**
     * Update all [Talisman]s.
     *
     * @param plugin Instance of Talismans.
     */
    @ConfigUpdater
    @JvmStatic
    fun update(plugin: TalismansPlugin) {
        for (talisman in values()) {
            removeTalisman(talisman)
        }
        for (setConfig in plugin.talismansYml.getSubsections("talismans")) {
            addNewTalisman(Talisman(setConfig, plugin))
        }
    }

    /**
     * Add new [Talisman] to Talismans.
     *
     * @param talisman The [Talisman] to add.
     */
    @JvmStatic
    fun addNewTalisman(talisman: Talisman) {
        BY_ID.remove(talisman.id)
        BY_ID[talisman.id] = talisman
    }

    /**
     * Remove [Talisman] from Talismans.
     *
     * @param talisman The [Talisman] to remove.
     */
    @JvmStatic
    fun removeTalisman(talisman: Talisman) {
        BY_ID.remove(talisman.id)
    }
}