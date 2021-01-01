package com.willfp.talismans.commands;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.willfp.eco.util.command.AbstractCommand;
import com.willfp.eco.util.drops.internal.DropManager;
import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import com.willfp.eco.util.proxy.ProxyConstants;
import com.willfp.talismans.talismans.Talismans;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class CommandTaldebug extends AbstractCommand {
    /**
     * Instantiate a new /taldebug command handler.
     *
     * @param plugin The plugin for the commands to listen for.
     */
    public CommandTaldebug(@NotNull final AbstractEcoPlugin plugin) {
        super(plugin, "taldebug", "talismans.taldebug", false);
    }

    @Override
    public void onExecute(@NotNull final CommandSender sender,
                          @NotNull final List<String> args) {
        this.getPlugin().getLog().info("--------------- BEGIN DEBUG ----------------");
        if (sender instanceof Player) {
            Player player = (Player) sender;
            player.sendMessage("Held Item: " + player.getInventory().getItemInMainHand().toString());
            this.getPlugin().getLog().info("");

            this.getPlugin().getLog().info("Held Item: " + player.getInventory().getItemInMainHand().toString());
            this.getPlugin().getLog().info("");
        }

        this.getPlugin().getLog().info("Running Version: " + this.getPlugin().getDescription().getVersion());
        this.getPlugin().getLog().info("");

        this.getPlugin().getLog().info("Loaded Extensions: " + this.getPlugin().getExtensionLoader().getLoadedExtensions().stream()
                .map(extension -> extension.getName() + " v" + extension.getVersion())
                .collect(Collectors.joining()));
        this.getPlugin().getLog().info("");

        this.getPlugin().getLog().info("Talismans.values(): " + Talismans.values().toString());
        this.getPlugin().getLog().info("");

        this.getPlugin().getLog().info("Installed Plugins: " + Arrays.stream(Bukkit.getPluginManager().getPlugins()).map(Plugin::getName).collect(Collectors.toList()).toString());
        this.getPlugin().getLog().info("");

        this.getPlugin().getLog().info("Drop Type: " + DropManager.getType());
        this.getPlugin().getLog().info("");

        this.getPlugin().getLog().info("Packets: " + ProtocolLibrary.getProtocolManager().getPacketListeners().stream()
                .filter(packetListener -> packetListener.getSendingWhitelist().getPriority().equals(ListenerPriority.MONITOR))
                .collect(Collectors.toList()).toString());
        this.getPlugin().getLog().info("");

        this.getPlugin().getLog().info("Server Information: ");
        this.getPlugin().getLog().info("Players Online: " + Bukkit.getServer().getOnlinePlayers().size());
        this.getPlugin().getLog().info("Bukkit IP: " + Bukkit.getIp());
        this.getPlugin().getLog().info("Running Version: " + Bukkit.getVersion()
                + ", Bukkit Version: " + Bukkit.getBukkitVersion()
                + ", Alt Version: " + Bukkit.getServer().getVersion()
                + ", NMS: " + ProxyConstants.NMS_VERSION);
        this.getPlugin().getLog().info("Motd: " + Bukkit.getServer().getMotd());
        this.getPlugin().getLog().info("--------------- END DEBUG ----------------");
    }
}
