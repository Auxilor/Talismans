package com.willfp.talismans

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
}
