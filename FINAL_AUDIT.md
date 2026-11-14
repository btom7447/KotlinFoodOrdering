# Final Rubric Audit: KotlinFoodOrdering

Audit date: 2026-11-14. Only evidence verified on that date is counted.

## Summary

| Criterion | Available points | Status | Evidence / Remaining action |
|---|---|---|---|
| 1. Video demonstration (face, running software, detailed code walkthrough) | 10 | PENDING | Not recorded yet. Script ready in `VIDEO_DEMO_SCRIPT.md` (about 4:30, with timestamps). **You:** record with your face visible and upload. |
| 2. Module attempted, original software, public GitHub | 10 | COMPLETE | Original CLI app; builds and runs. Public repo https://github.com/btom7447/KotlinFoodOrdering: GitHub reports `visibility: PUBLIC`, and an unauthenticated request returned HTTP 200 (see Verification log). |
| 3. Unique Kotlin requirements (6 compulsory + at least 1 additional) | 40 | COMPLETE | All 6 compulsory requirements plus both additional ones (data classes, collections). Evidence below. |
| 4. Code length and comments | 10 | COMPLETE | 387 meaningful lines of application code (blank and comment lines excluded); 40/40 functions have KDoc; every file has a header comment. |
| 5. README | 10 | NEEDS MY ACTION | Official template headings kept, in the repository root, all sections filled in, and versions verified. **You:** replace `http://youtube.link.goes.here` with your real video link. |
| 6. Time log (20+ hours) | 10 | PENDING | No hours recorded. The table in `MODULE_SUBMISSION_NOTES.md` is empty. **You:** log only real hours, at least 20 in total. |
| 7. Learning strategies discussion | 5 | PENDING | Prompts ready in `MODULE_SUBMISSION_NOTES.md`. **You:** write your own honest answers. |
| 8. Video posted in Microsoft Teams channel | 5 | NEEDS MY ACTION | No Teams access from this environment. **You:** post the video in the matching channel. |

**Estimated score from verified evidence: 60 / 100** (criteria 2, 3 and 4).
Adding the video link to the README makes it about 70. All of your remaining actions, done well, make 100 possible.

## Criterion 3 evidence (source: `src/main/kotlin/foodordering/`)

| # | Requirement | Evidence |
|---|---|---|
| 1 | Mutable and immutable variables | `val` for values that never change: all `FoodItem` properties (`FoodItem.kt:21-26`) and the `Cart` map reference (`Cart.kt:16`). `var` where a value must change: `nextOrderNumber` (`OrderProcessor.kt:24`, increased on every order) and `isRunning` (`FoodOrderingApp.kt:49`, controls the main loop). |
| 2 | Expressions | `food.priceNaira * quantity` (`CartItem.kt:18`); `sumOf { it.lineTotal }` (`Cart.kt:88`); `subtotal + deliveryFee` (`Order.kt:23`); `MAX_QUANTITY_PER_ITEM - cart.quantityOf(food.id)` (`FoodOrderingApp.kt:131`); `if`/`when` used as expressions that return values (`OrderProcessor.kt:31`, `FoodOrderingApp.kt:242`, `263`, `324`). |
| 3 | Conditionals | `if`/`else` validation in `addToCart` (`FoodOrderingApp.kt:123-135`); `when (answer.toIntOrNull())` main menu (`:65`); `when { ... }` input checks (`:324`); `when` on `Boolean?` at checkout (`:214`); `when (answer.lowercase())` (`:344`). |
| 4 | Loops | `while (isRunning)` (`FoodOrderingApp.kt:50`); `for ((category, items) in ...)` (`:84`); `for ((index, category) in categories.withIndex())` (`:94`); `for (item in items)` (`:241`); `for (line in cart.items)` (`:251`); `for` over main menu options (`:283`). |
| 5 | Functions | 40 functions with single responsibilities, for example `searchFood`, `addToCart`, `updateQuantity`, `removeFromCart`, `checkout`, `readNumber`, `Cart.add`, `Menu.search`, `OrderProcessor.deliveryFeeFor`, `formatNaira`. They include default parameters, nullable returns and expression bodies. |
| 6 | Classes | `Menu`, `Cart`, `OrderProcessor` and `FoodOrderingApp` (private state, public methods, constructor parameters, an `init` block), plus `enum class Category`. |
| 7 | Data classes | `FoodItem`, `CartItem` and `Order`. `copy()` in `Cart.kt:68`; generated `equals` relied on in `CartTest`. |
| 8 | Collections | Read-only `List` (`SampleMenu.kt:15`, `Menu.kt:15`, `MAIN_MENU_OPTIONS`); `MutableMap` cart that is created, read, added to, updated, removed from and cleared (`Cart.kt`); `Map` from `groupBy` (`Menu.kt:53`); `filter`, `find`, `sumOf`, `withIndex`, `distinctBy`. |

