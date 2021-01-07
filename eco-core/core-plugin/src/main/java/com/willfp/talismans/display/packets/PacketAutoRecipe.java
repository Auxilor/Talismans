package com.willfp.talismans.display.packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.willfp.eco.util.ProxyUtils;
import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import com.willfp.eco.util.protocollib.AbstractPacketAdapter;
import com.willfp.talismans.proxy.proxies.AutoCraftProxy;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;

public class PacketAutoRecipe extends AbstractPacketAdapter {
    /**
     * Instantiate a new listener for {@link PacketType.Play.Server#SET_SLOT}.
     *
     * @param plugin The plugin to listen through.
     */
    public PacketAutoRecipe(@NotNull final AbstractEcoPlugin plugin) {
        super(plugin, PacketType.Play.Server.AUTO_RECIPE, false);
    }

    @Override
    public void onSend(@NotNull final PacketContainer packet) {
        if (!packet.getMinecraftKeys().getValues().get(0).getFullKey().split(":")[0].equals("talismans")) {
            return;
        }

        if (packet.getMinecraftKeys().getValues().get(0).getFullKey().split(":")[1].contains("displayed")) {
            return;
        }

        try {
            ProxyUtils.getProxy(AutoCraftProxy.class).modifyPacket(packet.getHandle());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        PacketContainer newAutoRecipe = new PacketContainer(PacketType.Play.Server.AUTO_RECIPE);
        newAutoRecipe.getMinecraftKeys().write(0, packet.getMinecraftKeys().read(0));

        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(Bukkit.getServer().getPlayer("Auxilor"), newAutoRecipe);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
