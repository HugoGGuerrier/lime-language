package com.limelanguage.ast.declarations

import com.limelanguage.SourceSection
import com.limelanguage.analysis.AnalysisUnit
import com.limelanguage.ast.Child
import com.limelanguage.ast.Identifier
import com.limelanguage.ast.expressions.Expr
import java.util.Optional

/** This class represents a simple variable declaration in the Lime language. */
class VarDecl(
    unit: AnalysisUnit,
    location: SourceSection,
    @Child(0) val name: Identifier?,
    @Child(1) val type: Optional<Identifier>,
    @Child(2) val value: Optional<Expr>?,
) : Decl(unit, location)
