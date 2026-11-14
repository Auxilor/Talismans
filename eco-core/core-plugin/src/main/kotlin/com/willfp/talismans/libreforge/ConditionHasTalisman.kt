package com.willfp.talismans.libreforge

import com.willfp.talismans.talismans.Talisman
import com.willfp.talismans.talismans.util.TalismanChecks
import org.bukkit.entity.Player
import wiki.ecohub.config.ArgType
import wiki.ecohub.config.Config
import wiki.ecohub.config.arguments
import wiki.ecohub.core.registry.NoCompileData
import wiki.ecohub.rules.condition.Condition
import wiki.ecohub.rules.holder.Dispatcher
import wiki.ecohub.rules.holder.ProvidedHolder
import wiki.ecohub.rules.holder.get
import wiki.ecohub.ui.text.containsIgnoreCase

object ConditionHasTalisman : Condition<NoCompileData>("has_talisman") {
    override val description = "Passes when the player has the specified talisman."

    override val categories = setOf("inventory")

    override val arguments = arguments {
        require(
            "talisman",
            "You must specify the talisman!",
            description = "The id of the talisman to check for.",
            type = ArgType.STRING,
            example = "experience_1"
        )
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
