package com.willfp.talismans.libreforge

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.util.containsIgnoreCase
import com.willfp.libreforge.Dispatcher
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.ProvidedHolder
import com.willfp.libreforge.arguments
import com.willfp.libreforge.conditions.Condition
import com.willfp.libreforge.get
import com.willfp.talismans.talismans.Talisman
import com.willfp.talismans.talismans.util.TalismanChecks
import org.bukkit.entity.Player

object ConditionHasTalisman : Condition<NoCompileData>("has_talisman") {
    override val arguments = arguments {
        require("talisman", "You must specify the talisman!")
    }

    override fun isMet(
        dispatcher: Dispatcher<*>,
        config: Config,
        holder: ProvidedHolder,
        compileData: NoCompileData
    ): Boolean {
        val player = dispatcher.get<Player>() ?: return false

        return TalismanChecks.getTalismansOnPlayer(player)
            .map { it.holder }
            .filterIsInstance<Talisman>()
            .map { it.id.key }
            .containsIgnoreCase(config.getString("talisman"))
    }
}
