package com.limelanguage.ast

import com.limelanguage.SourceSection
import com.limelanguage.analysis.AnalysisUnit

/**
 * This class represents the base of all list of nodes which may appear in the Lime AST. For
 * example, it is used to represent a parameter list in a function declaration.
 */
abstract class LimeListNode<T : LimeNode>(
    unit: AnalysisUnit,
    location: SourceSection,
    val children: MutableList<T> = ArrayList(),
) : LimeNode(unit, location) {
    // ----- Methods -----

    override fun treeString(indentLevel: Int): String {
        val res = StringBuilder(this::class.simpleName).appendLine(" (list node)")
        for ((i, child) in children.withIndex()) {
            res.append(indentString(indentLevel + 1))
                .append("<$i>: ")
                .append(child.treeString(indentLevel + 1))
            if (i < children.size - 1) {
                res.appendLine()
            }
        }
        return res.toString()
    }
}
