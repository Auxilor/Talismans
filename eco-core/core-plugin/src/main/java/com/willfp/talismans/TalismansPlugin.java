package com.willfp.talismans;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.command.impl.PluginCommand;
import com.willfp.eco.core.display.DisplayModule;
import com.willfp.eco.core.items.CustomItem;
import com.willfp.talismans.command.CommandTalismans;
import com.willfp.talismans.display.TalismanDisplay;
import com.willfp.talismans.talismans.Talismans;
import com.willfp.talismans.talismans.util.BlockPlaceListener;
import com.willfp.talismans.talismans.util.DiscoverRecipeListener;
import com.willfp.talismans.talismans.util.TalismanChecks;
import com.willfp.talismans.talismans.util.TalismanCraftListener;
import com.willfp.talismans.talismans.util.WatcherTriggers;
import com.willfp.talismans.talismans.util.equipevent.SyncTalismanEquipEventTask;
import com.willfp.talismans.talismans.util.equipevent.TalismanEquipEventListeners;
import lombok.Getter;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")
public class TalismansPlugin extends EcoPlugin {
    /**
     * Instance of the plugin.
     */
    @Getter
    private static TalismansPlugin instance;

    /**
     * Internal constructor called by bukkit on plugin load.
     */
    public TalismansPlugin() {
        super(611, 9865, "&6", true);
        instance = this;
    }

    @Override
    protected void handleEnable() {
        this.getLogger().info(Talismans.values().size() + " Talismans Loaded");
        new CustomItem(this.getNamespacedKeyFactory().create("any_talisman"), test -> TalismanChecks.getTalismanOnItem(test) != null, Talismans.SHARPNESS_TALISMAN.getLevel(1).getItemStack()).register();
    }

    @Override
    protected void handleReload() {
        Talismans.values().forEach(talisman -> {
            HandlerList.unregisterAll(talisman);

            this.getScheduler().runLater(() -> {
                if (talisman.isEnabled()) {
                    this.getEventManager().registerListener(talisman);
                }
            }, 1);
        });
        SyncTalismanEquipEventTask.scheduleAutocheck(this);
    }

    @Override
    protected List<PluginCommand> loadPluginCommands() {
        return Arrays.asList(
                new CommandTalismans(this)
        );
    }

    @Override
    protected List<Listener> loadListeners() {
        return Arrays.asList(
                new WatcherTriggers(this),
                new BlockPlaceListener(),
                new TalismanCraftListener(this),
                new TalismanEquipEventListeners(this),
                new DiscoverRecipeListener(this)
        );
    }

    @Override
    @Nullable
    protected DisplayModule createDisplayModule() {
        return new TalismanDisplay(this);
    }
}
