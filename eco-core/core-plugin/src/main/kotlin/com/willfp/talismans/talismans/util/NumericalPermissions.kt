package com.willfp.talismans.talismans.util

import org.bukkit.entity.Player
import wiki.ecohub.core.platform.NumericalPermissions

/**
 * The highest number on a granted permission starting with [prefix], or [default].
 */
internal fun Player.getNumericalPermission(prefix: String, default: Double): Double =
    NumericalPermissions.highest(
        effectivePermissions.filter { it.value }.map { it.permission },
        prefix,
        default
    )
