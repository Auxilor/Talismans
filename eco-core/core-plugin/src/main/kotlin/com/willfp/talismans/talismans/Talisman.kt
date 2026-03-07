package com.willfp.talismans.talismans

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.core.display.Display
import com.willfp.eco.core.items.CustomItem
import com.willfp.eco.core.items.Items
import com.willfp.eco.core.items.builder.ItemStackBuilder
import com.willfp.eco.core.recipe.Recipes
import com.willfp.eco.core.recipe.parts.EmptyTestableItem
import com.willfp.eco.core.recipe.recipes.CraftingRecipe
import com.willfp.eco.core.registry.Registrable
import com.willfp.eco.util.formatEco
import com.willfp.libreforge.Holder
import com.willfp.libreforge.ViolationContext
import com.willfp.libreforge.conditions.Conditions
import com.willfp.libreforge.effects.Effects
import com.willfp.talismans.plugin
import com.willfp.talismans.talismans.util.TalismanChecks
import com.willfp.talismans.talismans.util.TalismanUtils
import org.apache.commons.lang3.Validate
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import java.util.Objects

class Talisman(
    id: String,
    val config: Config
) : Holder, Registrable {
    override val id: NamespacedKey = plugin.namespacedKeyFactory.create(id)

    val name = config.getString("name")

    val description = config.getStrings("description")

    private val _itemStack: ItemStack = run {
        val item = Items.lookup(config.getString("item"))
        Validate.isTrue(item !is EmptyTestableItem, "Item specified in $id is invalid!")
        TalismanUtils.registerTalismanMaterial(item.item.type)

        ItemStackBuilder(item.item)
            .setAmount(1)
            .setDisplayName(name.formatEco())
            .addLoreLines(description.map { Display.PREFIX + it.formatEco() })
            .writeMetaKey(plugin.namespacedKeyFactory.create("talisman"), PersistentDataType.STRING, id)
            .build()
    }

    val itemStack: ItemStack
        get() = _itemStack.clone()

    val recipe: CraftingRecipe? = config.getBool("craftable")
        .takeIf { it }
        ?.let {
            Recipes.createAndRegisterRecipe(
                plugin,
                id,
                itemStack,
                config.getStrings("recipe"),
                config.getStringOrNull("crafting-permission") ?: "talismans.fromtable.$id",
                config.getBool("shapeless")
            )
        }

    val customItem = CustomItem(
        this.id,
        { test -> TalismanChecks.getTalismanOnItem(test) == this },
        itemStack
    ).apply { register() }

    val lowerLevel: Talisman?
        get() = Talismans.getByID(config.getString("higherLevelOf"))

    override val effects = Effects.compile(
        config.getSubsections("effects"),
        ViolationContext(plugin, "Talisman $id")
    )

    override val conditions = Conditions.compile(
        config.getSubsections("conditions"),
        ViolationContext(plugin, "Talisman $id")
    )

    override fun getID(): String {
        return this.id.key
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other !is Talisman) {
            return false
        }
        return this.id == other.id
    }

    override fun hashCode(): Int {
        return Objects.hash(this.id)
    }

    override fun toString(): String {
        return ("Talisman{"
                + id
                + "}")
    }
}
