package com.willfp.talismans.bag

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class BagSlotsTest {
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
}
