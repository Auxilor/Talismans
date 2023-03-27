package com.willfp.talismans.talismans

import com.google.common.collect.ImmutableList
import com.willfp.eco.core.config.ConfigType
import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.core.config.readConfig
import com.willfp.eco.core.config.updating.ConfigUpdater
import com.willfp.eco.core.registry.Registry
import com.willfp.libreforge.loader.LibreforgePlugin
import com.willfp.libreforge.loader.configs.ConfigCategory
import com.willfp.libreforge.loader.configs.LegacyLocation
import com.willfp.talismans.TalismansPlugin
import java.io.File

object Talismans : ConfigCategory("talisman", "talismans") {
    private val registry = Registry<Talisman>()

    override val legacyLocation: LegacyLocation = LegacyLocation(
        "talismans", "talismans", emptyList()
    )

    /**
     * Get all registered [Talisman]s.
     *
     * @return A list of all [Talisman]s.
     */
    @JvmStatic
    fun values(): List<Talisman> {
        return ImmutableList.copyOf(registry.values())
    }

    /**
     * Get [Talisman] matching id.
     *
     * @param name The id to search for.
     * @return The matching [Talisman], or null if not found.
     */
    @JvmStatic
    fun getByID(name: String): Talisman? {
        return registry[name]
    }

    override fun clear(plugin: LibreforgePlugin) {
        registry.clear()
    }

    override fun acceptConfig(plugin: LibreforgePlugin, id: String, config: Config) {
        registry.register(Talisman(id, config, plugin as TalismansPlugin))
    }
}
