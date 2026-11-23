package com.willfp.talismans.command

import com.willfp.talismans.bag.TalismanBag
import org.bukkit.command.CommandSender
import wiki.ecohub.core.command.CommandBuilder

internal fun CommandBuilder<CommandSender>.bagCommand() = playerSubcommand(
    name = "bag",
    permission = "talismans.command.bag"
) {
    executes {
        TalismanBag.open(sender)
    }
}
