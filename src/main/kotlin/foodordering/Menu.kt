/*
 * The restaurant menu: looks up, searches, filters and groups food items.
 * It keeps its own read-only copy of the items, so the menu cannot change while the app is running.
 */
package foodordering

/**
 * Read-only access to the food items the restaurant offers.
 *
 * @param items the food items on the menu; every id must be unique.
 * @throws IllegalArgumentException if two items share the same id.
 */
class Menu(items: List<FoodItem>) {
    // Copied so that a caller holding a mutable list cannot change the menu afterwards.
    private val items: List<FoodItem> = items.toList()

    init {
        // Customers pick food by id, so a duplicate id would make a choice ambiguous.
        require(items.distinctBy { it.id }.size == items.size) { "Every menu item needs a unique id." }
    }

    /**
     * Finds one item by its id.
     *
     * @return the matching item, or null when no item has that id.
     */
    fun findById(id: Int): FoodItem? = items.find { it.id == id }

    /**
     * Lists the items in one menu section.
     *
     * @return the items in [category], in menu order (empty if the section has none).
     */
    fun itemsIn(category: Category): List<FoodItem> = items.filter { it.category == category }

    /**
     * Searches item names for [query], ignoring upper/lower case and surrounding spaces.
     *
     * @return the matching items, or an empty list when nothing matches or the query is blank.
     */
    fun search(query: String): List<FoodItem> {
        val searchTerm = query.trim()
        if (searchTerm.isEmpty()) return emptyList()
        return items.filter { it.name.contains(searchTerm, ignoreCase = true) }
    }

    /**
     * Groups every item (sold-out ones included) by its section, so the full menu can be printed
     * section by section.
     *
     * @return a map from each category that has items to that category's items.
     */
    fun itemsByCategory(): Map<Category, List<FoodItem>> = items.groupBy { it.category }
}
