package com.willfp.talismans.command;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.command.CommandHandler;
import com.willfp.eco.core.command.impl.PluginCommand;
import org.jetbrains.annotations.NotNull;

public class CommandTalismans extends PluginCommand {
    /**
     * Instantiate a new command handler.
     *
     * @param plugin The plugin for the commands to listen for.
     */
    public CommandTalismans(@NotNull final EcoPlugin plugin) {
        super(plugin, "talismans", "talismans.command.talismans", false);

        this.addSubcommand(new CommandReload(plugin))
        .addSubcommand(new CommandGive(plugin));
    }

    @Override
    public CommandHandler getHandler() {
        return (sender, args) -> {

        };
    }
}
