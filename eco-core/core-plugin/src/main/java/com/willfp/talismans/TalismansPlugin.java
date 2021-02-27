package com.willfp.talismans;

import com.willfp.eco.util.bukkit.scheduling.TimedRunnable;
import com.willfp.eco.util.command.AbstractCommand;
import com.willfp.eco.util.display.DisplayModule;
import com.willfp.eco.util.integrations.IntegrationLoader;
import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import com.willfp.eco.util.protocollib.AbstractPacketAdapter;
import com.willfp.talismans.commands.CommandTalgive;
import com.willfp.talismans.commands.CommandTalreload;
import com.willfp.talismans.commands.TabcompleterTalgive;
import com.willfp.talismans.config.TalismansConfigs;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")
public class TalismansPlugin extends AbstractEcoPlugin {
    /**
     * Instance of the plugin.
     */
    @Getter
    private static TalismansPlugin instance;

    /**
     * Internal constructor called by bukkit on plugin load.
     */
    public TalismansPlugin() {
        super("Talismans", 87377, 9865, "com.willfp.talismans.proxy", "&6");
        instance = this;
    }

    /**
     * Code executed on plugin enable.
     */
    @Override
    public void enable() {
        this.getExtensionLoader().loadExtensions();

        if (this.getExtensionLoader().getLoadedExtensions().isEmpty()) {
            this.getLog().info("&cNo extensions found");
        } else {
            this.getLog().info("Extensions Loaded:");
            this.getExtensionLoader().getLoadedExtensions().forEach(extension -> this.getLog().info("- " + extension.getName() + " v" + extension.getVersion()));
        }

        this.getLog().info(Talismans.values().size() + " Talismans Loaded");
    }

    /**
     * Code executed on plugin disable.
     */
    @Override
    public void disable() {
        this.getExtensionLoader().unloadExtensions();
    }

    /**
     * Nothing is called on plugin load.
     */
    @Override
    public void load() {
        // Nothing needs to be called on load
    }

    /**
     * Code executed on reload.
     */
    @Override
    public void onReload() {
        Talismans.values().forEach(talisman -> {
            HandlerList.unregisterAll(talisman);

            this.getScheduler().runLater(() -> {
                if (talisman.isEnabled()) {
                    this.getEventManager().registerListener(talisman);

                    if (talisman instanceof TimedRunnable) {
                        this.getScheduler().syncRepeating((TimedRunnable) talisman, 5, ((TimedRunnable) talisman).getTime());
                    }
                }
            }, 1);
        });
        SyncTalismanEquipEventTask.scheduleAutocheck(this);
    }

    /**
     * Code executed after server is up.
     */
    @Override
    public void postLoad() {
        // Nothing needs to be called after load.
    }

    /**
     * Talismans-specific integrations.
     *
     * @return A list of all integrations.
     */
    @Override
    public List<IntegrationLoader> getIntegrationLoaders() {
        return new ArrayList<>();
    }

    /**
     * Talismans-specific commands.
     *
     * @return A list of all commands.
     */
    @Override
    public List<AbstractCommand> getCommands() {
        return Arrays.asList(
                new CommandTalreload(this),
                new CommandTalgive(this)
        );
    }

    /**
     * Packet Adapters for talisman display.
     *
     * @return A list of packet adapters.
     */
    @Override
    public List<AbstractPacketAdapter> getPacketAdapters() {
        return new ArrayList<>();
    }

    /**
     * Talismans-specific listeners.
     *
     * @return A list of all listeners.
     */
    @Override
    public List<Listener> getListeners() {
        return Arrays.asList(
                new WatcherTriggers(this),
                new BlockPlaceListener(),
                new TalismanCraftListener(this),
                new TalismanEquipEventListeners(this),
                new DiscoverRecipeListener(this)
        );
    }

    @Override
    public List<Class<?>> getUpdatableClasses() {
        return Arrays.asList(
                TalismansConfigs.class,
                TalismanChecks.class,
                Talismans.class,
                TabcompleterTalgive.class
        );
    }

    @Override
    @Nullable
    protected DisplayModule createDisplayModule() {
        return new TalismanDisplay(this);
    }
}
