package com.limelanguage.analysis

import com.limelanguage.ast.LimeNode

/**
 * This class represents a lexical environment in the Lime language. It is an object holding lexical definitions in the
 * current scope, binding them o their declaration node. A lexical environment may have a [parent] for performing
 * recursive lookups.
 */
class LexicalEnvironment(
    val parent: LexicalEnvironment?,
) {
    init {
        parent?.children?.add(this)
    }

    // ----- Properties -----

    /** Bindings going from the symbols to their declaration node. */
    private val bindings: MutableMap<String, LimeNode> = HashMap()

    /** All children lexical environments. */
    private val children: MutableList<LexicalEnvironment> = ArrayList()

    // ----- Methods -----

    /**
     * Bind [symbol] to its declaration represented by [declNode].
     *
     * @throws SymbolAlreadyBoundedException If the given symbol is already present.
     */
    fun insert(
        symbol: String,
        declNode: LimeNode,
    ): LexicalEnvironment {
        if (bindings.containsKey(symbol)) {
            throw SymbolAlreadyBoundedException(symbol)
        }
        bindings[symbol] = declNode
        return this
    }

    /**
     * Lookup the [symbol] in the lexical environment and return it, if any. The [lookupMode] set whether the lookup
     * should recursively lookup in the parent lexical environment.
     */
    fun lookup(
        symbol: String,
        lookupMode: LookupMode = LookupMode.SIMPLE,
    ): LimeNode? {
        return bindings.getOrElse(symbol) {
            if (lookupMode == LookupMode.RECURSIVE) {
                parent?.lookup(symbol, LookupMode.RECURSIVE)
            } else {
                null
            }
        }
    }

    override fun toString(): String =
        StringBuilder("LexEnv(bindings=")
            .append(bindings)
            .append(", children=")
            .append(children)
            .append(")")
            .toString()
}

/** Exception class to signify that a symbol is already present in a lexical environment. */
class SymbolAlreadyBoundedException(
    symbol: String,
) : Exception("Symbol already bounded in this lexical environment: '$symbol'")

/** Possible modes for the lexical environment lookup method. */
enum class LookupMode {
    /** Lookup only in the current lexical environment. */
    SIMPLE,

    /** Lookup in the current lexical environment and recursively in its parents. */
    RECURSIVE,
}
