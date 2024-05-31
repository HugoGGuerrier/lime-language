package com.limelanguage.ast.expressions

import com.limelanguage.SourceSection
import com.limelanguage.analysis.AnalysisUnit
import com.limelanguage.ast.Identifier
import com.limelanguage.ast.LimeNode

/** This class represents a variable affectation in the Lime language. */
class VarAffect(
    unit: AnalysisUnit,
    location: SourceSection,
    val name: Identifier,
    val value: Expr,
) : LimeNode(unit, location)
