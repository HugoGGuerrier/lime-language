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

/**
 * This visitor translates visited Lime syntactic construct into AST nodes. This visitor should be
 * instantiated for each analysis unit.
 *
 * @property unit The unit to translate nodes for.
 */
class ParsingVisitor(val unit: AnalysisUnit) : LimeBaseVisitor<LimeNode?>() {
    // ----- Utils methods -----

    /** Create a source section from an ANTLR parsing context. */
    private fun loc(ctx: ParserRuleContext): SourceSection =
        if (ctx.position != null) {
            SourceSection.fromPosition(this.unit.source, ctx.position!!)
        } else {
            SourceSection(
                this.unit.source,
                SourceLocation.FIRST,
                SourceLocation.FIRST,
            )
        }

    /** Until function to create a source section from a lexed token. */
    private fun loc(token: Token): SourceSection =
        SourceSection(
            this.unit.source,
            SourceLocation.fromPoint(token.startPoint()),
            SourceLocation.fromPoint(token.endPoint() ?: token.startPoint()),
        )

    /** Synthetize a new identifier node from an ANTLR token. */
    private fun id(token: Token?): Identifier? {
        if (token == null) {
            return null
        } else {
            val text = token.text!!
            val startPoint = token.startPoint()
            return Identifier(
                unit,
                SourceSection(
                    unit.source,
                    SourceLocation(startPoint.line, startPoint.column),
                    SourceLocation(startPoint.line, startPoint.column + text.length),
                ),
                text,
            )
        }
    }

    // ----- Visiting methods -----

    // --- File top-level

    override fun visitFileModule(ctx: LimeParser.FileModuleContext): LimeNode? {
        return ctx.module_elems().accept(this)
    }

    // --- Module

    override fun visitEmptyModuleElem(ctx: LimeParser.EmptyModuleElemContext): LimeNode? {
        return Module(unit, loc(ctx))
    }

    override fun visitSingleModuleElem(ctx: LimeParser.SingleModuleElemContext): LimeNode? {
        val res = Module(unit, loc(ctx))
        res.children.add(ctx.elem!!.accept(this) as Decl)
        return res
    }

    override fun visitMultipleModuleElem(ctx: LimeParser.MultipleModuleElemContext): LimeNode? {
        val res: Module = ctx.tail!!.accept(this) as Module
        res.children.add(0, ctx.head!!.accept(this) as Decl)
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
            ctx.params!!.accept(this) as ParamList,
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

    override fun visitEmptyBlockElem(ctx: LimeParser.EmptyBlockElemContext): LimeNode? {
        val res = BlockElems(unit, loc(ctx))
        res.children.add(UnitLiteral(unit, loc(ctx)))
        return res
    }

    override fun visitSingleBlockElem(ctx: LimeParser.SingleBlockElemContext): LimeNode? {
        val res = BlockElems(unit, loc(ctx))
        res.children.add(ctx.elem!!.accept(this) as Expr)

        // Un-sugar the implicit unit valued block
        if (ctx.SEMI_COLON() != null) {
            res.children.add(UnitLiteral(unit, loc(ctx.SEMI_COLON()!!.symbol)))
        }
        return res
    }

    override fun visitMultipleBlockElem(ctx: LimeParser.MultipleBlockElemContext): LimeNode? {
        val res = ctx.tail!!.accept(this) as BlockElems
        res.children.add(0, ctx.head!!.accept(this) as Expr)
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

    override fun visitEmptyArg(ctx: LimeParser.EmptyArgContext): LimeNode? {
        return ArgList(unit, loc(ctx))
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

    override fun visitEmptyParam(ctx: LimeParser.EmptyParamContext): LimeNode? {
        return ParamList(unit, loc(ctx))
    }

    override fun visitSingleParam(ctx: LimeParser.SingleParamContext): LimeNode? {
        val res = ParamList(unit, loc(ctx))
        res.children.add(ctx.param!!.accept(this) as Param)
        return res
    }

    override fun visitMultipleParam(ctx: LimeParser.MultipleParamContext): LimeNode? {
        val res = ctx.tail!!.accept(this) as ParamList
        res.children.add(0, ctx.head!!.accept(this) as Param)
        return res
    }

    override fun visitParam(ctx: LimeParser.ParamContext): LimeNode? {
        return Param(unit, loc(ctx), id(ctx.name)!!, id(ctx.type)!!)
    }

    // --- Argument

    override fun visitSingleArg(ctx: LimeParser.SingleArgContext): LimeNode? {
        val res = ArgList(unit, loc(ctx))
        res.children.add(ctx.arg!!.accept(this) as Arg)
        return res
    }

    override fun visitMultipleArg(ctx: LimeParser.MultipleArgContext): LimeNode? {
        val res = ctx.tail!!.accept(this) as ArgList
        res.children.add(0, ctx.head!!.accept(this) as Arg)
        return res
    }

    override fun visitArg(ctx: LimeParser.ArgContext): LimeNode? {
        return Arg(unit, loc(ctx), ctx.value!!.accept(this) as Expr)
    }
}
