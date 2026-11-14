/*
 * Model for one line in the shopping cart: a food item and how many of it the customer wants.
 */
package foodordering

/**
 * One line in the cart.
 *
 * It is immutable: changing the quantity creates a new line with `copy(quantity = ...)`
 * instead of editing this one, so a line already shown or placed in an order never changes.
 *
 * @property food the menu item being ordered.
 * @property quantity how many portions; the cart keeps this between 1 and [MAX_QUANTITY_PER_ITEM].
 */
data class CartItem(val food: FoodItem, val quantity: Int) {
    /** The price of this line: unit price multiplied by quantity, in naira. */
    val lineTotal: Int
        get() = food.priceNaira * quantity
}
