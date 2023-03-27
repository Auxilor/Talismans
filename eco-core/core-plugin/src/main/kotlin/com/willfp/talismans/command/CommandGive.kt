package com.willfp.talismans.command

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.command.impl.Subcommand
import com.willfp.talismans.talismans.Talismans
import com.willfp.talismans.talismans.Talismans.getByID
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.util.StringUtil

class CommandGive(plugin: EcoPlugin) : Subcommand(plugin, "give", "talismans.command.give", false) {
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

    override fun onExecute(sender: CommandSender, args: List<String>) {
        if (args.isEmpty()) {
            sender.sendMessage(plugin.langYml.getMessage("needs-player"))
            return
        }

        if (args.size == 1) {
            sender.sendMessage(plugin.langYml.getMessage("needs-talisman"))
            return
        }

        var amount = 1
        if (args.size > 2) {
            amount = args[2].toIntOrNull() ?: 1
        }

        val receiverName = args[0]
        val receiver = Bukkit.getPlayer(receiverName)

        if (receiver == null) {
            sender.sendMessage(plugin.langYml.getMessage("invalid-player"))
            return
        }

        val talismanName = args[1]
        val talisman = getByID(talismanName)

        if (talisman == null) {
            sender.sendMessage(plugin.langYml.getMessage("invalid-talisman"))
            return
        }

        var message = plugin.langYml.getMessage("give-success")

        message = message.replace("%talisman%", talisman.name).replace("%recipient%", receiver.name)

        sender.sendMessage(message)

        val itemStack = talisman.itemStack

        itemStack.amount = amount

        receiver.inventory.addItem(itemStack)
    }

    override fun tabComplete(sender: CommandSender, args: List<String>): List<String> {
        val completions = mutableListOf<String>()

        if (args.isEmpty()) {
            return Talismans.values().map { it.id.key }
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
                Talismans.values().map { it.id.key },
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

        return completions
    }
}
