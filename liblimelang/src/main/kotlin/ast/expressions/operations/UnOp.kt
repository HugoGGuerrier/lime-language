package com.limelanguage.ast.expressions.operations

import com.limelanguage.SourceSection
import com.limelanguage.analysis.AnalysisUnit
import com.limelanguage.ast.Child
import com.limelanguage.ast.expressions.Expr
import com.limelanguage.ast.operators.Operator

/** This node represents the base for unary operations in the Lime language, with a [value] and an [op]. */
abstract class UnOp(
    unit: AnalysisUnit,
    location: SourceSection,
    @Child(0) val op: Operator?,
    @Child(1) val value: Expr?,
) : Expr(unit, location)
