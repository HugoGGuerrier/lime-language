package com.limelanguage.ast.types

import com.limelanguage.SourceSection
import com.limelanguage.analysis.AnalysisUnit
import com.limelanguage.ast.LimeNode

/** This node is the base of all Lime type expressions. */
abstract class TypeExpr(
    unit: AnalysisUnit,
    location: SourceSection,
) : LimeNode(unit, location)
