package com.willfp.talismans.commands;

import com.willfp.eco.util.command.AbstractCommand;
import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import com.willfp.talismans.data.inventory.TalismanInventoryUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CommandTalismans extends AbstractCommand {
    /**
     * Instantiate a new /talismans command handler.
     *
     * @param plugin The plugin for the commands to listen for.
     */
    public CommandTalismans(@NotNull final AbstractEcoPlugin plugin) {
        super(plugin, "talismans", "talismans.inventory", true);
    }

    @Override
    public void onExecute(@NotNull final CommandSender sender,
                          @NotNull final List<String> args) {
        Player player = (Player) sender;
        TalismanInventoryUtils.showInventory(player);
    }
}
