/*
 * The command-line front end. It prints menus, reads what the customer types, checks that input,
 * and asks Menu, Cart and OrderProcessor to do the real work.
 */
package foodordering

import java.io.BufferedReader
import java.io.PrintStream

private const val DIVIDER_WIDTH = 64
private const val EXIT_CHOICE = 10

private val MAIN_MENU_OPTIONS = listOf(
    "Browse all food",
    "Browse by category",
    "Search for food",
    "Add item to cart",
    "View cart",
    "Update item quantity",
    "Remove item from cart",
    "Clear cart",
    "Checkout",
    "Exit",
)

/**
 * Runs the interactive food ordering session.
 *
 * Input and output are passed in (defaulting to the keyboard and terminal) so automated tests
 * can type into the app and read what it printed.
 *
 * @param menu the food the customer can choose from.
 * @param cart the customer's cart; this class changes it as the customer shops.
 * @param orderProcessor prices and confirms orders at checkout.
 * @param input where customer input is read from, one line per answer.
 * @param output where everything the customer sees is printed.
 */
class FoodOrderingApp(
    private val menu: Menu,
    private val cart: Cart,
    private val orderProcessor: OrderProcessor,
    private val input: BufferedReader = System.`in`.bufferedReader(),
    private val output: PrintStream = System.out,
) {

    /** Shows the welcome screen, then repeats the main menu until the customer exits or input ends. */
    fun run() {
        printWelcome()
        var isRunning = true
        while (isRunning) {
            printMainMenu()
            // A null answer means input has ended (for example Ctrl+D), so leave instead of looping forever.
            val answer = ask("Choose an option (1-$EXIT_CHOICE): ") ?: break
            isRunning = handleMenuChoice(answer)
        }
        output.println("\nThank you for using KotlinFoodOrdering. Goodbye!")
    }

    /**
     * Carries out one main-menu choice.
     *
     * @return false when the customer chose Exit, true to keep showing the menu.
     */
    private fun handleMenuChoice(answer: String): Boolean {
        when (answer.toIntOrNull()) {
            1 -> browseAllFood()
            2 -> browseByCategory()
            3 -> searchFood()
            4 -> addToCart()
            5 -> viewCart()
            6 -> updateQuantity()
            7 -> removeFromCart()
            8 -> clearCart()
            9 -> checkout()
            EXIT_CHOICE -> return false
            else -> printError("\"$answer\" is not an option. Please enter a number from 1 to $EXIT_CHOICE.")
        }
        return true
    }

    /** Prints every menu section with its items, sold-out items included. */
    private fun browseAllFood() {
        printHeading("Full Menu")
        for ((category, items) in menu.itemsByCategory()) {
            output.println("\n-- ${category.displayName} --")
            printFoodList(items)
        }
    }

    /** Lets the customer pick one category and prints only the items in it. */
    private fun browseByCategory() {
        printHeading("Categories")
        val categories = Category.entries
        for ((index, category) in categories.withIndex()) {
            output.println("  ${index + 1}. ${category.displayName}")
        }
        val choice = readNumber("Choose a category (1-${categories.size}): ", 1..categories.size) ?: return
        val category = categories[choice - 1]
        printHeading(category.displayName)
        printFoodList(menu.itemsIn(category))
    }

    /** Asks for a search term and prints matching items, or a hint when nothing matches. */
    private fun searchFood() {
        val query = ask("Enter a food name to search for: ") ?: return
        if (query.isEmpty()) {
            printError("Search text cannot be empty. Try a word such as \"rice\" or \"shawarma\".")
            return
        }
        val results = menu.search(query)
        if (results.isEmpty()) {
            printError("No food matches \"$query\". Try a shorter word, or browse by category.")
            return
        }
        printHeading("Search results for \"$query\" (${results.size} found)")
        printFoodList(results)
    }

    /** Asks for a food id and a quantity, checks both, then adds the item to the cart. */
    private fun addToCart() {
        val foodId = readNumber("Enter the food ID to add: ") ?: return
        val food = menu.findById(foodId)
        if (food == null) {
            printError("There is no food with ID $foodId. Browse the menu to see the IDs.")
            return
        }
        if (!food.isAvailable) {
            printError("Sorry, ${food.name} is sold out right now. Please choose something else.")
            return
        }
        val spaceLeft = MAX_QUANTITY_PER_ITEM - cart.quantityOf(food.id)
        if (spaceLeft == 0) {
            printError("You already have the maximum of $MAX_QUANTITY_PER_ITEM ${food.name} in your cart.")
            return
        }
        val quantity = readNumber("How many ${food.name}? (1-$spaceLeft): ", 1..spaceLeft) ?: return
        val line = cart.add(food, quantity)
        printSuccess("Added $quantity x ${food.name}. Your cart now has ${line.quantity} of this item.")
    }

    /** Prints the cart lines and totals, or an empty-cart message. */
    private fun viewCart() {
        if (cart.isEmpty) {
            printEmptyCart()
            return
        }
        printHeading("Your Cart")
        printCartLines()
        printTotals(cart.subtotal())
    }

    /** Changes the quantity of an item already in the cart. */
    private fun updateQuantity() {
        if (cart.isEmpty) {
            printEmptyCart()
            return
        }
        printHeading("Update Quantity")
        printCartLines()
        val foodId = readNumber("Enter the food ID to update: ") ?: return
        val line = cart.find(foodId)
        if (line == null) {
            printError("Food ID $foodId is not in your cart.")
            return
        }
        val quantity = readNumber(
            "New quantity for ${line.food.name} (1-$MAX_QUANTITY_PER_ITEM, or use \"Remove item\" instead): ",
            1..MAX_QUANTITY_PER_ITEM,
        ) ?: return
        cart.updateQuantity(foodId, quantity)
        printSuccess("${line.food.name} quantity changed from ${line.quantity} to $quantity.")
    }

    /** Removes one item from the cart. */
    private fun removeFromCart() {
        if (cart.isEmpty) {
            printEmptyCart()
            return
        }
        printHeading("Remove Item")
        printCartLines()
        val foodId = readNumber("Enter the food ID to remove: ") ?: return
        val removedLine = cart.remove(foodId)
        if (removedLine == null) {
            printError("Food ID $foodId is not in your cart.")
        } else {
            printSuccess("Removed ${removedLine.food.name} from your cart.")
        }
    }

    /** Empties the cart after the customer confirms. */
    private fun clearCart() {
        if (cart.isEmpty) {
            printEmptyCart()
            return
        }
        if (confirm("Remove all ${cart.items.size} item(s) from your cart? (y/n): ") == true) {
            cart.clear()
            printSuccess("Your cart is now empty.")
        } else {
            output.println("Your cart was not changed.")
        }
    }

    /** Shows the order summary and, if the customer confirms, places the order and empties the cart. */
    private fun checkout() {
        if (cart.isEmpty) {
            printError("Your cart is empty, so there is nothing to check out. Choose \"Add item to cart\" first.")
            return
        }
        printHeading("Order Summary")
        printCartLines()
        printTotals(cart.subtotal())
        when (confirm("Place this order? (y/n): ")) {
            true -> {
                val order = orderProcessor.placeOrder(cart.items)
                cart.clear()
                printOrderConfirmation(order)
            }
            false -> output.println("Checkout cancelled. Your cart has been kept.")
            null -> output.println("Checkout cancelled because the answer was not y or n. Your cart has been kept.")
        }
    }

    /** Prints the confirmation screen for a placed order. */
    private fun printOrderConfirmation(order: Order) {
        printHeading("Order Confirmed")
        output.println("Order reference: ${order.reference}")
        output.println("Items ordered:   ${order.items.sumOf { it.quantity }}")
        output.println("Amount due:      ${formatNaira(order.total)} (pay on delivery)")
        printSuccess("Thank you! Your food is being prepared. This is a simulated order; no payment was taken.")
    }

    /** Prints food items as a table, showing which ones are sold out. */
    private fun printFoodList(items: List<FoodItem>) {
        if (items.isEmpty()) {
            output.println("No food in this section yet.")
            return
        }
        output.println(String.format("%-4s %-27s %10s", "ID", "Item", "Price"))
        for (item in items) {
            val status = if (item.isAvailable) "" else "  [SOLD OUT]"
            output.println(String.format("%-4d %-27s %10s%s", item.id, item.name, formatNaira(item.priceNaira), status))
            output.println("     ${item.description}")
        }
    }

    /** Prints each cart line with its quantity and line total. */
    private fun printCartLines() {
        output.println(String.format("%-4s %-27s %4s %12s", "ID", "Item", "Qty", "Line total"))
        for (line in cart.items) {
            output.println(
                String.format("%-4d %-27s %4d %12s", line.food.id, line.food.name, line.quantity, formatNaira(line.lineTotal)),
            )
        }
    }

    /** Prints subtotal, delivery fee and total for [subtotal], plus how much more earns free delivery. */
    private fun printTotals(subtotal: Int) {
        val deliveryFee = orderProcessor.deliveryFeeFor(subtotal)
        output.println("-".repeat(DIVIDER_WIDTH))
        output.println(String.format("%-37s %12s", "Subtotal", formatNaira(subtotal)))
        output.println(String.format("%-37s %12s", "Delivery fee", if (deliveryFee == 0) "Free" else formatNaira(deliveryFee)))
        output.println(String.format("%-37s %12s", "Total", formatNaira(subtotal + deliveryFee)))
        if (deliveryFee > 0) {
            output.println("Add ${formatNaira(FREE_DELIVERY_THRESHOLD - subtotal)} more to get free delivery.")
        }
    }

    /** Prints the welcome banner shown once when the app starts. */
    private fun printWelcome() {
        printHeading("Welcome to KotlinFoodOrdering")
        output.println("Order Nigerian favourites from your terminal.")
        output.println(
            "Delivery is ${formatNaira(DELIVERY_FEE)}, free on orders of ${formatNaira(FREE_DELIVERY_THRESHOLD)} or more.",
        )
        output.println("Prices are sample data for demonstration.")
    }

    /** Prints the numbered main menu. */
    private fun printMainMenu() {
        printHeading("Main Menu")
        for ((index, option) in MAIN_MENU_OPTIONS.withIndex()) {
            output.println("  ${index + 1}. $option")
        }
    }

    /** Prints [title] between two divider lines. */
    private fun printHeading(title: String) {
        output.println()
        output.println("=".repeat(DIVIDER_WIDTH))
        output.println(title)
        output.println("=".repeat(DIVIDER_WIDTH))
    }

    /** Prints the message shown whenever an action needs a non-empty cart. */
    private fun printEmptyCart() = output.println("Your cart is empty. Choose \"Add item to cart\" from the main menu.")

    /** Prints a success message with a consistent marker. */
    private fun printSuccess(message: String) = output.println("[OK] $message")

    /** Prints an error message with a consistent marker. */
    private fun printError(message: String) = output.println("[!] $message")

    /**
     * Prints [prompt] and reads one line of input.
     *
     * @return the trimmed answer, or null when input has ended.
     */
    private fun ask(prompt: String): String? {
        output.print(prompt)
        output.flush()
        return input.readLine()?.trim()
    }

    /**
     * Asks for a whole number, optionally limited to [allowed]. Invalid answers are explained to the customer.
     *
     * @return the number, or null when input ended, the answer was not a whole number, or it was out of range.
     */
    private fun readNumber(prompt: String, allowed: IntRange? = null): Int? {
        val answer = ask(prompt) ?: return null
        val number = answer.toIntOrNull()
        return when {
            number == null -> {
                printError("\"$answer\" is not a whole number.")
                null
            }
            allowed != null && number !in allowed -> {
                printError("$number is not allowed here. Please enter a number from ${allowed.first} to ${allowed.last}.")
                null
            }
            else -> number
        }
    }

    /**
     * Asks a yes/no question.
     *
     * @return true for y/yes, false for n/no, or null when input ended or the answer was something else.
     */
    private fun confirm(prompt: String): Boolean? {
        val answer = ask(prompt) ?: return null
        return when (answer.lowercase()) {
            "y", "yes" -> true
            "n", "no" -> false
            else -> null
        }
    }
}
