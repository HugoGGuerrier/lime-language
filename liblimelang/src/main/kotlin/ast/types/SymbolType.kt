package com.limelanguage.ast.types

import com.limelanguage.SourceSection
import com.limelanguage.analysis.AnalysisUnit
import com.limelanguage.ast.Child

/** This node represents a reference to an existing type by its name, represented by [symbol]. */
class SymbolType(
    unit: AnalysisUnit,
    location: SourceSection,
    @Child val symbol: String,
) : TypeExpr(unit, location)
