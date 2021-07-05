package com.willfp.talismans.command;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.command.CommandHandler;
import com.willfp.eco.core.command.TabCompleteHandler;
import com.willfp.eco.core.command.impl.Subcommand;
import com.willfp.eco.core.config.ConfigUpdater;
import com.willfp.talismans.TalismansPlugin;
import com.willfp.talismans.talismans.Talisman;
import com.willfp.talismans.talismans.TalismanLevel;
import com.willfp.talismans.talismans.Talismans;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CommandGive extends Subcommand {
    /**
     * The cached enchantment names.
     */
    private static final List<String> TALISMAN_NAMES = new ArrayList<>();

    /**
     * The cached numbers.
     */
    private static final List<String> NUMBERS = Arrays.asList(
            "1",
            "2",
            "3",
            "4",
            "5",
            "10",
            "32",
            "64"
    );

    /**
     * Instantiate a new command handler.
     *
     * @param plugin The plugin for the commands to listen for.
     */
    public CommandGive(@NotNull final EcoPlugin plugin) {
        super(plugin, "give", "talismans.command.give", false);
    }

    /**
     * Called on reload.
     */
    @ConfigUpdater
    public static void reload() {
        TALISMAN_NAMES.clear();
        TALISMAN_NAMES.addAll(Talismans.values().stream().filter(Talisman::isEnabled).map(talisman -> talisman.getKey().getKey()).collect(Collectors.toList()));
    }

    @Override
    public CommandHandler getHandler() {
        return (sender, args) -> {
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
        };
    }

    @Override
    public TabCompleteHandler getTabCompleter() {
        return (sender, args) -> {
            List<String> completions = new ArrayList<>();

            if (args.isEmpty()) {
                // Currently, this case is not ever reached
                return TALISMAN_NAMES;
            }

            if (args.size() == 1) {
                StringUtil.copyPartialMatches(args.get(0), Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()), completions);
                return completions;
            }

            if (args.size() == 2) {
                StringUtil.copyPartialMatches(args.get(1), TALISMAN_NAMES, completions);

                Collections.sort(completions);
                return completions;
            }

            if (args.size() == 3) {
                Talisman talisman = Talismans.getByKey(TalismansPlugin.getInstance().getNamespacedKeyFactory().create(args.get(1).toLowerCase()));
                if (talisman == null) {
                    return new ArrayList<>();
                }

                List<String> levels = talisman.getLevels().stream().map(TalismanLevel::getLevel).map(String::valueOf).collect(Collectors.toList());

                StringUtil.copyPartialMatches(args.get(2), levels, completions);

                completions.sort((s1, s2) -> {
                    int t1 = Integer.parseInt(s1);
                    int t2 = Integer.parseInt(s2);
                    return t1 - t2;
                });

                return completions;
            }

            if (args.size() == 4) {
                StringUtil.copyPartialMatches(args.get(3), NUMBERS, completions);

                completions.sort((s1, s2) -> {
                    int t1 = Integer.parseInt(s1);
                    int t2 = Integer.parseInt(s2);
                    return t1 - t2;
                });

                return completions;
            }

            return new ArrayList<>(0);
        };
    }
}
