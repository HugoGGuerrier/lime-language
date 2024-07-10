package com.limelanguage.ast.types

import com.limelanguage.SourceSection
import com.limelanguage.analysis.AnalysisUnit
import com.limelanguage.ast.Child

/** This node represents a type expression designating a functional type, with its [paramTypes] and [returnType]. */
class FunType(
    unit: AnalysisUnit,
    location: SourceSection,
    @Child(0) val paramTypes: TypeExprs?,
    @Child(1) val returnType: TypeExpr?,
) : TypeExpr(unit, location)
