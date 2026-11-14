/*
 * End-to-end tests for the command-line app: each test types a script of answers into
 * FoodOrderingApp and checks what it printed and what happened to the cart.
 */
package foodordering

import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class FoodOrderingAppTest {
    private val cart = Cart()

    /** Runs the app with [answers] typed one per line and returns everything it printed. */
    private fun runApp(vararg answers: String): String {
        val printed = ByteArrayOutputStream()
        val typed = answers.joinToString("\n", postfix = "\n").byteInputStream().bufferedReader()
        FoodOrderingApp(Menu(sampleMenuItems()), cart, OrderProcessor(), typed, PrintStream(printed, true, Charsets.UTF_8)).run()
        return printed.toString(Charsets.UTF_8)
    }

    @Test
    fun `start shows the welcome screen and main menu, and exit closes normally`() {
        val output = runApp("10")
        assertContains(output, "Welcome to KotlinFoodOrdering")
        assertContains(output, "10. Exit")
        assertContains(output, "Goodbye!")
    }

    @Test
    fun `browse all lists every item`() {
        val output = runApp("1", "10")
        for (item in sampleMenuItems()) assertContains(output, item.name)
        assertContains(output, "[SOLD OUT]")
    }

    @Test
    fun `browse by category shows only that category`() {
        val output = runApp("2", "5", "10")
        assertContains(output, "Bottled Water")
        assertFalse("Jollof" in output)
    }

    @Test
    fun `search shows matches and explains when nothing matches`() {
        assertContains(runApp("3", "jollof", "10"), "Jollof Rice & Chicken")
        assertContains(runApp("3", "pizza", "10"), "No food matches \"pizza\"")
        assertContains(runApp("3", "", "10"), "Search text cannot be empty")
    }

    @Test
    fun `invalid menu input shows an error and the app continues`() {
        val output = runApp("abc", "42", "5", "10")
        assertContains(output, "\"abc\" is not an option")
        assertContains(output, "\"42\" is not an option")
        assertContains(output, "Your cart is empty")
        assertContains(output, "Goodbye!")
    }

    @Test
    fun `adding items stores the quantity, and invalid ones are rejected`() {
        val output = runApp("4", "1", "3", "4", "999", "4", "13", "4", "2", "0", "4", "2", "-1", "4", "2", "two", "10")
        assertContains(output, "Added 3 x Jollof Rice & Chicken")
        assertContains(output, "There is no food with ID 999")
        assertContains(output, "Asun (Spicy Goat Meat) is sold out")
        assertContains(output, "0 is not allowed here")
        assertContains(output, "-1 is not allowed here")
        assertContains(output, "\"two\" is not a whole number")
        assertEquals(listOf(1 to 3), cart.items.map { it.food.id to it.quantity })
    }

    @Test
    fun `update, remove and clear change the cart`() {
        runApp("4", "1", "1", "4", "14", "2", "6", "1", "5", "10")
        assertEquals(5, cart.quantityOf(1))
        runApp("7", "14", "10")
        assertEquals(listOf(1), cart.items.map { it.food.id })
        val output = runApp("8", "y", "5", "10")
        assertContains(output, "Your cart is now empty")
        assertTrue(cart.isEmpty)
    }

    @Test
    fun `checkout with an empty cart is prevented`() {
        assertContains(runApp("9", "10"), "Your cart is empty, so there is nothing to check out")
    }

    @Test
    fun `confirmed checkout shows the summary and a reference, then empties the cart`() {
        val output = runApp("4", "10", "1", "4", "14", "2", "9", "y", "10")
        assertContains(output, "Order Summary")
        assertContains(output, formatNaira(10_700))
        assertContains(output, "Order reference: KFO-1001")
        assertContains(output, "Amount due:      ${formatNaira(12_200)}")
        assertTrue(cart.isEmpty)
    }

    @Test
    fun `cancelled checkout keeps the cart`() {
        val output = runApp("4", "1", "1", "9", "n", "10")
        assertContains(output, "Checkout cancelled")
        assertFalse("Order reference" in output)
        assertEquals(1, cart.quantityOf(1))
    }

    @Test
    fun `invalid category, unknown cart id and a non y-n checkout answer are handled`() {
        assertContains(runApp("2", "9", "10"), "9 is not allowed here")
        val output = runApp("4", "1", "1", "6", "5", "9", "maybe", "10")
        assertContains(output, "Food ID 5 is not in your cart")
        assertContains(output, "Checkout cancelled because the answer was not y or n")
        assertEquals(1, cart.quantityOf(1))
    }

    @Test
    fun `adding past the per-item limit is refused`() {
        val output = runApp("4", "1", "$MAX_QUANTITY_PER_ITEM", "4", "1", "10")
        assertContains(output, "You already have the maximum of $MAX_QUANTITY_PER_ITEM")
        assertEquals(MAX_QUANTITY_PER_ITEM, cart.quantityOf(1))
    }

    @Test
    fun `the cart shows how much more earns free delivery, and free delivery once reached`() {
        assertContains(runApp("4", "1", "1", "5", "10"), "Add ${formatNaira(10_500)} more to get free delivery")
        assertContains(runApp("4", "1", "3", "5", "10"), "Free")
    }

    @Test
    fun `the app exits when input ends instead of looping forever`() {
        assertContains(runApp("5"), "Goodbye!")
    }
}
