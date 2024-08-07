package com.limelanguage.parser.error

import com.limelanguage.Diagnostic
import com.limelanguage.SourceLocation
import com.limelanguage.SourceSection
import com.limelanguage.analysis.AnalysisUnit
import com.limelanguage.parser.LimeParser
import com.limelanguage.parser.fromToken
import org.antlr.v4.kotlinruntime.DefaultErrorStrategy
import org.antlr.v4.kotlinruntime.FailedPredicateException
import org.antlr.v4.kotlinruntime.InputMismatchException
import org.antlr.v4.kotlinruntime.NoViableAltException
import org.antlr.v4.kotlinruntime.Parser
import org.antlr.v4.kotlinruntime.Token
import org.antlr.v4.kotlinruntime.Vocabulary

class ParsingErrorStrategy(val unit: AnalysisUnit) : DefaultErrorStrategy() {
    companion object {
        /** Get the displaying name of a token, it may be null. */
        private fun tokenName(
            token: Token?,
            vocabulary: Vocabulary,
        ): String =
            when (token?.type) {
                null -> "<NO_TOKEN>"
                else -> tokenName(token.type, vocabulary)
            }

        /** Get the displaying name of a token type, this function is useful to handle custom token names. */
        private fun tokenName(
            type: Int,
            vocabulary: Vocabulary,
        ): String =
            when (type) {
                LimeParser.Tokens.EOF -> "<EOF>"
                LimeParser.Tokens.INT -> "<INT_LITERAL>"
                LimeParser.Tokens.ID -> "<IDENTIFIER>"
                else -> vocabulary.getDisplayName(type)
            }
    }

    // ----- Override methods -----

    override fun reportInputMismatch(
        recognizer: Parser,
        e: InputMismatchException,
    ) {
        val current = e.offendingToken
        val expecting =
            e.expectedTokens!!.toSet().joinToString(prefix = "{ ", postfix = " }") {
                tokenName(it, recognizer.vocabulary)
            }
        unit.addParsingDiagnostic(
            Diagnostic(
                "Unexpected input: ${tokenName(current, recognizer.vocabulary)}; expecting $expecting",
                location =
                    if (current != null) {
                        SourceSection.fromToken(unit.source, current)
                    } else {
                        SourceSection(unit.source, SourceLocation.FIRST, SourceLocation.FIRST)
                    },
            ),
        )
    }

    override fun reportNoViableAlternative(
        recognizer: Parser,
        e: NoViableAltException,
    ) {
        val current = e.offendingToken!!
        unit.addParsingDiagnostic(
            Diagnostic(
                "Cannot process token: ${tokenName(current, recognizer.vocabulary)}; stop parsing",
                location = SourceSection.fromToken(unit.source, current),
            ),
        )
    }

    override fun reportFailedPredicate(
        recognizer: Parser,
        e: FailedPredicateException,
    ) {
        val ruleName = recognizer.ruleNames[recognizer.context!!.ruleIndex]
        val current = e.offendingToken!!
        unit.addParsingDiagnostic(
            Diagnostic(
                "Failed predicate: $ruleName ${e.message}",
                location = SourceSection.fromToken(unit.source, current),
            ),
        )
    }

    override fun reportMissingToken(recognizer: Parser) {
        val missing = recognizer.expectedTokens.toSet().joinToString { tokenName(it, recognizer.vocabulary) }
        val current = recognizer.currentToken!!
        unit.addParsingDiagnostic(
            Diagnostic(
                "Missing $missing (got ${tokenName(current, recognizer.vocabulary)} instead)",
                location = SourceSection.fromToken(unit.source, current),
            ),
        )
    }

    override fun reportUnwantedToken(recognizer: Parser) {
        val expecting =
            recognizer.expectedTokens.toSet().joinToString(prefix = "{ ", postfix = " }") {
                tokenName(it, recognizer.vocabulary)
            }
        val unwanted = recognizer.currentToken!!
        unit.addParsingDiagnostic(
            Diagnostic(
                "Unwanted token: ${tokenName(unwanted, recognizer.vocabulary)}; expecting $expecting",
                location = SourceSection.fromToken(unit.source, unwanted),
            ),
        )
    }
}
