package com.limelanguage.ast.expressions

import com.limelanguage.SourceSection
import com.limelanguage.analysis.AnalysisUnit
import com.limelanguage.ast.Child
import com.limelanguage.ast.Identifier
import com.limelanguage.ast.LimeNode

/** This class represents a variable affectation in the Lime language. */
class VarAffect(
    unit: AnalysisUnit,
    location: SourceSection,
    @Child(0) val name: Identifier,
    @Child(1) val value: Expr,
) : LimeNode(unit, location)
