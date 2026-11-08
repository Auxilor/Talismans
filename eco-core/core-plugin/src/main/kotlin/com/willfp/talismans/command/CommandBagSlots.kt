package com.willfp.talismans.command

import com.willfp.eco.core.command.impl.Subcommand
import com.willfp.talismans.bag.BagSlotAction
import com.willfp.talismans.bag.TalismanBag
import com.willfp.talismans.plugin
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.util.StringUtil

object CommandBagSlots : Subcommand(
    plugin,
    "bagslots",
    "talismans.command.bagslots",
    false
) {
    private val numbers = listOf(
        "1",
        "2",
        "5",
        "10"
    )

    override fun onExecute(sender: CommandSender, args: List<String>) {
        val action = args.getOrNull(0)?.let { BagSlotAction.fromArg(it) }

        @Suppress("DEPRECATION")
        val target = args.getOrNull(1)?.let { name ->
            Bukkit.getOfflinePlayer(name).takeIf { it.isOnline || it.hasPlayedBefore() }
        }

        val amount = args.getOrNull(2)?.toIntOrNull()?.takeIf { it >= 0 }

        when {
            action == null -> {
                sender.sendMessage(plugin.langYml.getMessage("needs-action"))
            }

            args.size < 2 -> {
                sender.sendMessage(plugin.langYml.getMessage("needs-player"))
            }

            target == null -> {
                sender.sendMessage(plugin.langYml.getMessage("invalid-player"))
            }

            amount == null -> {
                sender.sendMessage(plugin.langYml.getMessage("invalid-amount"))
            }

            else -> {
                val slots = action.apply(current = TalismanBag.getBoughtSlots(target), amount = amount)
                TalismanBag.setBoughtSlots(target, slots)

                sender.sendMessage(
                    plugin.langYml.getMessage("bagslots-success")
                        .replace(oldValue = "%player%", newValue = target.name ?: args[1])
                        .replace(oldValue = "%slots%", newValue = slots.toString())
                )
            }
        }
    }

    override fun tabComplete(sender: CommandSender, args: List<String>): List<String> {
        val options = when (args.size) {
            0, 1 -> BagSlotAction.entries.map { it.name.lowercase() }
            2 -> Bukkit.getOnlinePlayers().map { it.name }
            3 -> numbers
            else -> return emptyList()
        }

        return StringUtil.copyPartialMatches(args.lastOrNull().orEmpty(), options, mutableListOf())
    }
}
