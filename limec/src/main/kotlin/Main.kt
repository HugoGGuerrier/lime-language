package com.limelanguage

import com.limelanguage.analysis.AnalysisContext

fun main() {
    val context = AnalysisContext()
    val unit = context.analyseBuffer("test", "const x = {42}")
    if (unit.diagnostics.isNotEmpty()) {
        println(unit.diagnostics)
    }
    println(unit.root)
}
