package com.willfp.talismans.bag

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class BagSlotsTest {
    private val slotCounts = mapOf(1 to 8, 2 to 17, 3 to 26, 4 to 35, 5 to 44, 6 to 53)

    @Test
    fun `bought adds to base`() {
        assertEquals(8, effectiveBagSize(base = 5, bought = 3))
    }

    @Test
    fun `unlimited ignores bought`() {
        assertEquals(UNLIMITED_BAG_SIZE, effectiveBagSize(base = UNLIMITED_BAG_SIZE, bought = 50))
    }

    @Test
    fun `negative bought ignored`() {
        assertEquals(5, effectiveBagSize(base = 5, bought = -2))
    }

    @Test
    fun `size capped at unlimited`() {
        assertEquals(UNLIMITED_BAG_SIZE, effectiveBagSize(base = 9999, bought = 50))
    }

    @Test
    fun `huge bought no overflow`() {
        assertEquals(UNLIMITED_BAG_SIZE, effectiveBagSize(base = 5, bought = Int.MAX_VALUE))
    }

    @Test
    fun `add saturates`() {
        assertEquals(Int.MAX_VALUE, BagSlotAction.ADD.apply(current = Int.MAX_VALUE, amount = 1))
    }

    @Test
    fun `remove clamps at zero`() {
        assertEquals(0, BagSlotAction.REMOVE.apply(current = 2, amount = 5))
    }

    @Test
    fun `set clamps at zero`() {
        assertEquals(0, BagSlotAction.SET.apply(current = 4, amount = -1))
    }

    @Test
    fun `add adds`() {
        assertEquals(7, BagSlotAction.ADD.apply(current = 4, amount = 3))
    }

    @Test
    fun `action arg parses`() {
        assertEquals(BagSlotAction.SET, BagSlotAction.fromArg("Set"))
        assertNull(BagSlotAction.fromArg("x"))
    }

    @Test
    fun `cap uses smaller value`() {
        assertEquals(27, upgradeCap(maxSize = 27, displayableSlots = 53))
        assertEquals(53, upgradeCap(maxSize = 100, displayableSlots = 53))
    }

    @Test
    fun `rows respect minimum`() {
        assertEquals(3, bagRows(size = 1, minimumRows = 3, slotCounts = slotCounts))
    }

    @Test
    fun `rows fit size`() {
        assertEquals(2, bagRows(size = 10, minimumRows = 1, slotCounts = slotCounts))
    }

    @Test
    fun `rows max six`() {
        assertEquals(6, bagRows(size = 500, minimumRows = 1, slotCounts = slotCounts))
    }
}
