package com.limelanguage.ast.expressions.literals

import com.limelanguage.SourceSection
import com.limelanguage.analysis.AnalysisUnit
import com.limelanguage.ast.Child
import com.limelanguage.ast.expressions.Expr

/** This node represents a boolean literal in the Lime source code. */
class BooleanLiteral(
    unit: AnalysisUnit,
    location: SourceSection,
    @Child val value: Boolean,
) : Expr(unit, location)
