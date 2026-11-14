package com.willfp.talismans.command

import com.willfp.talismans.bag.BagSlotAction
import com.willfp.talismans.bag.TalismanBag
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import wiki.ecohub.core.command.CommandBuilder

private val slotCompletions = listOf("1", "2", "5", "10")

private const val AMOUNT_ARGS = 3

internal fun CommandBuilder<CommandSender>.bagSlotsCommand() = subcommand(
    name = "bagslots",
    permission = "talismans.command.bagslots"
) {
    executes {
        val action = args.getOrNull(0)?.let { BagSlotAction.fromArg(it) } ?: fail("needs-action")
        val targetName = argument(1, "needs-player")

        @Suppress("DEPRECATION")
        val target = Bukkit.getOfflinePlayer(targetName).takeIf { it.isOnline || it.hasPlayedBefore() }
            ?: fail("invalid-player")

        val amount = args.getOrNull(2)?.toIntOrNull()?.takeIf { it >= 0 } ?: fail("invalid-amount")

        val slots = action.apply(current = TalismanBag.getBoughtSlots(target), amount = amount)
        TalismanBag.setBoughtSlots(target, slots)

        reply("bagslots-success", "player" to (target.name ?: targetName), "slots" to slots)
    }

    completes {
        when (args.size) {
            1 -> BagSlotAction.entries.map { it.name.lowercase() }
            2 -> playerNames()
            AMOUNT_ARGS -> slotCompletions
            else -> emptyList()
        }
    }
}
