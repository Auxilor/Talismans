package com.willfp.talismans.command

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.command.impl.PluginCommand
import org.bukkit.command.CommandSender

class CommandTalismans(plugin: EcoPlugin)
    : PluginCommand(plugin, "talismans", "talismans.command.talismans", false) {
    init {
        addSubcommand(CommandReload(plugin))
            .addSubcommand(CommandGive(plugin))
    }

    override fun onExecute(sender: CommandSender, args: List<String>) {
        sender.sendMessage(
            plugin.langYml.getMessage("invalid-command")
        )
    }
}