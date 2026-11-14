/*
 * The sections of the food menu. Used to group items and to let customers browse one section at a time.
 */
package foodordering

/**
 * A menu section. The declaration order is the order sections are shown to the customer.
 *
 * @property displayName the human-readable name printed in the terminal.
 */
enum class Category(val displayName: String) {
    RICE_AND_MEALS("Rice and Meals"),
    SHAWARMA("Shawarma"),
    SMALL_CHOPS("Small Chops"),
    GRILLS("Grills"),
    DRINKS("Drinks"),
}
