package com.limelanguage.parser

import com.limelanguage.SourceLocation
import com.limelanguage.SourceSection
import com.limelanguage.analysis.AnalysisUnit
import com.limelanguage.ast.Identifier
import com.limelanguage.ast.LimeNode
import com.limelanguage.ast.Module
import com.limelanguage.ast.declarations.ConstDecl
import com.limelanguage.ast.declarations.Decl
import com.limelanguage.ast.declarations.VarDecl
import com.limelanguage.ast.declarations.function.FunDecl
import com.limelanguage.ast.declarations.function.Param
import com.limelanguage.ast.declarations.function.ParamList
import com.limelanguage.ast.expressions.ConditionalExpr
import com.limelanguage.ast.expressions.Expr
import com.limelanguage.ast.expressions.VarAffect
import com.limelanguage.ast.expressions.block.BlockElems
import com.limelanguage.ast.expressions.block.BlockExpr
import com.limelanguage.ast.expressions.call.Arg
import com.limelanguage.ast.expressions.call.ArgList
import com.limelanguage.ast.expressions.call.FunCall
import com.limelanguage.ast.expressions.literals.BooleanLiteral
import com.limelanguage.ast.expressions.literals.IntLiteral
import com.limelanguage.ast.expressions.literals.SymbolLiteral
import com.limelanguage.ast.expressions.literals.UnitLiteral
import org.antlr.v4.kotlinruntime.ParserRuleContext
import org.antlr.v4.kotlinruntime.Token
import org.antlr.v4.kotlinruntime.ast.Position
import org.antlr.v4.kotlinruntime.tree.TerminalNode

/**
 * This visitor translates visited Lime syntactic construct into AST nodes. This visitor should be
 * instantiated for each analysis unit.
 *
 * @property unit The unit to translate nodes for.
 */
class ParsingVisitor(val unit: AnalysisUnit) : LimeBaseVisitor<LimeNode?>() {
    // ----- Visiting methods -----

    // --- File top-level

    override fun visitCompilationUnit(ctx: LimeParser.CompilationUnitContext): LimeNode? {
        return ctx.module_elems().accept(this)
    }

    // --- Module

    override fun visitModuleElems(ctx: LimeParser.ModuleElemsContext): LimeNode? {
        val res = Module(unit, loc(ctx))
        ctx.module_elem().forEach { res.children.add(it.accept(this) as Decl) }
        return res
    }

    // --- Declarations/Affectations

    override fun visitVarDecl(ctx: LimeParser.VarDeclContext): LimeNode? {
        return VarDecl(
            unit,
            loc(ctx),
            id(ctx.name)!!,
            id(ctx.type),
            ctx.value?.accept(this) as? Expr,
        )
    }

    override fun visitVarAffect(ctx: LimeParser.VarAffectContext): LimeNode? {
        return VarAffect(unit, loc(ctx), id(ctx.name)!!, ctx.value!!.accept(this) as Expr)
    }

    override fun visitConstDecl(ctx: LimeParser.ConstDeclContext): LimeNode? {
        return ConstDecl(
            unit,
            loc(ctx),
            id(ctx.name),
            id(ctx.type),
            ctx.value?.accept(this) as? Expr,
        )
    }

    override fun visitFunDecl(ctx: LimeParser.FunDeclContext): LimeNode? {
        return FunDecl(
            unit,
            loc(ctx),
            id(ctx.name)!!,
            ctx.params?.accept(this) as? ParamList,
            id(ctx.type),
            ctx.body!!.accept(this) as Expr,
        )
    }

    // --- Literals

    override fun visitUnitLiteral(ctx: LimeParser.UnitLiteralContext): LimeNode? {
        return UnitLiteral(unit, loc(ctx))
    }

    override fun visitIntLiteral(ctx: LimeParser.IntLiteralContext): LimeNode? {
        return IntLiteral(unit, loc(ctx), ctx.text.toBigInteger())
    }

    override fun visitSymbolLiteral(ctx: LimeParser.SymbolLiteralContext): LimeNode? {
        return SymbolLiteral(unit, loc(ctx), ctx.text)
    }

