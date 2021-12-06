package com.willfp.talismans.command

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.command.CommandHandler
import com.willfp.eco.core.command.TabCompleteHandler
import com.willfp.eco.core.command.impl.Subcommand
import com.willfp.talismans.talismans.Talismans
import com.willfp.talismans.talismans.Talismans.getByID
import org.bukkit.Bukkit
import org.bukkit.util.StringUtil

class CommandGive(plugin: EcoPlugin) : Subcommand(plugin, "reload", "talismans.commands.reload", false) {
    private val numbers = listOf(
        "1",
        "2",
        "3",
        "4",
        "5",
        "10",
        "32",
        "64"
    )

    override fun getHandler(): CommandHandler? {
        return CommandHandler { sender, args ->
            if (args.isEmpty()) {
                sender.sendMessage(plugin.langYml.getMessage("needs-player"))
                return@CommandHandler
            }

            if (args.size == 1) {
                sender.sendMessage(plugin.langYml.getMessage("needs-talisman"))
                return@CommandHandler
            }

            var amount = 1
            if (args.size > 2) {
                amount = args[2].toIntOrNull() ?: 1
            }

            val recieverName = args[0]
            val reciever = Bukkit.getPlayer(recieverName)

            if (reciever == null) {
                sender.sendMessage(plugin.langYml.getMessage("invalid-player"))
                return@CommandHandler
            }

            val talismanName = args[1]
            val talisman = getByID(talismanName)

            if (talisman == null) {
                sender.sendMessage(plugin.langYml.getMessage("invalid-talisman"))
                return@CommandHandler
            }

            var message = plugin.langYml.getMessage("give-success")

            message = message.replace("%talisman%", talisman.name).replace("%recipient%", reciever.name)

            sender.sendMessage(message)

            val itemStack = talisman.itemStack

            itemStack.amount = amount

            reciever.inventory.addItem(itemStack)
        }
    }

    override fun getTabCompleter(): TabCompleteHandler {
        return TabCompleteHandler { _, args ->
            val completions = mutableListOf<String>()

            if (args.isEmpty()) {
                return@TabCompleteHandler Talismans.values().map { it.id }
            }

            if (args.size == 1) {
                StringUtil.copyPartialMatches(
                    args[0],
                    Bukkit.getOnlinePlayers().map { it.name },
                    completions
                )
            }

            if (args.size == 2) {
                StringUtil.copyPartialMatches(
                    args[1],
                    Talismans.values().map { it.id },
                    completions
                )
            }



            if (args.size == 3) {
                StringUtil.copyPartialMatches(
                    args[2],
                    numbers,
                    completions
                )
            }

            completions
        }
    }
}