package com.limelanguage.ast.expressions.call

import com.limelanguage.SourceSection
import com.limelanguage.analysis.AnalysisUnit
import com.limelanguage.ast.Child
import com.limelanguage.ast.expressions.Expr

/**
 * This class represents a function call expression in the Lime language. This represents the value
 * resulting of the call of [callee] with [args]
 */
class FunCall(
    unit: AnalysisUnit,
    location: SourceSection,
    @Child val callee: Expr,
    @Child val args: ArgList,
) : Expr(unit, location)
