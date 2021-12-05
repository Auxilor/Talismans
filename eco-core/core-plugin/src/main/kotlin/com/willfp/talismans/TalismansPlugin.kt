package com.willfp.talismans

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.command.impl.PluginCommand
import com.willfp.eco.core.display.DisplayModule
import com.willfp.eco.core.items.CustomItem
import com.willfp.libreforge.LibReforge
import com.willfp.talismans.command.CommandTalismans
import com.willfp.talismans.config.TalismansYml
import com.willfp.talismans.display.TalismanDisplay
import com.willfp.talismans.talismans.Talismans
import com.willfp.talismans.talismans.util.*
import org.bukkit.event.Listener

class TalismansPlugin : EcoPlugin(611, 9865, "&6", true) {
    val talismansYml = TalismansYml(this)

    /**
     * Internal constructor called by bukkit on plugin load.
     */
    init {
        instance = this
        LibReforge.init(this)
        LibReforge.registerHolderProvider { TalismanChecks.getTalismansOnPlayer(it) }
    }

    override fun handleEnable() {
        LibReforge.enable(this)
        CustomItem(
            this.namespacedKeyFactory.create("any_talisman"),
            { test -> TalismanChecks.getTalismanOnItem(test) != null },
            Talismans.values()[0].getLevel(1)!!.itemStack
        ).register()
    }

    override fun handleDisable() {
        LibReforge.disable(this)
    }

    override fun handleReload() {
        logger.info("${Talismans.values().size} Talismans Loaded")
        LibReforge.reload(this);
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

    companion object {
        @JvmStatic
        lateinit var instance: TalismansPlugin
    }
}