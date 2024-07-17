package com.limelanguage.analysis

import com.limelanguage.Source
import com.limelanguage.SourceLocation
import com.limelanguage.SourceSection
import com.limelanguage.ast.Identifier
import com.limelanguage.ast.Module
import com.limelanguage.ast.declarations.TypeDecl
import com.limelanguage.ast.types.ScalarType

/** This class contains the Lime prelude. It is a collection of synthetic declaration usable in every Lime file. */
class Prelude {
    companion object {
        // ----- Properties -----

        /** Dummy source for the prelude. */
        val preludeSource: Source = Source("<prelude>", "")

        // ----- Methods -----

        /** Get the prelude analysis unit for the given analysis [context]. */
        internal fun preludeUnit(context: AnalysisContext): AnalysisUnit {
            // Create the prelude analysis unit
            val res = SyntheticAnalysisUnit(context, preludeSource)
            val loc = SourceSection(preludeSource, SourceLocation.FIRST, SourceLocation.FIRST)

            // Util functions
            fun id(symbol: String) = Identifier(res, loc, symbol)

            fun scalarDecl(
                name: String,
                size: Int,
            ) = TypeDecl(res, loc, id(name), ScalarType(res, loc, size))

            // Create the prelude root node and populate it
            val module = Module(res, loc)
            module.children.add(scalarDecl("unit", 0))
            module.children.add(scalarDecl("bool", 1))
            module.children.add(scalarDecl("int", 4))

            // Return the prelude analysis unit
            module.populateLexicalEnv(LexicalEnvironment(null))
            res.rootNode = module
            return res
        }
    }
}
