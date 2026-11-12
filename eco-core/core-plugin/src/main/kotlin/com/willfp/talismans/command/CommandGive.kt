package com.willfp.talismans.command

import com.willfp.talismans.talismans.Talismans
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import wiki.ecohub.core.command.CommandBuilder
import wiki.ecohub.game.drop.DropQueue
import wiki.ecohub.game.entity.runOwned
import wiki.ecohub.ui.text.formatEco

private val amountCompletions = listOf("1", "2", "3", "4", "5", "10", "32", "64")

private const val AMOUNT_ARGS = 3

/**
 * Gives on the receiver's region, as the console runs off it on Folia.
 */
internal fun CommandBuilder<CommandSender>.giveCommand() = subcommand(
    name = "give",
    permission = "talismans.command.give"
) {
    executes {
        val receiverName = argument(0, "needs-player")
        val talismanName = argument(1, "needs-talisman")
        val amount = args.getOrNull(2)?.toIntOrNull() ?: 1

        if (amount < 1) {
            fail("invalid-amount")
        }

        val receiver = Bukkit.getPlayer(receiverName) ?: fail("invalid-player")
        val talisman = Talismans.getByID(talismanName) ?: fail("invalid-talisman")

        receiver.runOwned {
            sender.sendMessage(
                plugin.langYml.getMessage("give-success")
                    .replace(oldValue = "%talisman%", newValue = talisman.name.formatEco())
                    .replace(oldValue = "%recipient%", newValue = receiver.name)
            )

            val itemStack = talisman.itemStack
            itemStack.amount = amount

            DropQueue(receiver)
                .addItem(itemStack)
                .forceTelekinesis()
                .push()
        }
    }

    completes {
        when (args.size) {
            1 -> playerNames()
            2 -> Talismans.values().map { it.id.key }
            AMOUNT_ARGS -> amountCompletions
            else -> emptyList()
        }
    }
}
