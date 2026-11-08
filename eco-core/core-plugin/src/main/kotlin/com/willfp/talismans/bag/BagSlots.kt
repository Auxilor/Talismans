package com.willfp.talismans.bag

const val UNLIMITED_BAG_SIZE = 10_000

const val MAX_BAG_ROWS = 6

fun effectiveBagSize(base: Int, bought: Int): Int {
    if (base >= UNLIMITED_BAG_SIZE) {
        return base
    }

    return (base + bought.coerceAtLeast(0)).coerceAtMost(UNLIMITED_BAG_SIZE)
}

fun upgradeCap(maxSize: Int, displayableSlots: Int): Int = minOf(a = maxSize, b = displayableSlots)

fun bagRows(size: Int, minimumRows: Int, slotCounts: Map<Int, Int>): Int =
    (minimumRows.coerceIn(minimumValue = 1, maximumValue = MAX_BAG_ROWS)..MAX_BAG_ROWS)
        .firstOrNull { slotCounts[it] ?: 0 >= size }
        ?: MAX_BAG_ROWS

enum class BagSlotAction {
    ADD,
    SET,
    REMOVE;

    fun apply(current: Int, amount: Int): Int = when (this) {
        ADD -> current + amount
        SET -> amount
        REMOVE -> current - amount
    }.coerceAtLeast(0)

    companion object {
        fun fromArg(arg: String): BagSlotAction? = entries.firstOrNull { it.name.equals(arg, ignoreCase = true) }
    }
}
