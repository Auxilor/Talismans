package com.willfp.talismans.command

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.command.impl.Subcommand
import com.willfp.talismans.bag.TalismanBag
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class CommandBag(plugin: EcoPlugin) : Subcommand(plugin, "bag", "talismans.commands.bag", true) {
    override fun onExecute(sender: CommandSender, args: List<String>) {
        TalismanBag.open(sender as Player)
    }
}
