package com.willfp.talismans

import com.willfp.eco.core.command.impl.PluginCommand
import com.willfp.eco.core.display.DisplayModule
import com.willfp.eco.core.items.CustomItem
import com.willfp.libreforge.LibReforgePlugin
import com.willfp.libreforge.chains.EffectChains
import com.willfp.talismans.command.CommandTalismans
import com.willfp.talismans.config.TalismansYml
import com.willfp.talismans.display.TalismanDisplay
import com.willfp.talismans.talismans.Talismans
import com.willfp.talismans.talismans.util.BlockPlaceListener
import com.willfp.talismans.talismans.util.DiscoverRecipeListener
import com.willfp.talismans.talismans.util.TalismanChecks
import com.willfp.talismans.talismans.util.TalismanCraftListener
import com.willfp.talismans.talismans.util.TalismanEnableListeners
import org.bukkit.event.Listener

class TalismansPlugin : LibReforgePlugin(611, 9865, "&6") {
    val talismansYml = TalismansYml(this)

    /**
     * Internal constructor called by bukkit on plugin load.
     */
    init {
        instance = this
        registerHolderProvider { TalismanChecks.getTalismansOnPlayer(it) }
    }

    override fun handleEnableAdditional() {
        talismansYml.getSubsections("chains").mapNotNull {
            EffectChains.compile(it, "Chains")
        }
    }

    override fun handleReloadAdditional() {
        logger.info("${Talismans.values().size} Talismans Loaded")
        CustomItem(
            this.namespacedKeyFactory.create("any_talisman"),
            { test -> TalismanChecks.getTalismanOnItem(test) != null },
            Talismans.values()[0].itemStack
        ).register()
    }

    override fun loadPluginCommands(): List<PluginCommand> {
        return listOf(
            CommandTalismans(this)
        )
    }

    override fun loadListeners(): List<Listener> {
        return listOf(
            BlockPlaceListener(),
            TalismanCraftListener(),
            TalismanEnableListeners(this),
            DiscoverRecipeListener(this)
        )
    }

    override fun createDisplayModule(): DisplayModule {
        return TalismanDisplay(this)
    }

    override fun getMinimumEcoVersion(): String {
        return "6.19.0"
    }

    companion object {
        @JvmStatic
        lateinit var instance: TalismansPlugin
    }
}