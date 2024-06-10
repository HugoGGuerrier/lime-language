package com.limelanguage.parser

import com.limelanguage.Diagnostic
import com.limelanguage.analysis.AnalysisUnit
import org.antlr.v4.kotlinruntime.DefaultErrorStrategy
import org.antlr.v4.kotlinruntime.FailedPredicateException
import org.antlr.v4.kotlinruntime.Parser
import org.antlr.v4.kotlinruntime.RecognitionException

class ErrorStrategy(val unit: AnalysisUnit) : DefaultErrorStrategy() {
    override fun reportError(
        recognizer: Parser,
        e: RecognitionException,
    ) {
        when (e) {
            is FailedPredicateException ->
                this.unit.addDiagnostic(
                    Diagnostic(
                        message = "Failed predicate during parsing: ${e.predicate}",
                    ),
                )
        }
        println(recognizer)
    }
}
