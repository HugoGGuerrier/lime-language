package com.limelanguage.ast.expressions.literals

import com.limelanguage.SourceSection
import com.limelanguage.analysis.AnalysisUnit
import com.limelanguage.ast.expressions.Expr

/** This class represents a boolean literal in the Lime source code. */
class BooleanLiteral(
    unit: AnalysisUnit,
    location: SourceSection,
    val value: Boolean,
) : Expr(unit, location)
