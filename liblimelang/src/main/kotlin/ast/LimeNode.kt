package com.limelanguage.ast

import com.limelanguage.SourceSection
import com.limelanguage.analysis.AnalysisUnit
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.memberProperties

/**
 * This class is the base of all lime nodes, it contains all common information and operations
 * require for all AST node types.
 *
 * @property unit The analysis unit which owns this node.
 * @property location The section in the Lime sources corresponding to this node.
 */
abstract class LimeNode(val unit: AnalysisUnit, val location: SourceSection) {
    companion object {
        fun indentString(level: Int): String = "|  ".repeat(level)
    }

    // ----- Methods -----

    /** Get the string representation of the Lime syntax tree from this node. */
    fun treeString(): String {
        return treeString(0)
    }

    open fun treeString(indentLevel: Int): String {
        /** Util internal function to get the string representation of a child field. */
        fun repr(v: Any?): String {
            return when (v) {
                null -> "None"
                is LimeNode -> v.treeString(indentLevel + 1)
                else -> v.toString()
            }
        }

        // Prepare the result and get the children to display
        val res = StringBuilder(this::class.simpleName)
        val children =
            this::class.memberProperties
                .filter { it.hasAnnotation<Child>() }
                .sortedBy { it.findAnnotation<Child>()!!.index }
        val childrenValue = children.map { c -> c.call(this) }

        // If the node hasn't any child, just return the return as it is
        if (children.isEmpty()) {
            return res.toString()
        }

        // If the node only has one child, display it on the same line. Else, display the whole tree
        if (children.size == 1 && childrenValue[0] !is LimeNode) {
            res.append(" { ${children[0].name}: ${repr(childrenValue[0])} }")
        } else if (children.size > 1 || childrenValue[0] is LimeNode) {
            res.appendLine()
            for ((i, child) in children.zip(childrenValue).withIndex()) {
                res.append(indentString(indentLevel + 1))
                    .append("${child.first.name}: ${repr(child.second)}")
                if (i < children.size - 1) {
                    res.appendLine()
                }
            }
        }

        // Return the computed string
        return res.toString()
    }
}

/** This annotation is used to introspect Lime AST trees and discriminate children members. */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.PROPERTY)
annotation class Child(val index: Int = 0)
