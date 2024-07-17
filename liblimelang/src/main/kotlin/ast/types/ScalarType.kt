package com.limelanguage.ast.types

import com.limelanguage.SourceSection
import com.limelanguage.analysis.AnalysisUnit
import com.limelanguage.ast.Child

/**
 * This node represents an expression of a scalar type. It cannot be created by the user through a special syntax, for
 * now it is only synthesised by the Lime prelude.
 * Its node describe the type by its [size], expressed in bytes.
 */
class ScalarType(
    unit: AnalysisUnit,
    location: SourceSection,
    @Child val size: Int,
) : TypeExpr(unit, location)
