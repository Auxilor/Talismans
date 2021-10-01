package com.willfp.talismans.command;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.command.CommandHandler;
import com.willfp.eco.core.command.impl.Subcommand;
import org.jetbrains.annotations.NotNull;

public class CommandReload extends Subcommand {
    /**
     * Instantiate a new command handler.
     *
     * @param plugin The plugin for the commands to listen for.
     */
    public CommandReload(@NotNull final EcoPlugin plugin) {
        super(plugin, "reload", "talismans.commands.reload", false);
    }

    @Override
    public CommandHandler getHandler() {
        return (sender, args) -> {
            sender.sendMessage(
                    this.getPlugin().getLangYml().getMessage("reloaded")
                            .replace("%time%", this.getPlugin().reloadWithTime() + "")
            );
        };
    }
}
