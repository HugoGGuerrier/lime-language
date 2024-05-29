package com.limelanguage.ast

import com.limelanguage.SourceSection
import com.limelanguage.analysis.AnalysisUnit

/**
 * This class represents the base of all list of nodes which may appear in the Lime AST. For
 * example, it is used to represent a parameter list in a function declaration.
 */
abstract class LimeListNode<T>(
    unit: AnalysisUnit,
    location: SourceSection,
    val children: MutableList<T> = ArrayList(),
) : LimeNode(unit, location)
