package com.limelanguage.ast.declarations

import com.limelanguage.SourceSection
import com.limelanguage.analysis.AnalysisUnit
import com.limelanguage.ast.expressions.Expr

/**
 * This node is the base for all declaration nodes. Those nodes are the ones who introduce new
 * symbols in the current lexical environment.
 * Note that all declarations are also expression whose value is unit.
 */
abstract class Decl(
    unit: AnalysisUnit,
    location: SourceSection,
) : Expr(unit, location)
