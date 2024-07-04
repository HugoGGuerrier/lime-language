package com.limelanguage.ast.declarations.function

import com.limelanguage.SourceSection
import com.limelanguage.analysis.AnalysisUnit
import com.limelanguage.ast.Child
import com.limelanguage.ast.Identifier
import com.limelanguage.ast.declarations.Decl
import com.limelanguage.ast.expressions.Expr
import java.util.Optional

/** This class represents a function parameter in the Lime language. */
class Param(
    unit: AnalysisUnit,
    location: SourceSection,
    @Child(0) val name: Identifier?,
    @Child(1) val type: Identifier?,
    @Child(2) val defaultValue: Optional<Expr>,
) : Decl(unit, location)
