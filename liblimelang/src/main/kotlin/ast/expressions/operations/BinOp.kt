package com.limelanguage.ast.expressions.operations

import com.limelanguage.SourceSection
import com.limelanguage.analysis.AnalysisUnit
import com.limelanguage.ast.Child
import com.limelanguage.ast.expressions.Expr
import com.limelanguage.ast.operators.Operator

/**
 * This node represents the base for binary operations in the Lime language, with a [left] and [right] operands and an
 * [op] representing the computing to do in those values.
 */
abstract class BinOp(
    unit: AnalysisUnit,
    location: SourceSection,
    @Child(0) val left: Expr?,
    @Child(1) val op: Operator?,
    @Child(2) val right: Expr?,
) : Expr(unit, location)
