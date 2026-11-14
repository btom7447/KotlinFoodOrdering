/*
 * Model for a confirmed order, created by OrderProcessor at checkout.
 */
package foodordering

/**
 * A confirmed order. It holds its own copy of the cart lines, so clearing the cart afterwards
 * does not change the order.
 *
 * @property reference the order reference shown to the customer, for example `KFO-1001`.
 * @property items the cart lines that were ordered.
 * @property subtotal the sum of all line totals, in naira.
 * @property deliveryFee the delivery charge in naira (0 when delivery is free).
 */
data class Order(
    val reference: String,
    val items: List<CartItem>,
    val subtotal: Int,
    val deliveryFee: Int,
) {
    /** The amount the customer pays: subtotal plus delivery fee, in naira. */
    val total: Int
        get() = subtotal + deliveryFee
}
