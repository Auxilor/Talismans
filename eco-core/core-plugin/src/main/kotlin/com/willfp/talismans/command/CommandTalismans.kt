package com.willfp.talismans.command

import com.willfp.talismans.TalismansPlugin
import wiki.ecohub.core.command.command

internal fun TalismansPlugin.talismansCommand() = command(
    name = "talismans",
    permission = "talismans.command.talismans"
) {
    aliases("talis", "tal", "talismen", "talisman")

    reloadCommand()
    giveCommand()
    bagCommand()
    bagSlotsCommand()
}
