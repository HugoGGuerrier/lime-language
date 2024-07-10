package com.limelanguage.ast.expressions.literals

import com.limelanguage.SourceSection
import com.limelanguage.analysis.AnalysisUnit
import com.limelanguage.ast.expressions.Expr

/** This node represents a unit literal value in the Lime language. */
class UnitLiteral(
    unit: AnalysisUnit,
    location: SourceSection,
) : Expr(unit, location)
