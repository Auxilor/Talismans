package com.willfp.talismans

import com.willfp.eco.core.bstats.EcoMetricsChart
import com.willfp.eco.core.command.impl.PluginCommand
import com.willfp.eco.core.display.DisplayModule
import com.willfp.eco.core.items.Items
import com.willfp.libreforge.conditions.Conditions
import com.willfp.libreforge.loader.LibreforgePlugin
import com.willfp.libreforge.loader.configs.ConfigCategory
import com.willfp.libreforge.registerSpecificHolderProvider
import com.willfp.libreforge.registerSpecificRefreshFunction
import com.willfp.libreforge.slot.SlotTypes
import com.willfp.talismans.bag.TalismanBag
import com.willfp.talismans.command.CommandTalismans
import com.willfp.talismans.display.TalismanDisplay
import com.willfp.talismans.libreforge.ConditionHasTalisman
import com.willfp.talismans.libreforge.SlotTypeTalisman
import com.willfp.talismans.talismans.TalismanTag
import com.willfp.talismans.talismans.Talismans
import com.willfp.talismans.talismans.util.BlockPlaceListener
import com.willfp.talismans.talismans.util.DiscoverRecipeListener
import com.willfp.talismans.talismans.util.TalismanChecks
import org.bukkit.entity.Player
import org.bukkit.event.Listener

internal lateinit var plugin: TalismansPlugin
    private set

class TalismansPlugin : LibreforgePlugin() {
    init {
        plugin = this

        TalismanChecks.registerItemStackProvider {
            TalismanBag.getTalismans(it)
        }

        TalismanChecks.registerItemStackProvider {
            if (configYml.getBool("read-inventory") && !configYml.getBool("offhand-only"))
                it.inventory.contents.filterNotNull()
            else emptyList()
        }

        TalismanChecks.registerItemStackProvider {
            if (configYml.getBool("read-enderchest") && !configYml.getBool("offhand-only"))
                @Suppress("UNNECESSARY_SAFE_CALL", "USELESS_ELVIS")
                it.enderChest?.contents?.filterNotNull() ?: emptyList()
            else emptyList()
        }
    }

    override fun handleLoad() {
        Items.registerTag(TalismanTag)
        Conditions.register(ConditionHasTalisman)
    }

    override fun handleEnable() {
        SlotTypes.register(SlotTypeTalisman)

        registerSpecificHolderProvider<Player> {
            TalismanChecks.getTalismansOnPlayer(it)
        }

        registerSpecificRefreshFunction<Player> {
            TalismanChecks.clearCache(it)
        }
    }

    override fun handleReload() {
        TalismanBag.update()
        TalismanChecks.reload()
    }

    override fun loadConfigCategories(): List<ConfigCategory> {
        return listOf(
            Talismans
        )
    }

    override fun loadPluginCommands(): List<PluginCommand> {
        return listOf(
            CommandTalismans
        )
    }

    override fun loadListeners(): List<Listener> {
        return listOf(
            BlockPlaceListener,
            DiscoverRecipeListener
        )
    }

    override fun loadDisplayModules(): List<DisplayModule> {
        return listOf(
            TalismanDisplay
        )
    }

    override fun getCustomCharts() = listOf(
        EcoMetricsChart.SingleLine("total_talismans") { Talismans.values().size },
        EcoMetricsChart.SimplePie("discover_recipes") {
            if (configYml.getBool("crafting.discover")) "enabled" else "disabled"
        },
        EcoMetricsChart.SimplePie("read_inventory") {
            if (configYml.getBool("read-inventory")) "enabled" else "disabled"
        },
        EcoMetricsChart.SimplePie("read_enderchest") {
            if (configYml.getBool("read-enderchest")) "enabled" else "disabled"
        },
        EcoMetricsChart.SimplePie("read_shulkerboxes") {
            if (configYml.getBool("read-shulkerboxes")) "enabled" else "disabled"
        },
        EcoMetricsChart.SimplePie("top_level_only") {
            if (configYml.getBool("top-level-only")) "enabled" else "disabled"
        },
        EcoMetricsChart.SimplePie("offhand_only") {
            if (configYml.getBool("offhand-only")) "enabled" else "disabled"
        }
    )
}
