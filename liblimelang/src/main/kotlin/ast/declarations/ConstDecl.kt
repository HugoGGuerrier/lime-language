package com.limelanguage.ast.declarations

import com.limelanguage.SourceSection
import com.limelanguage.analysis.AnalysisUnit
import com.limelanguage.ast.Child
import com.limelanguage.ast.Identifier
import com.limelanguage.ast.expressions.Expr
import com.limelanguage.ast.types.TypeExpr
import java.util.Optional

/** This node represents a simple constant declaration in the Lime language. */
class ConstDecl(
    unit: AnalysisUnit,
    location: SourceSection,
    @Child(0) val name: Identifier?,
    @Child(1) val type: Optional<TypeExpr>,
    @Child(2) val value: Expr?,
) : Decl(unit, location) {
    // ----- Methods -----

    override fun envSpec() {
        populateChildren()
        if (name != null) insertOrDiag(name.text, name)
    }
}
