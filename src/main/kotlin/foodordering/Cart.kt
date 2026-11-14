/*
 * The customer's shopping cart: the only place where the order being built is stored and changed.
 */
package foodordering

/** The largest quantity of a single item one cart may hold. Stops typos like 500 portions. */
const val MAX_QUANTITY_PER_ITEM = 20

/**
 * Holds the items the customer has chosen and keeps every line valid:
 * only available food, and quantities between 1 and [MAX_QUANTITY_PER_ITEM].
 */
class Cart {
    // Keyed by food id, so adding the same dish twice merges into one line instead of two.
    // mutableMapOf keeps insertion order, so the cart lists food in the order it was added.
    private val linesByFoodId = mutableMapOf<Int, CartItem>()

    /** A read-only snapshot of the cart lines, in the order they were added. */
    val items: List<CartItem>
        get() = linesByFoodId.values.toList()

    /** True when the cart has no lines. */
    val isEmpty: Boolean
        get() = linesByFoodId.isEmpty()

    /**
     * Gets the cart line for one food item.
     *
     * @return the line for [foodId], or null if that food is not in the cart.
     */
    fun find(foodId: Int): CartItem? = linesByFoodId[foodId]

    /**
     * Gets how many of one food item are already in the cart.
     *
     * @return the quantity of [foodId] in the cart, or 0 if it is not there.
     */
    fun quantityOf(foodId: Int): Int = find(foodId)?.quantity ?: 0

    /**
     * Adds [quantity] portions of [food], merging with any portions already in the cart.
     *
     * @return the cart line after adding.
     * @throws IllegalArgumentException if the food is sold out, [quantity] is not positive,
     *   or the line would go over [MAX_QUANTITY_PER_ITEM].
     */
    fun add(food: FoodItem, quantity: Int): CartItem {
        require(food.isAvailable) { "${food.name} is sold out." }
        val newQuantity = quantityOf(food.id) + quantity
        require(quantity > 0 && newQuantity <= MAX_QUANTITY_PER_ITEM) {
            "Quantity must be at least 1, and the cart can hold at most $MAX_QUANTITY_PER_ITEM ${food.name}."
        }
        val line = CartItem(food, newQuantity)
        linesByFoodId[food.id] = line
        return line
    }

    /**
     * Replaces the quantity of a food item that is already in the cart.
     *
     * @return the updated cart line.
     * @throws IllegalArgumentException if [quantity] is outside 1..[MAX_QUANTITY_PER_ITEM]
     *   or [foodId] is not in the cart.
     */
    fun updateQuantity(foodId: Int, quantity: Int): CartItem {
        require(quantity in 1..MAX_QUANTITY_PER_ITEM) { "Quantity must be between 1 and $MAX_QUANTITY_PER_ITEM." }
        val line = requireNotNull(find(foodId)) { "Food ID $foodId is not in the cart." }
        val updatedLine = line.copy(quantity = quantity)
        linesByFoodId[foodId] = updatedLine
        return updatedLine
    }

    /**
     * Removes one food item from the cart.
     *
     * @return the removed line, or null if that food was not in the cart.
     */
    fun remove(foodId: Int): CartItem? = linesByFoodId.remove(foodId)

    /** Removes every line from the cart. */
    fun clear() = linesByFoodId.clear()

    /**
     * Adds up the line totals.
     *
     * @return the cart subtotal in naira (0 for an empty cart).
     */
    fun subtotal(): Int = linesByFoodId.values.sumOf { it.lineTotal }
}
