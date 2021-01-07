package com.willfp.talismans.talismans.util.equipevent;

import com.willfp.talismans.talismans.Talisman;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class TalismanEquipEvent extends PlayerEvent {
    /**
     * Handler list.
     */
    private static final HandlerList HANDLER_LIST = new HandlerList();

    /**
     * The talisman.
     */
    @Getter
    private final Talisman talisman;

    /**
     * If the event is from being equipped or unequipped.
     */
    @Getter
    private final EquipType type;

    /**
     * Create a new TalismanEquipEvent.
     *
     * @param who      The player.
     * @param talisman The talisman.
     * @param type     The equip type.
     */
    public TalismanEquipEvent(@NotNull final Player who,
                              @NotNull final Talisman talisman,
                              @NotNull final EquipType type) {
        super(who);
        this.talisman = talisman;
        this.type = type;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

}
