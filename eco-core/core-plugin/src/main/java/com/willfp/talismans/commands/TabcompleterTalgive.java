package com.willfp.talismans.commands;

import com.willfp.eco.util.command.AbstractTabCompleter;
import com.willfp.eco.util.config.updating.annotations.ConfigUpdater;
import com.willfp.talismans.talismans.Talisman;
import com.willfp.talismans.talismans.Talismans;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TabcompleterTalgive extends AbstractTabCompleter {
    /**
     * The cached enchantment names.
     */
    private static final List<String> TALISMAN_NAMES = Talismans.values().stream().filter(Talisman::isEnabled).map(talisman -> talisman.getKey().getKey()).collect(Collectors.toList());

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
     * Instantiate a new tab-completer for /talgive.
     *
     * @param command Instance of /talgive.
     */
    public TabcompleterTalgive(@NotNull final CommandTalgive command) {
        super(command);
    }

    /**
     * Called on /ecoreload.
     */
    @ConfigUpdater
    public static void reload() {
        TALISMAN_NAMES.clear();
        TALISMAN_NAMES.addAll(Talismans.values().stream().filter(Talisman::isEnabled).map(talisman -> talisman.getKey().getKey()).collect(Collectors.toList()));
    }

    /**
     * The execution of the tabcompleter.
     *
     * @param sender The sender of the command.
     * @param args   The arguments of the command.
     * @return A list of tab-completions.
     */
    @Override
    public List<String> onTab(@NotNull final CommandSender sender,
                              @NotNull final List<String> args) {
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
            StringUtil.copyPartialMatches(args.get(2), NUMBERS, completions);

            completions.sort((s1, s2) -> {
                int t1 = Integer.parseInt(s1);
                int t2 = Integer.parseInt(s2);
                return t1 - t2;
            });

            return completions;
        }

        return new ArrayList<>(0);
    }
}
