package com.limelanguage.ast.declarations.function

import com.limelanguage.SourceSection
import com.limelanguage.analysis.AnalysisUnit
import com.limelanguage.ast.Identifier
import com.limelanguage.ast.declarations.Decl
import com.limelanguage.ast.expressions.Expr

/** This class represents a function declaration in the Lime language. */
class FunDecl(
    unit: AnalysisUnit,
    location: SourceSection,
    name: Identifier,
    val params: FunParamList,
    returnType: Identifier?,
    value: Expr,
) : Decl(unit, location, name, returnType, value)
