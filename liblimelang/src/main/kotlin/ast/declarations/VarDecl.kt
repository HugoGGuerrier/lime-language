package com.limelanguage.ast.declarations

import com.limelanguage.SourceSection
import com.limelanguage.analysis.AnalysisUnit
import com.limelanguage.ast.Identifier
import com.limelanguage.ast.expressions.Expr

/** This class represents a simple variable declaration in the Lime language. */
class VarDecl(
    unit: AnalysisUnit,
    location: SourceSection,
    name: Identifier,
    type: Identifier?,
    value: Expr?,
) : Decl(unit, location, name, type, value)
