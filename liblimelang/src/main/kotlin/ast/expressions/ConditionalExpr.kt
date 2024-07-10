package com.limelanguage.ast.expressions

import com.limelanguage.SourceSection
import com.limelanguage.analysis.AnalysisUnit
import com.limelanguage.ast.Child
import java.util.Optional

/** This node represents a conditional expression in the Lime language. */
class ConditionalExpr(
    unit: AnalysisUnit,
    location: SourceSection,
    @Child(0) val condition: Expr?,
    @Child(1) val thenExpr: Expr?,
    @Child(2) val elseExpr: Optional<Expr>,
) : Expr(unit, location)
