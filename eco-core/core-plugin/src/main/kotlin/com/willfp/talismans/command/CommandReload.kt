package com.willfp.talismans.command

import com.willfp.eco.core.command.impl.Subcommand
import com.willfp.talismans.plugin
import com.willfp.talismans.talismans.Talismans
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import wiki.ecohub.core.plugin.startup.Prerequisite
import wiki.ecohub.core.stdlib.toNiceString
import wiki.ecohub.ui.text.StringUtils

object CommandReload : Subcommand(
    plugin,
    "reload",
    "talismans.command.reload",
    false
) {
    override fun onExecute(sender: CommandSender, args: List<String>) {
        // Reloading rebuilds shared registries, so it belongs on the global region.
        if (Prerequisite.HAS_FOLIA.isMet && !Bukkit.isGlobalTickThread()) {
            plugin.scheduler.global().run { onExecute(sender, args) }
            return
        }

        sender.sendMessage(
            plugin.langYml.getMessage("reloaded", StringUtils.FormatOption.WITHOUT_PLACEHOLDERS)
                .replace("%time%", plugin.reloadWithTime().toNiceString())
                .replace("%count%", Talismans.values().size.toNiceString())
        )
    }
}