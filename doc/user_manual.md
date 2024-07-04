# Lime user manual

## Comments

Lime is a simple language for simple people, no need to make things shitty: You
can comment a line with `//` and comment a whole section with `/* ... */`.
Example:

```lime
/** My function does something */
fun do_something() { ... }

// No need to comment this...
fun main() {
    do_something()
}
```

## The module system

Every lime source file defines a "module" named after the file name. It means
that `main.lime` defines the `main` module. A module is defined by a **top-level
construct**, either called a namespace.

## The top-level module

A top-level can contain constant and function declarations.

```lime
const a_string = "Hello!"
const an_int = 42

fun say_hello() {
    println(a_string)
}

fun main() {
    say_hello()
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

## Symbol declarations

There are several ways to declare new symbols in lime. All those constructions
are considered as expression of the `unit` value, however, since they
represent a more imperative paradigm, they are not documented with other
expressions.

### Function declaration

You can declare a function with this syntax to add a function in the current
lexical environment. You may also declare the function's parameters and their
types, and the function return type (please note that function parameters are
immutable):

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

You can also provide default value for function parameters, this way, users can
call your function without value for this parameter.

```lime
fun add (x: int, y: int = 0) {
    x + y
}

fun main() {
    add(1, 2);
    add(1)
}
```

As you can see, there is no `return` keyword in lime, this is because the block
construct after a function declaration is an expression (see the 
**Block-expression** section). Thus, the result of the function is the value of
this expression.

**Note:** In the future, the type annotations for parameters will be optional.

### Constant declaration

A constant value is declared through the `const` keyword. This value cannot
be mutated during the execution, even internally. You can declare a constant
in all lexical environment, including a top-level one.

```lime
const a = [1, 2, 3]
const b = "First"

fun main() {
    const lst = a;
    lst[0] = 50; // Will raise an error at compilation
    b = "Second"; // Will also raise an error
}
```

You can add an explicit type annotation to the constant declaration:

```lime
const x: int = 80
const str: string = "Hello"
```

### Variable declaration

You can declare a variable in any lexical environment except for the top-level
one. A variable can be mutated and its value can be changed after its
declaration. You can declare a new variable with the `var` keyword:

```lime
fun main() {
    var x = 40;
    vart= y = 2;
}
```

As constant declaration, you may add a type annotation to a variable
declaration:

```lime
fun main() {
    var x: int = 40;
    var y: string = "Hello";
}
```

Unlike constants, variables are mutable, so it is possible to change their
value after their declaration. It is also possible to declare a variable 
without any value and defer its initialization. In that case, you **MUST** 
provide a type annotation to the variable.

```lime
fun main {
    var x: string;
    var y = "Hello ";
    
    y = x + y;
    
    print(y);
}
```

## Expressions

In the Lime language quite all syntax constructs are expressions then can be
typed and have a value.

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

Some constructs in block expressions don't need to end with a semicolon:
 - Function declarations
 - Nested block expressions
 - Conditional expressions (`if ... else ...`)

### Conditional expression

You can express a conditional expression with the `if` and `else` keywords.
Additionally being an expression, the `if/else` construction can be used to
control the execution flow of your program.
Example:

```lime
fun main() {
    var x = if my_condition { 1 } else { 2 };
    if x == 0 {
        println("Hello!");
    } else {
        println("World!");
    }
    println("The end");
}
```

### Function calls

You can call a function as in any other language, with some parentheses. A
function call is an expression and its type will be the one of the called
function result.

```lime
fun add(x: int, y: int) { x + y }

// Call to the "add" function
const added = add(40, 2)
```

To make long calls more readable, you can use the named arguments format.

```lime
const value = long_call(
    param_1=true
    param_2=54
    ...
    param_n="finally..."
)
```

In Lime, functions are first class citizens, so you can use the call syntax
on all expressions (this expression has to be a callable type for the code to
be valid).

```lime
// This function returns the "add" function
fun get_add() {
    fun add(x: int, y: int) { x + y }
    add
}

// Call to the "add" function
const added = get_add()(40, 2)
```


**Note:** Due to current limitations, the named argument format is not
available when the callee part of a function call is not a name directly
referring to a function.

```lime
fun add(x: int, y: int) { x + y }

fun get_add() { add }

const added_1 = add(x=1, y=2)        // Valid code
const added_2 = get_add()(x=1, y=2)  // Will fail at compilation! 
```

### Bracketed expression

You can surround any expression with brackets to increase its precedence.
Example:

### Literals

Lime allows you to express literal values of many type. While this list will
change in the future, here are the possible literals:
- Unit literal: `()` - typed as `unit`
- Boolean literal: `true` or `false` - typed as `bool`
- Integer literal: `1`, `42` or `-90` - typed as `int`
- Symbolic literal: `my_var`, `my_const` or `my_fun` - typed following the
  bounded value.

```lime
const x = (1 - 1) * (2 + 2)
// Is not the same as
const y = 1 - 1 * 2 + 2
```

## Lexical environments

A lexical environment is a set of bindings associating symbols to their values.
Symbols declared in a lexical environment are only visible inside this one or
its children.
Here are the language constructs which introduce a new lexical environment:
 - Top-level
 - Function declaration
 - Block expression

## Features roadmap

 - Type declarations at top-level
 - Function parameters type inference
