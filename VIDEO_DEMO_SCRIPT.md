# Video Demo Script: KotlinFoodOrdering

Target length: about 4 minutes 30 seconds (the rubric asks for 4–5 minutes).
Requirements for this video: your face visible the whole time (webcam overlay), the software running, and a detailed walkthrough of the code.

Speaking notes are suggestions. Say them in your own words; you will sound more natural, and you need to be able to explain every part yourself.

## Before you record

1. Open the `KotlinFoodOrdering` folder in VS Code.
2. Open the VS Code terminal and make sure Java 21 is available (see "How to run" in `MODULE_SUBMISSION_NOTES.md`):
   ```bash
   java -version          # should print 21.x
   ./gradlew build        # should end with BUILD SUCCESSFUL
   ```
3. Increase the terminal and editor font size (Cmd + =) so viewers can read the code.
4. Open these files in tabs, in this order: `FoodItem.kt`, `CartItem.kt`, `Cart.kt`, `Menu.kt`, `OrderProcessor.kt`, `FoodOrderingApp.kt` (all in `src/main/kotlin/foodordering/`).
5. Turn on your webcam overlay and check that your face is visible.
6. Do one practice run of the demo inputs below so nothing surprises you.

Demo inputs, in order: `1` → `3`, `shawarma` → `4`, `1`, `2` → `4`, `6`, `1` → `4`, `13` → `6`, `1`, `3` → `5` → `9`, `y` → `10`.

---

## 1. Introduction (0:00–0:25)

**Screen:** your face plus VS Code with the project open.

> "Hi, I'm Ekemini Tom. For this project I built KotlinFoodOrdering, a command-line food ordering application written in Kotlin. You can browse a menu of Nigerian food, search for dishes, manage a shopping cart, and check out. I'll show the program running first, then walk through the code and point out the Kotlin features it uses."

## 2. Application demo (0:25–1:45)

**Screen:** terminal. Run:
```bash
./gradlew run -q
```

| Time | Type | What to say |
|---|---|---|
| 0:25 | (app starts) | "This is the welcome screen and the main menu. Delivery is ₦1,500, or free on orders of ₦15,000 or more." |
| 0:35 | `1` | "Option 1 shows the full menu grouped by category. Each item has an ID, a name, a price in naira and a short description. Asun and Chapman are marked sold out." |
| 0:50 | `3`, then `shawarma` | "I can search by name, and the search ignores upper and lower case." |
| 1:00 | `4`, `1`, `2` | "Now I'll add two plates of Jollof Rice to the cart." |
| 1:07 | `4`, `6`, `1` | "And one Chicken Shawarma." |
| 1:12 | `4`, `13` | "If I try to add Asun, which is sold out, the app refuses and explains why." |
| 1:20 | `6`, `1`, `3` | "Option 6 updates a quantity. I'll change the Jollof Rice from 2 to 3." |
| 1:28 | `5` | "Viewing the cart shows each line total and the subtotal of ₦18,000. Because that's over ₦15,000, delivery is free." |
| 1:35 | `9`, `y` | "At checkout I see the order summary and confirm it. The app gives the order a reference, KFO-1001. This is simulated, so no payment is taken." |
| 1:42 | `10` | "Option 10 exits." |

Optional, if time allows: type `abc` at the main menu to show the error message and that the app keeps running.

## 3. Code walkthrough (1:45–4:10)

### Data classes and val (1:45–2:15)

**Screen:** `FoodItem.kt` lines 20–27, then `CartItem.kt` lines 15–19.

> "The data models are data classes. FoodItem holds an ID, name, category, description, price and availability. Kotlin generates equals, toString and copy for data classes, so I don't write that boilerplate. All the properties are `val`, which means immutable, because a menu item doesn't change while the app runs. Prices are whole naira stored as Int, so the money arithmetic is exact."
>
> "CartItem pairs a food item with a quantity. lineTotal is a computed property: the expression price times quantity."

### Classes, collections and var (2:15–2:55)

**Screen:** `Cart.kt` lines 13–16, then `add` at line 47 and `updateQuantity` at line 65.

> "Cart is a class that owns a `mutableMapOf` from food ID to cart line. It's a val, so the reference never changes, but the map's contents do. That's the difference between an immutable reference and a mutable collection. Because the map is keyed by ID, adding the same dish twice merges it into one line."
>
> "In add, `require` checks that the food is available and the quantity is valid. In updateQuantity, I use the data class `copy` function to make a new line with the new quantity instead of changing the old one."

**Screen:** `Cart.kt` line 88.

> "The subtotal is one expression: sumOf over every line total."

**Screen:** `Menu.kt`, `search` at line 41 and `itemsByCategory` at line 53.

> "The menu is a read-only List. Search uses `filter` with ignoreCase, and itemsByCategory uses `groupBy` to build a Map from each category to its items."

**Screen:** `OrderProcessor.kt` lines 24 and 31.

> "Here is a `var`: nextOrderNumber changes after every confirmed order, which is how the references go KFO-1001, 1002 and so on. The delivery fee uses `if` as an expression: if the subtotal reaches the threshold the fee is 0, otherwise ₦1,500."

### Loops, conditionals and functions (2:55–3:50)

**Screen:** `FoodOrderingApp.kt` lines 47–58 (`run`).

> "FoodOrderingApp is the command-line interface. `run` has the main `while` loop. isRunning is a `var` because it changes when the user chooses Exit. If input ends, the `?:` Elvis operator breaks out of the loop, so the app can't loop forever."

**Screen:** lines 64–77 (`handleMenuChoice`).

> "This `when` expression maps each menu number to a function. Each feature is its own small function, like browseAllFood, addToCart and checkout. `toIntOrNull` returns null for text like 'abc', which falls to the else branch and shows an error instead of crashing."

**Screen:** lines 82–88 (`browseAllFood`) and 249–255 (`printCartLines`).

> "These are `for` loops. The first one destructures each map entry into category and items. The second prints every cart line."

**Screen:** lines 120–139 (`addToCart`).

> "addToCart shows the validation chain using if statements: unknown ID, sold out, already at the maximum quantity, then a quantity limited to an allowed range."

**Screen:** lines 321–335 (`readNumber`) and 214–222 (the `when` in `checkout`).

> "readNumber uses a `when` with conditions to reject non-numbers and out-of-range values. In checkout, `when` covers all three answers: true places the order, false cancels, and null means the answer wasn't y or n."

### Tests (3:50–4:10)

**Screen:** terminal. Run:
```bash
./gradlew test
```
Optionally open `src/test/kotlin/foodordering/FoodOrderingAppTest.kt`.

> "I also wrote 35 automated tests with kotlin.test. They check the cart rules, the delivery fee boundary and the order references, and they run the whole app with scripted input to test invalid input, sold-out items, and confirming and cancelling checkout."

## 4. Conclusion (4:10–4:30)

**Screen:** your face, or the README.

> "Through this project I learned how Kotlin uses val and var, how if and when can return values, how null safety makes me handle bad input, and how data classes and collections keep the code short. In the future I'd like to save order history to a file or database, load the menu from a file, and add customer profiles and delivery addresses. Thanks for watching."

*(Replace the "what I learned" sentence with what you actually learned. Use your notes from the learning-strategies reflection.)*

---

## After recording

1. Upload to YouTube as **Unlisted** (or Public), and check that the link opens in a private browser window.
2. Put the link in `README.md`, replacing `http://youtube.link.goes.here`.
3. Post the video in the matching Microsoft Teams channel.
4. Add the link to your Module Submit document.
