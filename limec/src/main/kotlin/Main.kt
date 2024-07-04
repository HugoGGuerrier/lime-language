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
    if (unit.diagnostics.isNotEmpty()) {
        println(unit.diagnostics.map { it.message })
    }
    println(unit.root?.treeString())
}
