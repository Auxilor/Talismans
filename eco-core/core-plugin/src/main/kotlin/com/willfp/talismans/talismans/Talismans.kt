package com.willfp.talismans.talismans

import com.google.common.collect.ImmutableList
import com.willfp.talismans.talismans.util.TalismanUtils
import wiki.ecohub.config.Config
import wiki.ecohub.config.LegacyLocation
import wiki.ecohub.config.category.NativeConfigCategory
import wiki.ecohub.core.plugin.EcoPlugin
import wiki.ecohub.core.registry.Registry
import wiki.ecohub.modules.recipe.Recipes

object Talismans : NativeConfigCategory("talismans", "talisman") {
    private val registry = Registry<Talisman>()

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

    override fun clear(plugin: EcoPlugin) {
        for (talisman in registry.values()) {
            talisman.recipe?.let { Recipes.unregister(it) }
        }
        registry.clear()
        TalismanUtils.clearTalismanMaterials()
    }

    override fun acceptConfig(plugin: EcoPlugin, id: String, config: Config) {
        registry.register(Talisman(id, config))
    }

    override fun afterReload(plugin: EcoPlugin) {
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
