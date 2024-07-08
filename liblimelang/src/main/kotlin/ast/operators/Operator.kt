package com.limelanguage.ast.operators

import com.limelanguage.SourceSection
import com.limelanguage.analysis.AnalysisUnit
import com.limelanguage.ast.LimeNode

/**
 * This class is the base of all operator nodes. Those are special nodes in the Lime AST, used to represent infix
 * operations such as "+", "-" or "&&".
 */
abstract class Operator(
    unit: AnalysisUnit,
    location: SourceSection,
) : LimeNode(unit, location)