    override fun visitTrueBooleanLiteral(ctx: LimeParser.TrueBooleanLiteralContext): LimeNode? {
        return BooleanLiteral(unit, loc(ctx), true)
    }

    override fun visitFalseBooleanLiteral(ctx: LimeParser.FalseBooleanLiteralContext): LimeNode? {
        return BooleanLiteral(unit, loc(ctx), false)
    }

    // --- Bracketed expression

    override fun visitBracketExpr(ctx: LimeParser.BracketExprContext): LimeNode? {
        return ctx.inner!!.accept(this)
    }

    // --- Block expression

    override fun visitBlock(ctx: LimeParser.BlockContext): LimeNode? {
        return BlockExpr(unit, loc(ctx), ctx.elems!!.accept(this) as BlockElems)
    }

    override fun visitBlockElems(ctx: LimeParser.BlockElemsContext): LimeNode? {
        val res = BlockElems(unit, loc(ctx))
        ctx.children!!
            .filterIsInstance<ParserRuleContext>()
            .forEach { res.children.add(it.accept(this) as Expr) }
        val lastChild = ctx.getChild(ctx.childCount - 1)!!
        if (lastChild is TerminalNode && lastChild.symbol.type == LimeLexer.Tokens.SEMI_COLON) {
            res.children.add(UnitLiteral(unit, loc(lastChild.symbol)))
        }
        return res
    }

    // --- Function call

    override fun visitFunCallExpr(ctx: LimeParser.FunCallExprContext): LimeNode? {
        return FunCall(
            unit,
            loc(ctx),
            ctx.callee!!.accept(this) as Expr,
            ctx.args!!.accept(this) as ArgList,
        )
    }

    // --- Conditional expression

    override fun visitConditional(ctx: LimeParser.ConditionalContext): LimeNode? {
        return ConditionalExpr(
            unit,
            loc(ctx),
            ctx.condition!!.accept(this) as Expr,
            ctx.thenExpr!!.accept(this) as Expr,
            ctx.elseExpr?.accept(this) as? Expr,
        )
    }

    // --- Parameter

    override fun visitParams(ctx: LimeParser.ParamsContext): LimeNode? {
        val res = ParamList(unit, loc(ctx))
        ctx.parameter().forEach { res.children.add(it.accept(this) as Param) }
        return res
    }

    override fun visitParam(ctx: LimeParser.ParamContext): LimeNode? {
        return Param(unit, loc(ctx), id(ctx.name)!!, id(ctx.type)!!)
    }

    // --- Argument

    override fun visitArgs(ctx: LimeParser.ArgsContext): LimeNode? {
        val res = ArgList(unit, loc(ctx))
        ctx.argument().forEach { res.children.add(it.accept(this) as Arg) }
        return res
    }

    override fun visitArg(ctx: LimeParser.ArgContext): LimeNode? {
        return Arg(unit, loc(ctx), ctx.value!!.accept(this) as Expr)
    }

    // ----- Utils methods -----

    /** Create a source section from an ANTLR parsing context. */
    private fun loc(ctx: ParserRuleContext): SourceSection {
        val startTok = ctx.start
        val stopTok = ctx.stop
        return when {
            startTok is Token && stopTok is Token -> SourceSection.fromTokens(unit.source, startTok, stopTok)
            ctx.position is Position -> SourceSection.fromPosition(unit.source, ctx.position!!)
            else -> SourceSection(unit.source, SourceLocation.FIRST, SourceLocation.FIRST)
        }
    }

    /** Until function to create a source section from a lexed token. */
    private fun loc(token: Token): SourceSection = SourceSection.fromToken(unit.source, token)

    /** Synthetize a new identifier node from an ANTLR token. */
    private fun id(token: Token?): Identifier? {
        if (token == null) {
            return null
        } else {
            return Identifier(
                unit,
                SourceSection.fromToken(unit.source, token),
                token.text ?: "",
            )
        }
    }
}
