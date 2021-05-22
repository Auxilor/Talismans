package com.willfp.talismans.commands;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.command.AbstractCommand;
import com.willfp.eco.core.command.AbstractTabCompleter;
import com.willfp.talismans.talismans.Talisman;
import com.willfp.talismans.talismans.TalismanLevel;
import com.willfp.talismans.talismans.Talismans;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CommandTalgive extends AbstractCommand {
    /**
     * Instantiate a new /talgive command handler.
     *
     * @param plugin The plugin for the commands to listen for.
     */
    public CommandTalgive(@NotNull final EcoPlugin plugin) {
        super(plugin, "talgive", "talismans.give", false);
    }

    @Override
    public @Nullable AbstractTabCompleter getTab() {
        return new TabcompleterTalgive(this);
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

        int level = 1;

        if (args.size() > 2) {
            try {
                level = Integer.parseInt(args.get(2));
            } catch (NumberFormatException ignored) {
                // do nothing
            }
        }

        int amount = 1;

        if (args.size() > 3) {
            try {
                amount = Integer.parseInt(args.get(3));
            } catch (NumberFormatException ignored) {
                // do nothing
            }
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

        TalismanLevel talismanLevel = talisman.getLevel(level);
        if (talismanLevel == null) {
            sender.sendMessage(this.getPlugin().getLangYml().getMessage("invalid-level"));
            return;
        }

        String message = this.getPlugin().getLangYml().getMessage("give-success");
        message = message.replace("%talisman%", talismanLevel.getName()).replace("%recipient%", reciever.getName());
        sender.sendMessage(message);

        ItemStack itemStack = talismanLevel.getItemStack();
        itemStack.setAmount(amount);
        reciever.getInventory().addItem(itemStack);
    }
}
