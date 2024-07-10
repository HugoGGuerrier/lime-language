package com.limelanguage.ast.expressions.block

import com.limelanguage.SourceSection
import com.limelanguage.analysis.AnalysisUnit
import com.limelanguage.ast.Child
import com.limelanguage.ast.expressions.Expr

/**
 * This node represents a block expression in the Lime language, this is a collection of
 * expressions.
 *
 * @property elems The list of expressions composing the block.
 * @property value The result of the block.
 */
class BlockExpr(
    unit: AnalysisUnit,
    location: SourceSection,
    @Child(0) val elems: BlockElems?,
    @Child(1) val value: Expr?,
) : Expr(unit, location)
