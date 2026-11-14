# Overview

As a software engineer, I wanted to learn Kotlin by building something complete rather than isolated snippets: a program that takes messy user input, keeps state that changes over time, and still produces exact results. This project let me practise Kotlin's core syntax, its type system and null safety, and its collection library, and organise the code into small classes with clear responsibilities.

KotlinFoodOrdering is a command-line food ordering application. Customers can browse a menu of Nigerian dishes and drinks (rice meals, shawarma, small chops, grills and drinks), browse by category, search by name, add items to a cart, change quantities, remove items, clear the cart, and check out. Checkout shows an order summary with each line total, the subtotal, the delivery fee (₦1,500, free on orders of ₦15,000 or more) and the final total. When the customer confirms, the app gives the order a reference such as `KFO-1001`. Sold-out items cannot be added, and invalid input (letters where a number is expected, unknown food IDs, zero or negative quantities, empty searches) is explained to the user instead of crashing the program. Prices are sample data in naira, and checkout is simulated: no payment is taken.

My purpose in writing this software was to learn how Kotlin expresses common programming ideas: immutable `val` and mutable `var` variables, `if` and `when` used as expressions, `for` and `while` loops, functions with default parameters, classes, data classes and enum classes, and read-only versus mutable lists and maps. I also wanted to learn how to build, run and test a Kotlin/JVM project with Gradle.

[Software Demo Video](https://youtu.be/cbwKfRzuZGs)

# Development Environment

Tools used to develop the software:

- macOS 27 on an Apple M1 MacBook Air
- Visual Studio Code 1.145, with Claude Code as an AI programming assistant
- OpenJDK 21.0.12.1 (Homebrew `openjdk@21`)
- Kotlin 2.4.21, targeting the JVM (Kotlin Gradle plugin `org.jetbrains.kotlin.jvm`)
- Gradle 9.8.1 through the Gradle wrapper (`./gradlew`), with the `application` plugin to run the program
- Git and GitHub for version control

The program is written in **Kotlin**, a statically typed language that runs on the Java Virtual Machine. Kotlin's type system separates nullable types (`String?`) from non-null ones, so missing values have to be handled explicitly. It also has concise syntax for data-holding classes and a large standard collection library.

Libraries:

- **Kotlin standard library**: collections (`listOf`, `mutableMapOf`, `filter`, `groupBy`, `sumOf`), string formatting and input/output. No third-party libraries are used in the application.
- **kotlin.test with JUnit 5** (tests only): 35 automated tests cover the menu, cart, order and currency logic, plus end-to-end runs of the command-line interface with scripted input.

To build, test and run (JDK 21 must be installed; Gradle downloads Kotlin automatically):

```bash
./gradlew build        # compile and run all tests
./gradlew run -q       # start the application
```

Source code is in `src/main/kotlin/foodordering/` and tests are in `src/test/kotlin/foodordering/`.

# Useful Websites

- [Kotlin Documentation (home)](https://kotlinlang.org/docs/home.html)
- [Kotlin: Basic syntax (variables, functions, loops)](https://kotlinlang.org/docs/basic-syntax.html)
- [Kotlin: Conditions and loops](https://kotlinlang.org/docs/control-flow.html)
- [Kotlin: Functions](https://kotlinlang.org/docs/functions.html)
- [Kotlin: Classes](https://kotlinlang.org/docs/classes.html)
- [Kotlin: Data classes](https://kotlinlang.org/docs/data-classes.html)
- [Kotlin: Enum classes](https://kotlinlang.org/docs/enum-classes.html)
- [Kotlin: Collections overview](https://kotlinlang.org/docs/collections-overview.html)
- [Kotlin: Null safety](https://kotlinlang.org/docs/null-safety.html)
- [Kotlin: Read standard input](https://kotlinlang.org/docs/read-standard-input.html)
- [Kotlin: Documenting code with KDoc](https://kotlinlang.org/docs/kotlin-doc.html)
- [Kotlin: Coding conventions](https://kotlinlang.org/docs/coding-conventions.html)
- [Kotlin: Gradle](https://kotlinlang.org/docs/gradle.html)
- [kotlin.test API reference](https://kotlinlang.org/api/core/kotlin-test/)
- [Gradle: The Application Plugin](https://docs.gradle.org/current/userguide/application_plugin.html)
- [Gradle: The Gradle Wrapper](https://docs.gradle.org/current/userguide/gradle_wrapper.html)

# Future Work

- Save orders to a file or database so order history and order numbers carry over between runs (references currently restart at `KFO-1001` each time the app starts).
- Load the menu from a file or database instead of the built-in sample list, so prices and availability can change without editing code.
- Add customer profiles and delivery address management.
- Improve search to match descriptions and categories, and add filters such as price range.
- Build a graphical or web interface on top of the same Menu, Cart and OrderProcessor classes.
- Add automated reports, such as daily sales and best-selling items.
