package com.limelanguage.ast.declarations

import com.limelanguage.SourceSection
import com.limelanguage.analysis.AnalysisUnit
import com.limelanguage.ast.Child
import com.limelanguage.ast.Identifier
import com.limelanguage.ast.expressions.Expr
import java.util.Optional

/** This class represents a simple constant declaration in the Lime language. */
class ConstDecl(
    unit: AnalysisUnit,
    location: SourceSection,
    @Child(0) val name: Identifier?,
    @Child(1) val type: Optional<Identifier>,
    @Child(2) val value: Expr?,
) : Decl(unit, location)
