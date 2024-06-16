package com.limelanguage.ast.expressions.block

import com.limelanguage.SourceSection
import com.limelanguage.analysis.AnalysisUnit
import com.limelanguage.ast.Child
import com.limelanguage.ast.expressions.Expr

/**
 * This class represents a block expression in the Lime language, this is a collection of
 * expressions.
 */
class BlockExpr(
    unit: AnalysisUnit,
    location: SourceSection,
    @Child val elems: BlockElems,
) : Expr(unit, location)
