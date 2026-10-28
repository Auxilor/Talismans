package com.willfp.talismans.talismans

import com.google.common.collect.ImmutableList
import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.core.registry.Registry
import com.willfp.eco.core.version.Version
import com.willfp.libreforge.loader.LibreforgePlugin
import com.willfp.libreforge.loader.configs.ConfigCategory
import com.willfp.libreforge.loader.configs.LegacyLocation
import com.willfp.talismans.talismans.util.TalismanUtils
import java.util.concurrent.CopyOnWriteArrayList

object Talismans : ConfigCategory("talisman", "talismans") {
    private val registry = Registry<Talisman>()

    private val fromTableCutoff = Version("2026.44")

    private val fromTableTalismans = CopyOnWriteArrayList<String>()

    @Volatile
    private var fromTableDisabled = false

    @Volatile
    private var lastFromTableWarning: Pair<List<String>, Boolean>? = null

    override val legacyLocation = LegacyLocation(
        "talismans.yml",
        "talismans"
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
        TalismanUtils.clearTalismanMaterials()
        fromTableTalismans.clear()
        fromTableDisabled = plugin.configYml.getBool("opt-in.disable-fromtable-permission") ||
            Version(plugin.description.version).isAtLeast(fromTableCutoff)
    }

    internal fun fromTablePermission(id: String): String? {
        fromTableTalismans += id
        return if (fromTableDisabled) null else "talismans.fromtable.$id"
    }

    override fun acceptConfig(plugin: LibreforgePlugin, id: String, config: Config) {
        registry.register(Talisman(id, config))
    }

    override fun afterReload(plugin: LibreforgePlugin) {
        val fromTableWarning = fromTableTalismans.toList() to fromTableDisabled
        if (fromTableTalismans.isNotEmpty() && fromTableWarning != lastFromTableWarning) {
            plugin.logger.warning(
                "${fromTableTalismans.size} craftable talisman(s) have no 'crafting-permission' set: " +
                    fromTableTalismans.joinToString(", ")
            )
            if (fromTableDisabled) {
                plugin.logger.warning(
                    "The talismans.fromtable.<id> permission no longer applies, so anyone can craft these. " +
                        "It will be removed in 2026.45."
                )
            } else {
                plugin.logger.warning(
                    "They still use talismans.fromtable.<id>, which stops working in 2026.44 " +
                        "and is removed in 2026.45. " +
                        "Enable 'opt-in.disable-fromtable-permission' to apply the change early."
                )
            }
            plugin.logger.warning("Set 'crafting-permission' on each one, or set it to [] for no permission.")
        }
        lastFromTableWarning = fromTableWarning

        for (talisman in registry.values()) {
            val visited = mutableSetOf(talisman)
            var lowerLevel = talisman.lowerLevel
            while (lowerLevel != null && visited.add(lowerLevel)) {
                lowerLevel = lowerLevel.lowerLevel
            }
            if (lowerLevel == talisman) {
                plugin.logger.warning(
                    "Talisman '${talisman.getID()}' is in a higherLevelOf cycle " +
                        "(${visited.joinToString(", ") { it.getID() }}). Fix higherLevelOf in its config."
                )
            }
        }
    }
}
