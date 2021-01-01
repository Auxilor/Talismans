package com.willfp.talismans.commands;

import com.willfp.eco.util.command.AbstractCommand;
import com.willfp.eco.util.config.Configs;
import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CommandTalreload extends AbstractCommand {
    /**
     * Instantiate a new /talreload command handler.
     *
     * @param plugin The plugin for the commands to listen for.
     */
    public CommandTalreload(@NotNull final AbstractEcoPlugin plugin) {
        super(plugin, "talreload", "talismans.reload", false);
    }

    @Override
    public void onExecute(@NotNull final CommandSender sender,
                          @NotNull final List<String> args) {
        this.getPlugin().reload();
        sender.sendMessage(Configs.LANG.getMessage("reloaded"));
    }
}
