package com.willfp.talismans

import com.willfp.eco.core.command.impl.PluginCommand
import com.willfp.eco.core.display.DisplayModule
import com.willfp.libreforge.LibReforgePlugin
import com.willfp.talismans.bag.TalismanBag
import com.willfp.talismans.command.CommandTalismans
import com.willfp.talismans.display.TalismanDisplay
import com.willfp.talismans.talismans.Talismans
import com.willfp.talismans.talismans.util.BlockPlaceListener
import com.willfp.talismans.talismans.util.DiscoverRecipeListener
import com.willfp.talismans.talismans.util.TalismanChecks
import com.willfp.talismans.talismans.util.TalismanEnableListeners
import org.bukkit.event.Listener

class TalismansPlugin : LibReforgePlugin() {
    init {
        instance = this

        TalismanChecks.registerItemStackProvider {
            TalismanBag.getTalismans(it)
        }

        registerHolderProvider { TalismanChecks.getTalismansOnPlayer(it) }
    }

    override fun handleEnableAdditional() {
        this.copyConfigs("talismans")
    }

    override fun handleReloadAdditional() {
        logger.info("${Talismans.values().size} Talismans Loaded")
    }

    override fun loadPluginCommands(): List<PluginCommand> {
        return listOf(
            CommandTalismans(this)
        )
    }

    override fun loadListeners(): List<Listener> {
        return listOf(
            BlockPlaceListener(),
            TalismanEnableListeners(this),
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