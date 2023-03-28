package com.willfp.talismans

import com.willfp.eco.core.command.impl.PluginCommand
import com.willfp.eco.core.display.DisplayModule
import com.willfp.libreforge.conditions.Conditions
import com.willfp.libreforge.loader.LibreforgePlugin
import com.willfp.libreforge.loader.configs.ConfigCategory
import com.willfp.libreforge.registerHolderProvider
import com.willfp.libreforge.registerPlayerRefreshFunction
import com.willfp.talismans.bag.TalismanBag
import com.willfp.talismans.command.CommandTalismans
import com.willfp.talismans.display.TalismanDisplay
import com.willfp.talismans.libreforge.ConditionHasTalisman
import com.willfp.talismans.talismans.Talismans
import com.willfp.talismans.talismans.util.BlockPlaceListener
import com.willfp.talismans.talismans.util.DiscoverRecipeListener
import com.willfp.talismans.talismans.util.TalismanChecks
import org.bukkit.event.Listener

class TalismansPlugin : LibreforgePlugin() {
    init {
        instance = this

        TalismanChecks.registerItemStackProvider {
            TalismanBag.getTalismans(it)
        }
    }

    override fun handleEnable() {
        Conditions.register(ConditionHasTalisman)

        registerHolderProvider { TalismanChecks.getTalismansOnPlayer(it) }
        registerPlayerRefreshFunction { TalismanChecks.clearCache(it) }
    }

    override fun loadConfigCategories(): List<ConfigCategory> {
        return listOf(
            Talismans
        )
    }

    override fun loadPluginCommands(): List<PluginCommand> {
        return listOf(
            CommandTalismans(this)
        )
    }

    override fun loadListeners(): List<Listener> {
        return listOf(
            BlockPlaceListener(),
            DiscoverRecipeListener(this)
        )
    }

    override fun createDisplayModule(): DisplayModule {
        return TalismanDisplay(this)
    }

    companion object {
        @JvmStatic
        lateinit var instance: TalismansPlugin
    }
}
