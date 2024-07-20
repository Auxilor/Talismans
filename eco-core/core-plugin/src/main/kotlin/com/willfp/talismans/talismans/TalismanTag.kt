package com.willfp.talismans.talismans

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.items.tag.CustomItemTag
import com.willfp.talismans.talismans.util.TalismanChecks
import org.bukkit.inventory.ItemStack

class TalismanTag(plugin: EcoPlugin): CustomItemTag(plugin.createNamespacedKey("talisman")) {
    override fun matches(p0: ItemStack): Boolean {
        return TalismanChecks.getTalismanOnItem(p0) != null
    }

    override fun getExampleItem(): ItemStack {
        return Talismans.values().random().itemStack
    }
}
