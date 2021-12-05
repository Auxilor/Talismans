package com.willfp.talismans.command

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.command.CommandHandler
import com.willfp.eco.core.command.impl.Subcommand

class CommandReload(plugin: EcoPlugin)
    : Subcommand(plugin, "reload", "talismans.commands.reload", false) {
    override fun getHandler(): CommandHandler {
        return CommandHandler { sender, _ ->
            sender.sendMessage(
                plugin.langYml.getMessage("reloaded")
                    .replace("%time%", plugin.reloadWithTime().toString() + "")
            )
        }
    }
}