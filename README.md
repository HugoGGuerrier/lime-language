# 🍋‍🟩 Lime language

## The language

The Lime language is a general purpose, imperative with functional tastes,
statically typed language focused on readability, expressivity and genericity.
Its toolchain is designed to be frictionless for the user, simple to kickstart,
simple to develop in and simple to share.

Here is a simple example:

```lime
fun main() {
    println("Hello world!");
}
```

The main feature of the language is its type inference and implicit genericity.
For example:

```lime
// There is no parameter type annotations or return type, this function is a
// implicit generic function. It can take arguments of any type if there is a
// "+" operation defined on those
fun add(x, y) = { x + y }

fun main() {
    // Using the "add" function to add integers
    let x = add(1, 2);

    // Using the "add" function to concatenate strings
    let y = add("Hello", "World");

    // Will raise an error at compile time since there is no "+" operation
    // defined for integer and string
    let z = add(x, y);
}
```

**NOTE:** This feature is not available for now.

Lime hasn't the goal of being the most powerful language on the Earth, it would
always prefer user-friendly and simple concepts instead of elitist and
hyper-specific ones.

For more information about the language, please read the Lime documentation

TODO: Add a link to the doc

## The content of this repository

This repository contains the first implementation of the Lime language and its
toolchain before its bootstrapping process.
You can find in this folder:
 - The language frontend library, in the `liblimelang` directory. This is a
   simple yet complete frontend, used by other toolchain parts to parse and
   analyse Lime sources.
 - The Lime compiler, in the `limec` directory. A compiler which uses
   `liblimelang` to turn Lime sources to LLVM bitcode

## Build the toolchain

In order to build the Lime toolchain, you will need some pre-requirements:
 - An available Java Development Kit installation (17 or superior)

Every part of this project is written in Kotlin, using Gradle to handle the
build process. Thus, you can build the entire Lime toolchain by running:

```
./gradlew build
```

TODO: Produce a unique folder with all produced tools

## Test the toolchain

You can also run the Lime testsuite with the following command:

```
./gradlew test
```
