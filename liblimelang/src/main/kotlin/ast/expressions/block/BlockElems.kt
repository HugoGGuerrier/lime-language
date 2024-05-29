package com.limelanguage.ast.expressions.block

import com.limelanguage.SourceSection
import com.limelanguage.analysis.AnalysisUnit
import com.limelanguage.ast.LimeListNode
import com.limelanguage.ast.expressions.Expr

/** This class represents a list of elements inside a Lime block expression. */
class BlockElems(
    unit: AnalysisUnit,
    location: SourceSection,
) : LimeListNode<Expr>(unit, location)
