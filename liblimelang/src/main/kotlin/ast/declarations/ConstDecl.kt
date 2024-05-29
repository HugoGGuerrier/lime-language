package com.limelanguage.ast.declarations

import com.limelanguage.SourceSection
import com.limelanguage.analysis.AnalysisUnit
import com.limelanguage.ast.Identifier
import com.limelanguage.ast.expressions.Expr

/** This class represents a simple constant declaration in the Lime language. */
class ConstDecl(
    unit: AnalysisUnit,
    location: SourceSection,
    name: Identifier,
    type: Identifier?,
    value: Expr,
) : Decl(unit, location, name, type, value)
