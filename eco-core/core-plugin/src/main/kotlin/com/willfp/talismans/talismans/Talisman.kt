package com.willfp.talismans.talismans

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.core.display.Display
import com.willfp.eco.core.items.CustomItem
import com.willfp.eco.core.items.Items
import com.willfp.eco.core.items.builder.ItemStackBuilder
import com.willfp.eco.core.recipe.Recipes
import com.willfp.eco.core.recipe.parts.EmptyTestableItem
import com.willfp.eco.core.recipe.recipes.CraftingRecipe
import com.willfp.libreforge.Holder
import com.willfp.libreforge.conditions.Conditions
import com.willfp.libreforge.effects.Effects
import com.willfp.talismans.TalismansPlugin
import com.willfp.talismans.talismans.util.TalismanChecks
import com.willfp.talismans.talismans.util.TalismanUtils
import org.apache.commons.lang.Validate
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import java.util.*

class Talisman(
    val config: Config,
    private val plugin: TalismansPlugin
) : Holder {
    override val id = config.getString("id")

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

    val recipe: CraftingRecipe? = run {
        if (craftable) {
            Recipes.createAndRegisterRecipe(
                plugin,
                key.key,
                itemStack,
                config.getStrings("recipe"),
                "talismans.fromtable.${key.key}"
            )
        } else null
    }

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