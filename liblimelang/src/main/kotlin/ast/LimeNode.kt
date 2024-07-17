package com.limelanguage.ast

import com.limelanguage.Diagnostic
import com.limelanguage.DiagnosticHint
import com.limelanguage.DiagnosticKind
import com.limelanguage.SourceSection
import com.limelanguage.analysis.AnalysisUnit
import com.limelanguage.analysis.LexicalEnvironment
import com.limelanguage.analysis.LookupMode
import com.limelanguage.analysis.SymbolAlreadyBoundedException
import java.util.Optional
import kotlin.jvm.optionals.getOrDefault
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.memberProperties

/**
 * This node is the base of all lime nodes, it contains all common information and operations
 * require for all AST node types.
 *
 * AST nodes have children denoted by the [Child] annotation. All children are nullable to allow error recovery from the
 * Lime parser, so if a child is null, there was at least an error during the parsing. Some children are wrapped in the
 * [Optional] class, those can be empty even when there is no parsing error.
 *
 * @property unit The analysis unit which owns this node.
 * @property location The section in the Lime sources corresponding to this node.
 */
abstract class LimeNode(val unit: AnalysisUnit, val location: SourceSection) {
    // ----- Properties -----

    /** The lexical environment where the node resides. */
    lateinit var nodeLexicalEnvironment: LexicalEnvironment
        protected set

    /** The lexical environment opened by this node, where all its children resides. */
    var childrenLexicalEnvironment: LexicalEnvironment? = null
        protected set

    /** List of node's children, ordered by their index, paired with their name. */
    open val childrenWithName: List<ChildPair> by lazy {
        this::class.memberProperties
            .filter { it.hasAnnotation<Child>() }
            .sortedBy { it.findAnnotation<Child>()!!.index }
            .map { ChildPair(it.name, it.call(this)) }
    }

    /** List of children that are of [LimeNode] type, or of [Optional] type with a wrapped value. */
    open val childrenNodes: List<LimeNode> by lazy {
        val res = ArrayList<LimeNode>()
        for (child in childrenWithName) {
            when (child.value) {
                is LimeNode -> res.add(child.value)
                is Optional<*> -> child.value.ifPresent { res.add(it as LimeNode) }
            }
        }
        res
    }

    // ----- Methods -----

    // --- Lexical environment methods

    /**
     * Function to call to set the node's lexical environment to [lexicalEnvironment] and perform its custom logic on
     * this environment.
     */
    internal fun populateLexicalEnv(lexicalEnvironment: LexicalEnvironment) {
        nodeLexicalEnvironment = lexicalEnvironment
        envSpec()
    }

    /**
     * Internal function to perform logic on the node's lexical environment. It is called by [populateLexicalEnv] and
     * should be overridden by sub-classed nodes to specify their lexical environment handling.
     * It just recurse on all children by default.
     */
    protected open fun envSpec() {
        populateChildren()
    }

    /** Recurse on all node's children [populateLexicalEnv] method with the given [lexicalEnvironment]. */
    internal fun populateChildren(lexicalEnvironment: LexicalEnvironment = nodeLexicalEnvironment) {
        childrenNodes.forEach { it.populateLexicalEnv(lexicalEnvironment) }
    }

    /** Create a new lexical environment, child of [lexicalEnvironment] and set it as [childrenLexicalEnvironment]. */
    internal fun openEnv(lexicalEnvironment: LexicalEnvironment = nodeLexicalEnvironment) {
        childrenLexicalEnvironment = LexicalEnvironment(lexicalEnvironment)
    }

    /**
     * Insert the given [symbol] in [nodeLexicalEnvironment], bounded to the receiver node. If this symbol is already
     * present, add a diagnostic to the node's [unit] located at [diagLoc].
     */
    internal fun insertOrDiag(
        symbol: String,
        diagLoc: LimeNode = this,
        lexicalEnvironment: LexicalEnvironment = nodeLexicalEnvironment,
        then: (() -> Unit)? = null,
    ) {
        try {
            lexicalEnvironment.insert(symbol, this)
            if (then != null) then()
        } catch (e: SymbolAlreadyBoundedException) {
            val previousDecl = lexicalEnvironment.lookup(symbol)
            unit.addLexEnvDiagnostic(
                Diagnostic(
                    message = "This symbol already exists in the current scope",
                    location = diagLoc.location,
                    hints =
                        listOf(
                            DiagnosticHint("Previously declared here", previousDecl!!.location),
                        ),
                ),
            )
        }
    }

    /**
     * Get the declaration node bounded to [symbol] in [lexicalEnvironment] (using the recursive lookup mode). If this
     * symbol cannot be found a diagnostics is added to the [AnalysisUnit.lexEnvDiagnostics] list and null is returned.
     */
    internal fun getOrDiag(
        symbol: String,
        diagLoc: LimeNode = this,
        lexicalEnvironment: LexicalEnvironment = nodeLexicalEnvironment,
        then: ((LimeNode) -> Unit)? = null,
    ) {
        val res = lexicalEnvironment.lookup(symbol, LookupMode.RECURSIVE)
        if (res != null) {
            if (then != null) then(res)
        } else {
            unit.addLexEnvDiagnostic(
                Diagnostic(
                    message = "Cannot find this symbol in the current scope",
                    location = diagLoc.location,
                    kind = DiagnosticKind.ERROR,
                ),
            )
        }
    }

    // --- Display methods

    /** Get the string representation of the Lime syntax tree from this node. */
    fun treeString(): String {
        return treeString(0)
    }

    internal open fun treeString(indentLevel: Int): String {
        /** Util internal function to get the string representation of a child field. */
        fun repr(v: Any?): String {
            return when (v) {
                null -> "<PARSING_ERROR>"
                is LimeNode -> v.treeString(indentLevel + 1)
                is Optional<*> -> v.map { (it as LimeNode).treeString(indentLevel + 1) }.getOrDefault("None")
                else -> v.toString()
            }
        }

        // Prepare the result and get the children to display
        val res = StringBuilder(this::class.simpleName)

        // If the node hasn't any child, just return the return as it is
        if (childrenWithName.isEmpty()) {
            return res.toString()
        }

        // If the node only has one child, display it on the same line. Else, display the whole tree
        if (childrenWithName.size == 1 && childrenWithName[0].value !is LimeNode) {
            res.append(" { ${childrenWithName[0].name}: ${repr(childrenWithName[0].value)} }")
        } else if (childrenWithName.size > 1 || childrenWithName[0].value is LimeNode) {
            res.appendLine()
            for ((i, child) in childrenWithName.withIndex()) {
                res.append(indentString(indentLevel + 1))
                    .append("${child.name}: ${repr(child.value)}")
                if (i < childrenWithName.size - 1) {
                    res.appendLine()
                }
            }
        }

        // Return the computed string
        return res.toString()
    }

    /** Util function to indent a node representation in a tree string. */
    internal fun indentString(level: Int): String = "|  ".repeat(level)

    override fun toString(): String = "<${this::class.simpleName} | ${location.prettyString()}>"
}

/** This annotation is used to introspect Lime AST trees and discriminate children members. */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.PROPERTY)
annotation class Child(val index: Int = 0)

/** Data class to store a node's child with its name. */
data class ChildPair(val name: String, val value: Any?)
