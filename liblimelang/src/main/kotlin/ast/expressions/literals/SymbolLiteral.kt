package com.limelanguage.ast.expressions.literals

import com.limelanguage.SourceSection
import com.limelanguage.analysis.AnalysisUnit
import com.limelanguage.ast.Child
import com.limelanguage.ast.expressions.Expr

/** This class represents symbolic access in the Lime language. */
class SymbolLiteral(
    unit: AnalysisUnit,
    location: SourceSection,
    @Child val symbol: String,
) : Expr(unit, location)
