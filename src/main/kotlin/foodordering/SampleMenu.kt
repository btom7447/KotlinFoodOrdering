/*
 * The demonstration menu the app starts with. Prices are realistic sample values in naira,
 * not live prices from any real restaurant.
 */
package foodordering

/**
 * Builds the sample menu.
 *
 * `listOf` returns a read-only list: the menu is fixed while the app runs, so nothing
 * should be able to add or remove items from it.
 *
 * @return the sample food items, with unique ids starting at 1.
 */
fun sampleMenuItems(): List<FoodItem> = listOf(
    FoodItem(1, "Jollof Rice & Chicken", Category.RICE_AND_MEALS, "Smoky party jollof with fried chicken", 4_500),
    FoodItem(2, "Fried Rice & Chicken", Category.RICE_AND_MEALS, "Vegetable fried rice with fried chicken", 4_500),
    FoodItem(3, "Ofada Rice & Ayamase", Category.RICE_AND_MEALS, "Local rice with spicy green pepper sauce", 5_000),
    FoodItem(4, "Pounded Yam & Egusi", Category.RICE_AND_MEALS, "Pounded yam with melon seed soup and beef", 5_500),
    FoodItem(5, "Beef Shawarma", Category.SHAWARMA, "Grilled beef, cabbage and cream in a wrap", 4_000),
    FoodItem(6, "Chicken Shawarma", Category.SHAWARMA, "Grilled chicken, cabbage and cream in a wrap", 4_500),
    FoodItem(7, "Samosa (4 pcs)", Category.SMALL_CHOPS, "Crispy pastry with spiced minced meat", 2_000),
    FoodItem(8, "Spring Rolls (4 pcs)", Category.SMALL_CHOPS, "Crispy rolls with vegetable filling", 2_000),
    FoodItem(9, "Puff-Puff (10 pcs)", Category.SMALL_CHOPS, "Sweet fried dough balls", 1_500),
    FoodItem(10, "Grilled Fish (Croaker)", Category.GRILLS, "Whole croaker grilled with pepper sauce", 9_500),
    FoodItem(11, "Grilled Chicken (Quarter)", Category.GRILLS, "Quarter chicken grilled with suya spice", 3_500),
    FoodItem(12, "Beef Suya", Category.GRILLS, "Spicy skewered beef with onions", 3_000),
    FoodItem(13, "Asun (Spicy Goat Meat)", Category.GRILLS, "Peppered smoked goat meat", 4_000, isAvailable = false),
    FoodItem(14, "Soft Drink (50cl)", Category.DRINKS, "Chilled soft drink", 600),
    FoodItem(15, "Bottled Water (75cl)", Category.DRINKS, "Chilled table water", 400),
    FoodItem(16, "Chapman", Category.DRINKS, "Fruity Nigerian mocktail", 2_500, isAvailable = false),
)
