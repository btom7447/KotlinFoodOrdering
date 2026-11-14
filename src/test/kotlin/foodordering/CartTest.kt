/*
 * Tests for Cart: adding, merging, quantity rules, sold-out items, removal and subtotal.
 */
package foodordering

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertTrue

class CartTest {
    private val jollof = FoodItem(1, "Jollof Rice", Category.RICE_AND_MEALS, "Rice", 4_500)
    private val water = FoodItem(2, "Water", Category.DRINKS, "Water", 400)
    private val soldOut = FoodItem(3, "Asun", Category.GRILLS, "Goat", 4_000, isAvailable = false)
    private val cart = Cart()

    @Test
    fun `adding stores the item with its quantity`() {
        cart.add(jollof, 3)
        assertEquals(listOf(CartItem(jollof, 3)), cart.items)
    }

    @Test
    fun `adding the same item twice merges into one line`() {
        cart.add(jollof, 2)
        cart.add(jollof, 1)
        assertEquals(1, cart.items.size)
        assertEquals(3, cart.quantityOf(jollof.id))
    }

    @Test
    fun `sold-out items cannot be added`() {
        assertFailsWith<IllegalArgumentException> { cart.add(soldOut, 1) }
        assertTrue(cart.isEmpty)
    }

    @Test
    fun `zero, negative and over-limit quantities are rejected`() {
        assertFailsWith<IllegalArgumentException> { cart.add(jollof, 0) }
        assertFailsWith<IllegalArgumentException> { cart.add(jollof, -2) }
        cart.add(jollof, MAX_QUANTITY_PER_ITEM)
        assertFailsWith<IllegalArgumentException> { cart.add(jollof, 1) }
        assertEquals(MAX_QUANTITY_PER_ITEM, cart.quantityOf(jollof.id))
    }

    @Test
    fun `updateQuantity replaces the quantity`() {
        cart.add(jollof, 1)
        cart.updateQuantity(jollof.id, 4)
        assertEquals(4, cart.quantityOf(jollof.id))
    }

    @Test
    fun `updateQuantity rejects invalid quantities and items not in the cart`() {
        cart.add(jollof, 1)
        assertFailsWith<IllegalArgumentException> { cart.updateQuantity(jollof.id, 0) }
        assertFailsWith<IllegalArgumentException> { cart.updateQuantity(water.id, 2) }
        assertEquals(1, cart.quantityOf(jollof.id))
    }

    @Test
    fun `remove takes the item out and reports missing items`() {
        cart.add(jollof, 1)
        assertEquals(CartItem(jollof, 1), cart.remove(jollof.id))
        assertNull(cart.remove(jollof.id))
        assertTrue(cart.isEmpty)
    }

    @Test
    fun `clear empties the cart`() {
        cart.add(jollof, 1)
        cart.add(water, 2)
        cart.clear()
        assertTrue(cart.isEmpty)
        assertEquals(0, cart.subtotal())
    }

    @Test
    fun `subtotal adds every line total`() {
        cart.add(jollof, 2)
        cart.add(water, 3)
        assertEquals(2 * 4_500 + 3 * 400, cart.subtotal())
    }
}
