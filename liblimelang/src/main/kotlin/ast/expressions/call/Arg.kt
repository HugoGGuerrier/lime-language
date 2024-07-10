package com.limelanguage.ast.expressions.call

import com.limelanguage.SourceSection
import com.limelanguage.analysis.AnalysisUnit
import com.limelanguage.ast.Child
import com.limelanguage.ast.Identifier
import com.limelanguage.ast.LimeNode
import com.limelanguage.ast.expressions.Expr
import java.util.Optional

/**
 * This node represents an argument in the Lime language. It can be a function argument, for
 * example.
 */
class Arg(
    unit: AnalysisUnit,
    location: SourceSection,
    @Child(0) val name: Optional<Identifier>,
    @Child(1) val value: Expr?,
) : LimeNode(unit, location)
