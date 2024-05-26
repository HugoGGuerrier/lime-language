package com.limelanguage.ast

import com.limelanguage.SourceSection
import com.limelanguage.analysis.AnalysisUnit

/**
 * This class represents the base of all list of nodes which may appear in the Lime AST. For example, it is used to
 * represent a parameter list of a function.
 */
open class LimeListNode(unit: AnalysisUnit, location: SourceSection) : LimeNode(unit, location)
