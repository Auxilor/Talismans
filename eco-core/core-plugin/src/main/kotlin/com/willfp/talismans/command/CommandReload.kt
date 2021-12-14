package com.willfp.talismans.command

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.command.CommandHandler
import com.willfp.eco.core.command.impl.Subcommand
import org.bukkit.command.CommandSender

class CommandReload(plugin: EcoPlugin)
    : Subcommand(plugin, "reload", "talismans.commands.reload", false) {
    override fun onExecute(sender: CommandSender, args: List<String>) {
        sender.sendMessage(
            plugin.langYml.getMessage("reloaded")
                .replace("%time%", plugin.reloadWithTime().toString() + "")
        )
    }
}