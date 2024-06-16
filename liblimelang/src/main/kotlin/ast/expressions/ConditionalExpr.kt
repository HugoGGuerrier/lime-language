package com.limelanguage.ast.expressions

import com.limelanguage.SourceSection
import com.limelanguage.analysis.AnalysisUnit
import com.limelanguage.ast.Child

/** This class represents a conditional expression in the Lime language. */
class ConditionalExpr(
    unit: AnalysisUnit,
    location: SourceSection,
    @Child val condition: Expr,
    @Child val thenExpr: Expr,
    @Child val elseExpr: Expr?,
) : Expr(unit, location)
