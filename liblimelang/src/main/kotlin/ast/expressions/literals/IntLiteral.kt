package com.limelanguage.ast.expressions.literals

import com.limelanguage.SourceSection
import com.limelanguage.analysis.AnalysisUnit
import com.limelanguage.ast.Child
import com.limelanguage.ast.expressions.Expr
import java.math.BigInteger

/** This node represents an arbitrary integer literal Lime sources. */
class IntLiteral(
    unit: AnalysisUnit,
    location: SourceSection,
    @Child val value: BigInteger,
) : Expr(unit, location)
