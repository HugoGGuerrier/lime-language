# Lime user manual

## The module system

Every lime source file defines a "module" named after the file name. It means
that `main.lime` defines the `main` module. A module is defined by a **top-level
construct**.

## The top-level

A top-level can contain constant and function declarations.

```lime
const a_string = "Hello!"
const an_int = 42

fun say_hello() {
    println(a_string);
}

fun main() {
    say_hello();
}
```

This part of the language is purely declarative, it means that declaration
order has no effect on the module semantics and function can be mutually
recursive.
However, there is a restriction in constant declarations: You cannot declare a
constant which depends on another constant declared after:

```lime
// This is valid
const a = "Hello"
const b = "world"
const c = a + b

// This is not valid!
const z = x + y
const x = 40
const y = 2
```

## Constant declaration

A constant value is declared through the `const` keyword. This value is
initialized when the program starts and cannot be mutated during the execution.
Please note that this value is internally immutable too:

```lime
const a = [1, 2, 3]
const b = "First"

fun main() {
    a[0] = 50; // Will raise an error at compilation
    b = "Second"; // Will also raise an error
}
```

## Function declaration

You can declare a function with this syntax to add a function in the current
lexical environment. You may also declare the function's parameters and their
types, and the function return type:

```lime
fun add(x: int, y: int) -> int {
    x + y
}
```

Note that the return type annotation is optional and the language is able to
infer this information. So the `add` function could be changed into:

```lime
fun add(x: int, y: int) {
    x + y
}
```

As you can see, there is no `return` keyword in lime, this is because all
language constructs are expressions, and the body of a function is just an
arbitrary expression. You could then write:

```lime
fun add (x: int, y: int) x + y
```

**Note:** In the future, the type annotations for parameters will be optional.

## Expressions

In the Lime language all semantic constructs are expressions then can be typed.

### Block-Expression

The block expression is a special kind of expression. It is a collection of
other expressions separated by semicolons, and its value is the last expression
of the block. If the block expression ends with a semicolon then the value of
the block will be the unit value. For example:

```lime
// The value of x will be "42"
const x = {
    println("Hello");
    println("World");
    42
}

// The value of y will be the unit value because of the last semicolon
const y = {
    println("Hello world");
    42;
}
```

## Lexical environments

A lexical environment is a set of bindings associating symbols to their values.
Some lexical constructs introduce a new lexical environment such as:
 - Top-level
 - Function declaration
 - Block expression

## Features roadmap

 - Type declarations at top-level
 - 
