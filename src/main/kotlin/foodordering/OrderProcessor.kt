/*
 * Checkout rules: the delivery fee and turning cart lines into a confirmed Order with a reference.
 * This is a simulated checkout; no payment is taken.
 */
package foodordering

/** Flat delivery charge in naira for orders below [FREE_DELIVERY_THRESHOLD]. */
const val DELIVERY_FEE = 1_500

/** Orders with a subtotal of at least this many naira get free delivery. */
const val FREE_DELIVERY_THRESHOLD = 15_000

private const val REFERENCE_PREFIX = "KFO-"
private const val FIRST_ORDER_NUMBER = 1001

/**
 * Prices and places orders.
 *
 * Order numbers start at [FIRST_ORDER_NUMBER] each time the app starts, because orders are not
 * saved anywhere yet.
 */
class OrderProcessor {
    // A var because every confirmed order uses up the current number.
    private var nextOrderNumber = FIRST_ORDER_NUMBER

    /**
     * Works out the delivery charge for a subtotal.
     *
     * @return 0 when [subtotal] reaches [FREE_DELIVERY_THRESHOLD], otherwise [DELIVERY_FEE].
     */
    fun deliveryFeeFor(subtotal: Int): Int = if (subtotal >= FREE_DELIVERY_THRESHOLD) 0 else DELIVERY_FEE

    /**
     * Confirms an order for [items] and gives it the next order reference.
     *
     * The caller is responsible for clearing the cart afterwards.
     *
     * @return the confirmed order with its subtotal, delivery fee and reference.
     * @throws IllegalArgumentException if [items] is empty.
     */
    fun placeOrder(items: List<CartItem>): Order {
        require(items.isNotEmpty()) { "An order needs at least one item." }
        val subtotal = items.sumOf { it.lineTotal }
        val reference = REFERENCE_PREFIX + nextOrderNumber
        nextOrderNumber++
        return Order(reference, items.toList(), subtotal, deliveryFeeFor(subtotal))
    }
}
