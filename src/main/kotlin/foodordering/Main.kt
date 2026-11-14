/*
 * Entry point: builds the menu, cart and order processor, then starts the command-line app.
 */
package foodordering

/** Starts KotlinFoodOrdering, reading from the keyboard and printing to the terminal. */
fun main() {
    val app = FoodOrderingApp(Menu(sampleMenuItems()), Cart(), OrderProcessor())
    app.run()
}
