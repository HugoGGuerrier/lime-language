package com.limelanguage.ast

import com.limelanguage.SourceSection
import com.limelanguage.analysis.AnalysisUnit
import com.limelanguage.ast.declarations.Decl

/**
 * This node represents a module declaration in the Lime language. A module is a collection of unique symbols bounded to
 * values and being accessible from other modules.
 */
class Module(
    unit: AnalysisUnit,
    location: SourceSection,
) : LimeListNode<Decl>(unit, location) {
    // ----- Methods -----

    override fun envSpec() {
        // A module doesn't open a new lexical environment, it will populate its given lexical environment
        // TODO: Handle the module in a declarative way
        populateChildren()
    }
}