## Criterion 4 evidence

- Meaningful line count, measured with a script that ignores blank lines, `//` lines and `/* */` blocks: **387** lines of application code (319 if lines with only `}` or `)` are also excluded); **272** lines of tests (not counted toward the requirement).
- Function comments: a script checked that the line before each of the 40 `fun` declarations ends a KDoc comment. Result: 40/40.
- Comments explain *why* (for example why prices are `Int`, why the cart is keyed by food id, why `Locale.US` is fixed, why a null answer ends the loop).

## Verification log (commands actually run on 2026-11-14)

| Check | Result |
|---|---|
| `./gradlew clean build` | BUILD SUCCESSFUL, no compiler warnings |
| Automated tests | 35 run, 35 passed, 0 skipped (CartTest 9, FoodOrderingAppTest 14, MenuTest 6, MoneyTest 1, OrderProcessorTest 5) |
| Test strength check | Breaking the free-delivery rule (`>=` → `>`) and removing the sold-out check each made a test fail; the code was then restored |
| Manual run: `./gradlew run -q` with the demo inputs from the video script | Output matched the script: sold-out labels, search results, adds, sold-out refusal, quantity update, ₦18,000 subtotal with free delivery, reference KFO-1001, clean exit |
| Independent code review (`quality-reviewer` agent) | No must-fix issues; the suggestions applied were wording, a defensive list copy, and new tests for five previously untested paths |
| GitHub | `gh repo view` → `visibility: PUBLIC`; unauthenticated `curl` of the repo URL → HTTP 200 |

### Test scenario coverage

| Scenario | Expected | How verified | Result |
|---|---|---|---|
| Start application | Welcome screen and main menu | Automated (`FoodOrderingAppTest`) and manual | Pass |
| Browse menu | All items appear, sold-out ones labelled | Automated | Pass |
| Browse category | Only matching items | Automated (Drinks shows water, no Jollof) | Pass |
| Search existing food | Matching result | Automated and manual | Pass |
| Search nonexistent / empty | Helpful message | Automated | Pass |
| Add food to cart | Item appears in cart | Automated (`CartTest`, `FoodOrderingAppTest`) | Pass |
| Add multiple quantities | Correct quantity stored; repeat adds merge | Automated | Pass |
| Update quantity | Cart shows the new quantity | Automated and manual | Pass |
| Remove item | Item disappears | Automated | Pass |
| Clear cart | Cart becomes empty | Automated | Pass |
| View empty cart | Helpful empty-cart message | Automated | Pass |
| Checkout with empty cart | Prevented | Automated | Pass |
| Checkout with items | Order summary appears | Automated and manual | Pass |
| Confirm checkout | Confirmation with reference KFO-1001 | Automated and manual | Pass |
| Cancel checkout (n, or an answer other than y/n) | No order; cart kept | Automated | Pass |
| Invalid menu input (`abc`, `42`) | Error message; app continues | Automated | Pass |
| Invalid quantity (`0`, `-1`, `two`, over the limit) | Rejected | Automated | Pass |
| Unavailable food | Cannot be added | Automated and manual | Pass |
| Exit (option 10, or end of input) | Closes normally | Automated | Pass |

## Known limitations (also listed in README Future Work)

- Orders and order numbers are not saved; references restart at KFO-1001 on each run.
- After an invalid answer inside an action (for example a bad quantity), the app returns to the main menu instead of asking again. It explains the error and never crashes.
- Interactive input typed into a real terminal was not automated. It was verified by piping scripted input into the app; you should also try it yourself in the VS Code terminal.
