package com.willfp.talismans.libreforge

import com.willfp.libreforge.slot.SlotType
import com.willfp.talismans.talismans.util.TalismanChecks
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

object SlotTypeTalisman: SlotType("talisman") {
    override fun getItems(entity: LivingEntity): List<ItemStack> {
        return if (entity is Player) {
            TalismanChecks.getTalismanItemsOnPlayer(entity).toList()
        } else {
            emptyList()
        }
    }

    override fun addToSlot(player: Player, item: ItemStack): Boolean {
        return false
    }

    override fun getItemSlots(player: Player): List<Int> {
        return emptyList()
    }
}
