package com.limelanguage.ast.expressions.call

import com.limelanguage.SourceSection
import com.limelanguage.analysis.AnalysisUnit
import com.limelanguage.ast.LimeNode
import com.limelanguage.ast.expressions.Expr

/**
 * This class represents an argument in the Lime language. It can be a function argument, for
 * example.
 */
class Arg(
    unit: AnalysisUnit,
    location: SourceSection,
    val value: Expr,
) : LimeNode(unit, location)
