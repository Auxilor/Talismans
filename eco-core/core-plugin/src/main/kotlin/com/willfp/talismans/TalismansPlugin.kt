package com.willfp.talismans

import com.willfp.talismans.bag.TalismanBag
import com.willfp.talismans.command.talismansCommand
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
import wiki.ecohub.config.category.NativeConfigCategory
import wiki.ecohub.core.command.EcoCommand
import wiki.ecohub.core.plugin.EcoPlugin
import wiki.ecohub.game.item.Items
import wiki.ecohub.provider.metrics.EcoMetricsChart
import wiki.ecohub.rules.condition.Conditions
import wiki.ecohub.rules.holder.registerSpecificHolderProvider
import wiki.ecohub.rules.holder.registerSpecificRefreshFunction
import wiki.ecohub.rules.holder.slot.SlotTypes
import wiki.ecohub.ui.display.DisplayModule

internal lateinit var plugin: TalismansPlugin
    private set

class TalismansPlugin : EcoPlugin() {
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

        TalismanChecks.registerItemStackProvider { player ->
            if (configYml.getBool("read-enderchest") && !configYml.getBool("offhand-only"))
                player.enderChest.contents.filterNotNull()
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

    override fun loadConfigCategories(): List<NativeConfigCategory> {
        return listOf(
            Talismans
        )
    }

    override fun loadPluginCommands(): List<EcoCommand> {
        return listOf(
            talismansCommand()
        )
    }

    override fun loadListeners(): List<Listener> {
        return listOf(
            BlockPlaceListener,
            DiscoverRecipeListener,
            TalismanBag
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
