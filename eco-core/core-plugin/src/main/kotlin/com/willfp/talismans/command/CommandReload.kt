package com.willfp.talismans.command

import com.willfp.talismans.talismans.Talismans
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import wiki.ecohub.core.command.CommandBuilder
import wiki.ecohub.core.platform.runGlobal
import wiki.ecohub.core.stdlib.toNiceString
import wiki.ecohub.ui.text.StringUtils

/**
 * Reloading rebuilds shared registries, so it runs on the global region.
 */
internal fun CommandBuilder<CommandSender>.reloadCommand() = subcommand(
    name = "reload",
    permission = "talismans.command.reload"
) {
    executes {
        Bukkit.getServer().runGlobal {
            sender.sendMessage(
                plugin.langYml.getMessage("reloaded", StringUtils.FormatOption.WITHOUT_PLACEHOLDERS)
                    .replace(oldValue = "%time%", newValue = plugin.reloadWithTime().toNiceString())
                    .replace(oldValue = "%count%", newValue = Talismans.values().size.toNiceString())
            )
        }
    }
}
