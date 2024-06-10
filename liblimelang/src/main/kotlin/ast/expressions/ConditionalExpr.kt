package com.limelanguage.ast.expressions

import com.limelanguage.SourceSection
import com.limelanguage.analysis.AnalysisUnit

/** This class represents a conditional expression in the Lime language. */
class ConditionalExpr(
    unit: AnalysisUnit,
    location: SourceSection,
    val condition: Expr,
    val thenExpr: Expr,
    val elseExpr: Expr?,
) : Expr(unit, location)
