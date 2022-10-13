package com.willfp.talismans.command

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.command.impl.PluginCommand
import com.willfp.libreforge.LibReforgePlugin
import com.willfp.libreforge.lrcdb.CommandExport
import com.willfp.libreforge.lrcdb.CommandImport
import com.willfp.libreforge.lrcdb.ExportableConfig
import com.willfp.talismans.talismans.Talismans
import org.bukkit.command.CommandSender

class CommandTalismans(plugin: LibReforgePlugin)
    : PluginCommand(plugin, "talismans", "talismans.command.talismans", false) {
    init {
        addSubcommand(CommandReload(plugin))
            .addSubcommand(CommandGive(plugin))
            .addSubcommand(CommandBag(plugin))
            .addSubcommand(CommandImport("talismans", plugin))
            .addSubcommand(CommandExport(plugin) {
                Talismans.values().map {
                    ExportableConfig(
                        it.id,
                        it.config
                    )
                }
            })
    }

    override fun onExecute(sender: CommandSender, args: List<String>) {
        sender.sendMessage(
            plugin.langYml.getMessage("invalid-command")
        )
    }
}
