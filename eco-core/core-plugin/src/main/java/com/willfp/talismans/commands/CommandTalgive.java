package com.willfp.talismans.commands;

import com.willfp.eco.util.command.AbstractCommand;
import com.willfp.eco.util.command.AbstractTabCompleter;
import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import com.willfp.talismans.talismans.Talisman;
import com.willfp.talismans.talismans.Talismans;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CommandTalgive extends AbstractCommand {
    /**
     * Instantiate a new /talgive command handler.
     *
     * @param plugin The plugin for the commands to listen for.
     */
    public CommandTalgive(@NotNull final AbstractEcoPlugin plugin) {
        super(plugin, "talgive", "talismans.give", false);
    }

    @Override
    public @Nullable AbstractTabCompleter getTab() {
        return new TabcompleterTalgive();
    }

    @Override
    public void onExecute(@NotNull final CommandSender sender,
                          @NotNull final List<String> args) {
        if (args.isEmpty()) {
            sender.sendMessage(this.getPlugin().getLangYml().getMessage("needs-player"));
            return;
        }

        if (args.size() == 1) {
            sender.sendMessage(this.getPlugin().getLangYml().getMessage("needs-talisman"));
            return;
        }

        String recieverName = args.get(0);
        Player reciever = Bukkit.getPlayer(recieverName);

        if (reciever == null) {
            sender.sendMessage(this.getPlugin().getLangYml().getMessage("invalid-player"));
            return;
        }

        String talismanName = args.get(1);
        Talisman talisman = Talismans.getByKey(this.getPlugin().getNamespacedKeyFactory().create(talismanName));
        if (talisman == null || !talisman.isEnabled()) {
            sender.sendMessage(this.getPlugin().getLangYml().getMessage("invalid-talisman"));
            return;
        }

        String message = this.getPlugin().getLangYml().getMessage("give-success");
        message = message.replace("%talisman%", talisman.getFormattedName()).replace("%recipient%", reciever.getName());
        sender.sendMessage(message);
        reciever.getInventory().addItem(talisman.getItemStack());
    }
}
