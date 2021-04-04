package com.willfp.talismans.commands;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.command.AbstractCommand;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CommandTalreload extends AbstractCommand {
    /**
     * Instantiate a new /talreload command handler.
     *
     * @param plugin The plugin for the commands to listen for.
     */
    public CommandTalreload(@NotNull final EcoPlugin plugin) {
        super(plugin, "talreload", "talismans.reload", false);
    }

    @Override
    public void onExecute(@NotNull final CommandSender sender,
                          @NotNull final List<String> args) {
        this.getPlugin().reload();
        this.getPlugin().reload(); // Aids
        sender.sendMessage(this.getPlugin().getLangYml().getMessage("reloaded"));
    }
}
