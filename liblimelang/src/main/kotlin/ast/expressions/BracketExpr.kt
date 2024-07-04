package com.limelanguage.ast.expressions

import com.limelanguage.SourceSection
import com.limelanguage.analysis.AnalysisUnit
import com.limelanguage.ast.Child

/**
 * This class represents a bracketed expression in the Lime language, it is stored in the AST to keep this parsing
 * information.
 */
class BracketExpr(
    unit: AnalysisUnit,
    location: SourceSection,
    @Child val expr: Expr?,
) : Expr(unit, location)
