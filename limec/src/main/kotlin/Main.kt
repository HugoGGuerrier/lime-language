package com.limelanguage

import com.limelanguage.analysis.AnalysisContext

fun main() {
    val context = AnalysisContext()
    val unit =
        context.analyseBuffer(
            "test",
            """
            const x = (
            """.trimIndent(),
        )
    if (unit.parsingDiagnostics.isNotEmpty()) {
        println(unit.parsingDiagnostics.map { it.message })
    }
    println(unit.root?.treeString())
}
