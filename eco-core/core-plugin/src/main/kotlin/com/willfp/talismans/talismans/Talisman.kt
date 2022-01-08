package com.willfp.talismans.talismans

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.core.display.Display
import com.willfp.eco.core.items.CustomItem
import com.willfp.eco.core.items.Items
import com.willfp.eco.core.items.builder.ItemStackBuilder
import com.willfp.eco.core.recipe.parts.EmptyTestableItem
import com.willfp.eco.core.recipe.recipes.ShapedCraftingRecipe
import com.willfp.libreforge.Holder
import com.willfp.libreforge.conditions.Conditions
import com.willfp.libreforge.effects.Effects
import com.willfp.talismans.TalismansPlugin
import com.willfp.talismans.talismans.util.TalismanChecks
import com.willfp.talismans.talismans.util.TalismanUtils
import org.apache.commons.lang.Validate
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.permissions.Permission
import org.bukkit.permissions.PermissionDefault
import org.bukkit.persistence.PersistentDataType
import java.util.*

class Talisman(
    val config: Config,
    private val plugin: TalismansPlugin
) : Holder {
    val id = config.getString("id")

    val key: NamespacedKey = plugin.namespacedKeyFactory.create(id)

    val name = config.getFormattedString("name")

    val description = config.getFormattedStrings("description").map { Display.PREFIX + it }

    val itemStack: ItemStack = run {
        val item = Items.lookup(config.getString("item"))
        Validate.isTrue(item !is EmptyTestableItem, "Item specified in " + key.key + " is invalid!")
        TalismanUtils.registerTalismanMaterial(item.item.type)

        ItemStackBuilder(item.item)
            .setAmount(1)
            .setDisplayName(name)
            .addLoreLines(description)
            .writeMetaKey(plugin.namespacedKeyFactory.create("talisman"), PersistentDataType.STRING, id)
            .build()
    }

    val craftable = config.getBool("craftable")

    val recipe: ShapedCraftingRecipe? = run {
        if (craftable) {
            val builder = ShapedCraftingRecipe.builder(plugin, key.key)
                .setOutput(itemStack)
            val recipeStrings = config.getStrings("recipe")
            for (i in recipeStrings.indices) {
                builder.setRecipePart(i, Items.lookup(recipeStrings[i]))
            }
            builder.build()
        } else null
    }.apply { this?.register() }

    val customItem = CustomItem(
        key,
        { test -> TalismanChecks.getTalismanOnItem(test) == this },
        itemStack
    ).apply { register() }

    val lowerLevel: Talisman?
        get() = Talismans.getByID(config.getString("higherLevelOf"))

    override val effects = config.getSubsections("effects").mapNotNull {
        Effects.compile(it, "Talisman ID $id")
    }.toSet()

    override val conditions = config.getSubsections("conditions").mapNotNull {
        Conditions.compile(it, "Talisman ID $id")
    }.toSet()

    init {
        if (Bukkit.getPluginManager().getPermission("talismans.fromtable." + key.key) == null) {
            val permission = Permission(
                "talismans.fromtable." + key.key,
                "Allows getting " + key.key + " from a Crafting Table",
                PermissionDefault.TRUE
            )
            permission.addParent(
                Bukkit.getPluginManager().getPermission("talismans.fromtable.*")!!,
                true
            )
            Bukkit.getPluginManager().addPermission(permission)
        }
        Talismans.addNewTalisman(this)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other !is Talisman) {
            return false
        }
        return key == other.key
    }

    override fun hashCode(): Int {
        return Objects.hash(key)
    }

    override fun toString(): String {
        return ("Talisman{"
                + key
                + "}")
    }
}