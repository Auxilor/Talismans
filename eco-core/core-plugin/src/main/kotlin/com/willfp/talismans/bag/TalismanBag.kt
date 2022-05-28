package com.willfp.talismans.bag

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.config.updating.ConfigUpdater
import com.willfp.eco.core.data.keys.PersistentDataKey
import com.willfp.eco.core.data.keys.PersistentDataKeyType
import com.willfp.eco.core.data.profile
import com.willfp.eco.core.gui.menu
import com.willfp.eco.core.gui.menu.Menu
import com.willfp.eco.core.gui.slot
import com.willfp.eco.core.items.Items
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

object TalismanBag {
    private lateinit var menu: Menu
    private lateinit var key: PersistentDataKey<List<String>>

    @JvmStatic
    @ConfigUpdater
    fun update(plugin: EcoPlugin) {
        key = PersistentDataKey(
            plugin.namespacedKeyFactory.create("talisman_bag"),
            PersistentDataKeyType.STRING_LIST,
            emptyList()
        )

        menu = menu(6) {
            for (row in 1..6) {
                for (column in 1..9) {
                    setSlot(row, column,
                        slot { player, _ ->
                            val items = player.profile.read(key).map {
                                Items.lookup(it).item
                            }

                            val index = (row - 1) * 9 + column - 1

                            items.toList().getOrNull(index) ?: ItemStack(Material.AIR)
                        }
                    )
                }
            }

            onClose { event, menu ->
                val player = event.player as Player
                val items = menu.getCaptiveItems(player).map {
                    Items.toLookupString(it)
                }

                player.profile.write(key, items)
            }
        }
    }

    fun open(player: Player) {
        menu.open(player)
    }
}
