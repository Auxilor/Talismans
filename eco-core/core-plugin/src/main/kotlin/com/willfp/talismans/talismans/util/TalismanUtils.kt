package com.willfp.talismans.talismans.util

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.fast.fast
import com.willfp.talismans.TalismansPlugin.Companion.instance
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

object TalismanUtils {
    private val TALISMAN_MATERIALS = mutableSetOf<Material>()
    private val PLUGIN: EcoPlugin = instance

    fun convert(itemStack: ItemStack?) {
        if (itemStack == null) {
            return
        }
        if (!isTalismanMaterial(itemStack.type)) {
            return
        }
        val container = itemStack.fast().persistentDataContainer
        val talismanKey = container.keys.firstOrNull { it.namespace == "talismans" } ?: return
        if (!container.has(talismanKey, PersistentDataType.INTEGER)) {
            return
        }
        val level = container.get(talismanKey, PersistentDataType.INTEGER) ?: return
        container.remove(talismanKey)
        container.set(
            PLUGIN.namespacedKeyFactory.create("talisman"),
            PersistentDataType.STRING,
            talismanKey.key + "_" + level
        )
    }

    fun getLimit(player: Player): Int {
        val prefix = "talismans.limit."
        var highest = -1
        for (permission in player.effectivePermissions.map { it.permission }) {
            if (!permission.startsWith(prefix)) {
                continue
            }

            val limit = permission.substring(permission.lastIndexOf(".") + 1).toInt()
            if (limit > highest) {
                highest = limit
            }
        }
        return if (highest < 0) {
            10000
        } else {
            highest
        }
    }

    fun isTalismanMaterial(material: Material): Boolean {
        return TALISMAN_MATERIALS.contains(material)
    }

    fun registerTalismanMaterial(material: Material) {
        TALISMAN_MATERIALS.add(material)
    }

    fun clearTalismanMaterials() {
        TALISMAN_MATERIALS.clear()
    }
}