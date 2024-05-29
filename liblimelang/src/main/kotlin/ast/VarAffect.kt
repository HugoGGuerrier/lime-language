package com.limelanguage.ast

import com.limelanguage.SourceSection
import com.limelanguage.analysis.AnalysisUnit
import com.limelanguage.ast.expressions.Expr

/** This class represents a variable affectation in the Lime language. */
class VarAffect(
    unit: AnalysisUnit,
    location: SourceSection,
    val name: Identifier,
    val value: Expr,
) : LimeNode(unit, location)
