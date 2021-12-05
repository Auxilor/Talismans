package com.willfp.talismans.command

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.command.CommandHandler
import com.willfp.eco.core.command.impl.PluginCommand

class CommandTalismans(plugin: EcoPlugin)
    : PluginCommand(plugin, "talismans", "talismans.command.talismans", false) {
    init {
        addSubcommand(CommandReload(plugin))
            .addSubcommand(CommandGive(plugin))
    }

    override fun getHandler(): CommandHandler {
        return CommandHandler { sender, _ ->
            sender.sendMessage(
                plugin.langYml.getMessage("invalid-command")
            )
        }
    }
}