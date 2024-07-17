package com.limelanguage.parser.error

import com.limelanguage.Diagnostic
import com.limelanguage.SourceLocation
import com.limelanguage.SourceSection
import com.limelanguage.analysis.AnalysisUnit
import org.antlr.v4.kotlinruntime.BaseErrorListener
import org.antlr.v4.kotlinruntime.RecognitionException
import org.antlr.v4.kotlinruntime.Recognizer

/** This class is the error listener for the Lime lexer. */
class LexingErrorListener(val unit: AnalysisUnit) : BaseErrorListener() {
    override fun syntaxError(
        recognizer: Recognizer<*, *>,
        offendingSymbol: Any?,
        line: Int,
        charPositionInLine: Int,
        msg: String,
        e: RecognitionException?,
    ) {
        unit.addParsingDiagnostic(
            Diagnostic(
                msg[0].uppercase() + msg.substring(1..<msg.length),
                location =
                    SourceSection(
                        unit.source,
                        SourceLocation(line, charPositionInLine),
                        SourceLocation(line, charPositionInLine),
                    ),
            ),
        )
    }
}
