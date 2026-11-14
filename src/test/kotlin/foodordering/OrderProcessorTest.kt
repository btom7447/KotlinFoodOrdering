/*
 * Tests for OrderProcessor: the delivery fee rule, order totals and order references.
 */
package foodordering

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class OrderProcessorTest {
    private val processor = OrderProcessor()
    private val grilledFish = FoodItem(10, "Grilled Fish", Category.GRILLS, "Fish", 9_500)

    @Test
    fun `delivery is charged below the threshold and free from it`() {
        assertEquals(DELIVERY_FEE, processor.deliveryFeeFor(FREE_DELIVERY_THRESHOLD - 1))
        assertEquals(0, processor.deliveryFeeFor(FREE_DELIVERY_THRESHOLD))
    }

    @Test
    fun `placeOrder totals the items and adds the delivery fee`() {
        val order = processor.placeOrder(listOf(CartItem(grilledFish, 1)))
        assertEquals(9_500, order.subtotal)
        assertEquals(DELIVERY_FEE, order.deliveryFee)
        assertEquals(9_500 + DELIVERY_FEE, order.total)
    }

    @Test
    fun `large orders get free delivery`() {
        val order = processor.placeOrder(listOf(CartItem(grilledFish, 2)))
        assertEquals(0, order.deliveryFee)
        assertEquals(19_000, order.total)
    }

    @Test
    fun `each order gets the next reference`() {
        val items = listOf(CartItem(grilledFish, 1))
        assertEquals("KFO-1001", processor.placeOrder(items).reference)
        assertEquals("KFO-1002", processor.placeOrder(items).reference)
    }

    @Test
    fun `an empty order is rejected`() {
        assertFailsWith<IllegalArgumentException> { processor.placeOrder(emptyList()) }
    }
}
