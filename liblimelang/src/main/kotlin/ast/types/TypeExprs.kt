package com.limelanguage.ast.types

import com.limelanguage.SourceSection
import com.limelanguage.analysis.AnalysisUnit
import com.limelanguage.ast.LimeListNode

/** This node is an abstraction of all type expression list. */
class TypeExprs(
    unit: AnalysisUnit,
    location: SourceSection,
) : LimeListNode<TypeExpr>(unit, location)
