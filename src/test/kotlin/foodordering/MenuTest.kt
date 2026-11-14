/*
 * Tests for Menu: lookup, category filtering, search and id uniqueness.
 */
package foodordering

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertTrue

class MenuTest {
    private val menu = Menu(sampleMenuItems())

    @Test
    fun `findById returns the matching item or null`() {
        assertEquals("Jollof Rice & Chicken", menu.findById(1)?.name)
        assertNull(menu.findById(999))
    }

    @Test
    fun `itemsIn returns only items from that category`() {
        val shawarmas = menu.itemsIn(Category.SHAWARMA)
        assertEquals(listOf("Beef Shawarma", "Chicken Shawarma"), shawarmas.map { it.name })
    }

    @Test
    fun `search ignores case and surrounding spaces`() {
        val results = menu.search("  SHAWARMA ")
        assertEquals(listOf(5, 6), results.map { it.id })
    }

    @Test
    fun `search returns nothing for unknown or blank text`() {
        assertTrue(menu.search("pizza").isEmpty())
        assertTrue(menu.search("   ").isEmpty())
    }

    @Test
    fun `itemsByCategory covers every item, sold-out ones included`() {
        val grouped = menu.itemsByCategory()
        assertEquals(sampleMenuItems().size, grouped.values.sumOf { it.size })
        assertTrue(grouped.getValue(Category.GRILLS).any { !it.isAvailable })
    }

    @Test
    fun `duplicate ids are rejected`() {
        val item = sampleMenuItems().first()
        assertFailsWith<IllegalArgumentException> { Menu(listOf(item, item.copy(name = "Copy"))) }
    }
}
