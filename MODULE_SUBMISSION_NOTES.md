# Module Submission Notes

Use this to fill in the official Module Submit Word document. Items marked **YOU** can only be done by you.

## Student information

| | |
|---|---|
| Name | Ekemini Benjamin John Tom |
| Course | CSE 310 – Applied Programming |
| Module | Module 1 |
| Language | Kotlin |

## Project information

| | |
|---|---|
| Project name | KotlinFoodOrdering |
| GitHub repository | https://github.com/btom7447/KotlinFoodOrdering (public) |
| Video URL | **YOU:** `<paste your YouTube link here after uploading>` |

## How to run (for you and for graders)

The project needs Java 21. On this Mac it was installed with Homebrew (`brew install openjdk@21`). Make it available to every terminal, VS Code's included, by adding this line to `~/.zshrc`, then open a new terminal:

```bash
export JAVA_HOME=/opt/homebrew/opt/openjdk@21/libexec/openjdk.jdk/Contents/Home
```

Then, from the project folder:

```bash
./gradlew build      # compiles and runs the 35 tests
./gradlew run -q     # starts the app
```

## Module-specific checklist (Kotlin)

All paths are in `src/main/kotlin/foodordering/`.

| Requirement | Done | Where it appears |
|---|---|---|
| Mutable and immutable variables | ✅ | `val` everywhere a value never changes: every `FoodItem` property (`FoodItem.kt:21-26`) and local results such as `val subtotal` (`OrderProcessor.kt:43`). `var` where a value must change: `var nextOrderNumber` (`OrderProcessor.kt:24`), which increases after each order, and `var isRunning` (`FoodOrderingApp.kt:49`), which controls the main loop. |
| Expressions | ✅ | Line total `food.priceNaira * quantity` (`CartItem.kt:18`); subtotal `sumOf { it.lineTotal }` (`Cart.kt:88`); order total `subtotal + deliveryFee` (`Order.kt:23`); comparison expression `subtotal >= FREE_DELIVERY_THRESHOLD` (`OrderProcessor.kt:31`); conditional results such as `if (deliveryFee == 0) "Free" else ...` (`FoodOrderingApp.kt:263`). |
| Conditionals | ✅ | `if` as an expression for the delivery fee (`OrderProcessor.kt:31`); `if` validation chain in `addToCart` (`FoodOrderingApp.kt:120-139`); `when` with values for the main menu (`FoodOrderingApp.kt:65`); `when` with conditions in `readNumber` (`FoodOrderingApp.kt:324`); `when` on `Boolean?` at checkout (`FoodOrderingApp.kt:214`). |
| Loops | ✅ | `while` main loop (`FoodOrderingApp.kt:50`); `for` over map entries with destructuring (`FoodOrderingApp.kt:84`); `for` with `withIndex()` for the category and main menus (lines 94, 283); `for` over food items and cart lines (lines 241, 251). |
| Functions | ✅ | 40 functions, each with one job: for example `searchFood`, `addToCart`, `updateQuantity`, `checkout` and `readNumber` in `FoodOrderingApp.kt`; `Cart.add` and `Cart.subtotal`; `Menu.search`; `OrderProcessor.deliveryFeeFor`; and the top-level `formatNaira` and `sampleMenuItems`. They use default parameters (`readNumber(prompt, allowed = null)`) and nullable return types. |
| Classes | ✅ | `Menu`, `Cart`, `OrderProcessor` and `FoodOrderingApp`, each with private state and a public interface; an `enum class Category`. |
| Additional: Data classes | ✅ | `FoodItem`, `CartItem` and `Order`. `copy()` is used in `Cart.updateQuantity` (`Cart.kt:68`), and generated `equals` is used in the tests. |
| Additional: Collections | ✅ | Read-only `List` for the menu (`SampleMenu.kt:15`, `Menu.kt:15`) and the main-menu options; `MutableMap` for the cart (`Cart.kt:16`) that is added to, updated, removed from and cleared; a `Map` built with `groupBy` (`Menu.kt:53`); `filter`, `find`, `sumOf` and `withIndex`. |

## General checklist

| Item | Status |
|---|---|
| Original software project | ✅ Built from scratch for this module |
| Public GitHub repository | ✅ https://github.com/btom7447/KotlinFoodOrdering |
| At least 100 meaningful lines of code | ✅ 387 non-blank, non-comment lines of application code (319 if lines with only a closing brace are excluded), plus 272 lines of tests |
| Useful comments | ✅ A header comment on every file; comments explain *why* |
| Function-level comment on every function | ✅ All 40 functions have KDoc (checked with a script) |
| Completed README (official template) | ✅ Except the video link: **YOU** replace the placeholder |
| Application runs and is tested | ✅ `./gradlew build` passes, 35/35 tests |
| Video with your face visible | ⏳ **YOU** record it (script: `VIDEO_DEMO_SCRIPT.md`) |
| Detailed code walkthrough in the video | ⏳ **YOU** (the script covers it) |
| Video posted in the matching Microsoft Teams channel | ⏳ **YOU** |
| Video URL in the submission document | ⏳ **YOU** |
| Time log with 20+ hours | ⏳ **YOU** (see below) |
| Learning strategies reflection | ⏳ **YOU** (see below) |

## Time log

Only record time you actually spent. The rubric requires a total of at least 20 hours.

| Date | Activity | Time spent |
|---|---|---|
| | | |
| | | |
| | | |
| **Total** | | **0 h (fill in)** |

No hours are filled in. AI-assisted build time is not your personal study time, and I don't have a record of your hours. If your genuine total is under 20 hours, these are real, useful ways to make up the difference:

- Read the Kotlin docs pages listed in the README (basic syntax, control flow, functions, classes, data classes, collections, null safety) and try their examples in the [Kotlin Playground](https://play.kotlinlang.org/).
- Read every file in `src/main/kotlin/foodordering/` until you can explain each function without notes.
- Make a change yourself, for example add search by description, or a new category, and add a test for it.
- Try breaking things on purpose (change a `>=` to `>`, remove a check) and see which tests fail and why.
- Run the app and try every invalid input you can think of.
- Rehearse the video walkthrough two or three times, then record it.
- Write your reflection and review the README.

## Learning strategies reflection (complete honestly in your own words)

Answer each prompt briefly with what actually happened.

1. **How I approached learning Kotlin.** Where did you start? Did you learn by reading first, by building, or both?
2. **Official documentation.** Which kotlinlang.org pages did you actually read? Name one thing you learned from them that appears in this project (for example `val` vs `var`, `when` as an expression, data class `copy`, `groupBy`).
3. **Practising concepts.** How did you practise? (Small experiments, changing the code, the Kotlin Playground, …)
4. **Testing and debugging.** Did you run the app and try invalid input? Did you read the tests or break the code to watch tests fail? Describe one problem you found or understood.
5. **Using AI as a learning assistant.** Be specific and honest. This project was built with Claude Code's help. What did you ask it to explain? How did you check that you understood the code it wrote?
6. **What I found difficult.** For example nullable types and `?:`, the difference between a `val` reference and a mutable collection, or Gradle.
7. **What I understand better now.**
8. **What I would do differently next time.** For example writing more of the code yourself first, or starting the time log on day one.
