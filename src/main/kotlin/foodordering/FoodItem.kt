/*
 * Model for one dish or drink on the menu.
 */
package foodordering

/**
 * A single dish or drink the restaurant sells.
 *
 * This is a data class because it only holds values: Kotlin generates `equals`, `hashCode`,
 * `toString` and `copy` for it, so two items with the same fields compare as equal.
 * Every property is a `val` because a menu item never changes while the app is running.
 *
 * @property id unique number the customer types to choose this item.
 * @property name the dish name shown on the menu and used by search.
 * @property category the menu section the item belongs to.
 * @property description a short line describing the dish.
 * @property priceNaira price in whole naira. Whole numbers (not decimals) keep money arithmetic exact.
 * @property isAvailable false when the kitchen has sold out, which stops it being added to a cart.
 */
data class FoodItem(
    val id: Int,
    val name: String,
    val category: Category,
    val description: String,
    val priceNaira: Int,
    val isAvailable: Boolean = true,
)
