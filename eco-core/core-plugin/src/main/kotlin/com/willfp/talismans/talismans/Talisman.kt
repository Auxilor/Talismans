package com.willfp.talismans.talismans

import com.google.common.collect.ImmutableSet
import com.willfp.eco.core.config.interfaces.Config
import com.willfp.talismans.TalismansPlugin
import org.bukkit.NamespacedKey
import java.util.*

class Talisman(
    private val config: Config,
    private val plugin: TalismansPlugin
) {
    val id = config.getString("id")

    val key: NamespacedKey = plugin.namespacedKeyFactory.create(id)

    private val levels: MutableMap<Int, TalismanLevel> = HashMap()

    init {
        Talismans.addNewTalisman(this)
        update()
    }

    private fun update() {
        levels.clear()
        var i = 1
        for (config in config.getSubsections("levels")) {
            val level = TalismanLevel(this, i, config, plugin)
            levels[i] = level
            i++
        }
    }

    fun getLevel(level: Int): TalismanLevel? {
        return levels[level]
    }

    fun getLevels(): Set<TalismanLevel> {
        return ImmutableSet.copyOf(levels.values)
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o !is Talisman) {
            return false
        }
        return key == o.key
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