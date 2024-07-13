package com.limelanguage

import com.limelanguage.analysis.AnalysisContext

fun main() {
    val context = AnalysisContext()
    val unit =
        context.analyseBuffer(
            "test.lime",
            """
            fun f() { var x = x }
            """.trimIndent(),
        )
    println(unit.parsingDiagnostics)
    println(unit.lexEnvDiagnostics)
    println(unit.rootNode?.treeString())
    println(unit.rootNode?.nodeLexicalEnvironment)
}
