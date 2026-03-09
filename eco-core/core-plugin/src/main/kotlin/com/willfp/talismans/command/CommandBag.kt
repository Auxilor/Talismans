package com.willfp.talismans.command

import com.willfp.eco.core.command.impl.Subcommand
import com.willfp.talismans.bag.TalismanBag
import com.willfp.talismans.plugin
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object CommandBag : Subcommand(
    plugin,
    "bag",
    "talismans.command.bag",
    true
) {
    override fun onExecute(sender: CommandSender, args: List<String>) {
        TalismanBag.open(sender as Player)
    }
}
