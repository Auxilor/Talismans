package com.willfp.talismans.display.packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import com.willfp.eco.util.protocollib.AbstractPacketAdapter;
import com.willfp.talismans.display.TalismanDisplay;
import org.jetbrains.annotations.NotNull;

public class PacketSetSlot extends AbstractPacketAdapter {
    /**
     * Instantiate a new listener for {@link PacketType.Play.Server#SET_SLOT}.
     *
     * @param plugin The plugin to listen through.
     */
    public PacketSetSlot(@NotNull final AbstractEcoPlugin plugin) {
        super(plugin, PacketType.Play.Server.SET_SLOT, false);
    }

    @Override
    public void onSend(@NotNull final PacketContainer packet) {
        packet.getItemModifier().modify(0, TalismanDisplay::displayTalisman);
    }
}
