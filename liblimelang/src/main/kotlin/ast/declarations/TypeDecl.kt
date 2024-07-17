package com.limelanguage.ast.declarations

import com.limelanguage.SourceSection
import com.limelanguage.analysis.AnalysisUnit
import com.limelanguage.ast.Child
import com.limelanguage.ast.Identifier
import com.limelanguage.ast.types.TypeExpr

/** This node represents a declaration of a new type in Lime. */
class TypeDecl(
    unit: AnalysisUnit,
    location: SourceSection,
    @Child(0) val name: Identifier?,
    @Child(1) val expr: TypeExpr?,
) : Decl(unit, location) {
    // ----- Methods -----

    override fun envSpec() {
        populateChildren()
        if (name != null) insertOrDiag(name.text, name)
    }
}
