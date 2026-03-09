package com.willfp.talismans.command

import com.willfp.eco.core.command.impl.PluginCommand
import com.willfp.talismans.plugin
import org.bukkit.command.CommandSender

object CommandTalismans : PluginCommand(
    plugin,
    "talismans",
    "talismans.command.talismans",
    false
) {
    init {
        addSubcommand(CommandReload)
            .addSubcommand(CommandGive)
            .addSubcommand(CommandBag)
    }

    override fun onExecute(sender: CommandSender, args: List<String>) {
        sender.sendMessage(
            plugin.langYml.getMessage("invalid-command")
        )
    }

    override fun getAliases(): List<String> {
        return listOf("talis", "tal", "talismen", "talisman")
    }
}
