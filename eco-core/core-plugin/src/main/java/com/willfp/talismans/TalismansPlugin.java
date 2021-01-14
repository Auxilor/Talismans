package com.willfp.talismans;

import com.willfp.eco.util.command.AbstractCommand;
import com.willfp.eco.util.integrations.IntegrationLoader;
import com.willfp.eco.util.interfaces.EcoRunnable;
import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import com.willfp.eco.util.protocollib.AbstractPacketAdapter;
import com.willfp.talismans.commands.CommandTaldebug;
import com.willfp.talismans.commands.CommandTalgive;
import com.willfp.talismans.commands.CommandTalreload;
import com.willfp.talismans.commands.TabcompleterTalgive;
import com.willfp.talismans.config.TalismansConfigs;
import com.willfp.talismans.display.packets.PacketAutoRecipe;
import com.willfp.talismans.display.packets.PacketChat;
import com.willfp.talismans.display.packets.PacketOpenWindowMerchant;
import com.willfp.talismans.display.packets.PacketSetCreativeSlot;
import com.willfp.talismans.display.packets.PacketSetSlot;
import com.willfp.talismans.display.packets.PacketWindowItems;
import com.willfp.talismans.integrations.mcmmo.McmmoManager;
import com.willfp.talismans.integrations.mcmmo.plugins.McmmoIntegrationImpl;
import com.willfp.talismans.talismans.Talismans;
import com.willfp.talismans.talismans.util.BlockPlaceListener;
import com.willfp.talismans.talismans.util.TalismanChecks;
import com.willfp.talismans.talismans.util.TalismanCraftListener;
import com.willfp.talismans.talismans.util.WatcherTriggers;
import com.willfp.talismans.talismans.util.equipevent.SyncTalismanEquipEventTask;
import com.willfp.talismans.talismans.util.equipevent.TalismanEquipEventListeners;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")
public class TalismansPlugin extends AbstractEcoPlugin {
    /**
     * Internal constructor called by bukkit on plugin load.
     */
    public TalismansPlugin() {
        super("Talismans", 87377, 9865, "com.willfp.talismans.proxy", "&6");
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

                    if (talisman instanceof EcoRunnable) {
                        this.getScheduler().syncRepeating((EcoRunnable) talisman, 5, ((EcoRunnable) talisman).getTime());
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
        return Arrays.asList(
                new IntegrationLoader("mcMMO", () -> McmmoManager.registerIntegration(new McmmoIntegrationImpl()))
        );
    }

    /**
     * Talismans-specific commands.
     *
     * @return A list of all commands.
     */
    @Override
    public List<AbstractCommand> getCommands() {
        return Arrays.asList(
                new CommandTaldebug(this),
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
        return Arrays.asList(
                new PacketChat(this),
                new PacketOpenWindowMerchant(this),
                new PacketSetCreativeSlot(this),
                new PacketSetSlot(this),
                new PacketWindowItems(this),
                new PacketAutoRecipe(this)
        );
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
                new TalismanCraftListener(),
                new TalismanEquipEventListeners(this)
        );
    }

    @Override
    public List<Class<?>> getUpdatableClasses() {
        return Arrays.asList(
                TalismansConfigs.class,
                TalismanChecks.class,
                TalismanChecks.class,
                Talismans.class,
                TabcompleterTalgive.class
        );
    }
}
