package com.limelanguage.parser

import com.limelanguage.SourceLocation
import com.limelanguage.SourceSection
import com.limelanguage.analysis.AnalysisUnit
import com.limelanguage.ast.Identifier
import com.limelanguage.ast.LimeNode
import com.limelanguage.ast.Module
import com.limelanguage.ast.declarations.ConstDecl
import com.limelanguage.ast.declarations.Decl
import com.limelanguage.ast.declarations.VarAffect
import com.limelanguage.ast.declarations.VarDecl
import com.limelanguage.ast.declarations.function.FunDecl
import com.limelanguage.ast.declarations.function.Param
import com.limelanguage.ast.declarations.function.ParamList
import com.limelanguage.ast.expressions.BracketExpr
import com.limelanguage.ast.expressions.ConditionalExpr
import com.limelanguage.ast.expressions.Expr
import com.limelanguage.ast.expressions.block.BlockElems
import com.limelanguage.ast.expressions.block.BlockExpr
import com.limelanguage.ast.expressions.call.Arg
import com.limelanguage.ast.expressions.call.ArgList
import com.limelanguage.ast.expressions.call.FunCall
import com.limelanguage.ast.expressions.literals.BooleanLiteral
import com.limelanguage.ast.expressions.literals.IntLiteral
import com.limelanguage.ast.expressions.literals.SymbolLiteral
import com.limelanguage.ast.expressions.literals.UnitLiteral
import com.limelanguage.ast.expressions.operations.ArithBinOp
import com.limelanguage.ast.expressions.operations.ArithUnOp
import com.limelanguage.ast.expressions.operations.CompBinOp
import com.limelanguage.ast.expressions.operations.LogicBinOp
import com.limelanguage.ast.expressions.operations.LogicUnOp
import com.limelanguage.ast.operators.AndOp
import com.limelanguage.ast.operators.DivOp
import com.limelanguage.ast.operators.EqOp
import com.limelanguage.ast.operators.GeqOp
import com.limelanguage.ast.operators.GtOp
import com.limelanguage.ast.operators.LeqOp
import com.limelanguage.ast.operators.LtOp
import com.limelanguage.ast.operators.MinusOp
import com.limelanguage.ast.operators.MulOp
import com.limelanguage.ast.operators.NeqOp
import com.limelanguage.ast.operators.NotOp
import com.limelanguage.ast.operators.OrOp
import com.limelanguage.ast.operators.PlusOp
import org.antlr.v4.kotlinruntime.ParserRuleContext
import org.antlr.v4.kotlinruntime.Token
import org.antlr.v4.kotlinruntime.ast.Position
import java.util.Optional

/**
 * This visitor translates visited Lime syntactic construct into AST nodes. This visitor should be
 * instantiated for each analysis unit.
 *
 * @property unit The unit to translate nodes for.
 */
class ParsingVisitor(val unit: AnalysisUnit) : LimeSafeBaseVisitor<LimeNode>() {
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
            name = id(ctx.name),
            type = Optional.ofNullable(id(ctx.type)),
            value = Optional.ofNullable(ctx.value?.accept(this) as? Expr),
        )
    }

    override fun visitVarAffect(ctx: LimeParser.VarAffectContext): LimeNode? {
        return VarAffect(unit, loc(ctx), id(ctx.name), ctx.value?.accept(this) as? Expr)
    }

    override fun visitConstDecl(ctx: LimeParser.ConstDeclContext): LimeNode? {
        return ConstDecl(
            unit,
            loc(ctx),
            name = id(ctx.name),
            type = Optional.ofNullable(id(ctx.type)),
            value = ctx.value?.accept(this) as? Expr,
        )
    }

    override fun visitFunDecl(ctx: LimeParser.FunDeclContext): LimeNode? {
        return FunDecl(
            unit,
            loc(ctx),
            name = id(ctx.name),
            params = ctx.params?.accept(this) as? ParamList,
            returnType = Optional.ofNullable(id(ctx.type)),
            body = ctx.body?.accept(this) as? Expr,
        )
    }

    // --- Conditional expression

    override fun visitConditionalExpr(ctx: LimeParser.ConditionalExprContext): LimeNode? {
        return ConditionalExpr(
            unit,
            loc(ctx),
            condition = ctx.condition?.accept(this) as? Expr,
            thenExpr = ctx.thenExpr?.accept(this) as? Expr,
            elseExpr = Optional.ofNullable(ctx.elseExpr?.accept(this) as? Expr),
        )
    }

    // --- Block expression

    override fun visitBlock(ctx: LimeParser.BlockContext): LimeNode? {
        // Get the block elements
        val elems = BlockElems(unit, loc(ctx))
        ctx.block_elem().forEach { elems.children.add(it.accept(this) as Expr) }

        // Get the block value
        var value = ctx.value?.accept(this) as? Expr
        if (value == null) {
            value = UnitLiteral(unit, loc(ctx))
        }

        // Return the resulting block expression
        return BlockExpr(unit, loc(ctx), elems, value)
    }

    override fun visitBlockElem(ctx: LimeParser.BlockElemContext): LimeNode? {
        return if (ctx.belem != null) {
            ctx.belem!!.accept(this)
        } else if (ctx.uelem != null) {
            ctx.uelem!!.accept(this)
        } else {
            ctx.fdelem!!.accept(this)
        }
    }

    // --- Logical operations

    override fun visitNotExpr(ctx: LimeParser.NotExprContext): LimeNode? {
        return LogicUnOp(
            unit,
            loc(ctx),
            op = NotOp(unit, loc(ctx.NOT().symbol)),
            operand = ctx.operand?.accept(this) as? Expr,
        )
    }

    override fun visitAndExpr(ctx: LimeParser.AndExprContext): LimeNode? {
        return LogicBinOp(
            unit,
            loc(ctx),
            left = ctx.left?.accept(this) as? Expr,
            op = AndOp(unit, loc(ctx.AND().symbol)),
            right = ctx.right?.accept(this) as? Expr,
        )
    }

    override fun visitOrExpr(ctx: LimeParser.OrExprContext): LimeNode? {
        return LogicBinOp(
            unit,
            loc(ctx),
            left = ctx.left?.accept(this) as? Expr,
            op = OrOp(unit, loc(ctx.OR().symbol)),
            right = ctx.right?.accept(this) as? Expr,
        )
    }

    // --- Comparison operations

    override fun visitEqExpr(ctx: LimeParser.EqExprContext): LimeNode? {
        return CompBinOp(
            unit,
            loc(ctx),
            left = ctx.left?.accept(this) as? Expr,
            op = EqOp(unit, loc(ctx.EQ_OP().symbol)),
            right = ctx.right?.accept(this) as? Expr,
        )
    }

    override fun visitNeqExpr(ctx: LimeParser.NeqExprContext): LimeNode? {
        return CompBinOp(
            unit,
            loc(ctx),
            left = ctx.left?.accept(this) as? Expr,
            op = NeqOp(unit, loc(ctx.NEQ().symbol)),
            right = ctx.right?.accept(this) as? Expr,
        )
    }

    override fun visitLtExpr(ctx: LimeParser.LtExprContext): LimeNode? {
        return CompBinOp(
            unit,
            loc(ctx),
            left = ctx.left?.accept(this) as? Expr,
            op = LtOp(unit, loc(ctx.LT().symbol)),
            right = ctx.right?.accept(this) as? Expr,
        )
    }

    override fun visitGtExpr(ctx: LimeParser.GtExprContext): LimeNode? {
        return CompBinOp(
            unit,
            loc(ctx),
            left = ctx.left?.accept(this) as? Expr,
            op = GtOp(unit, loc(ctx.GT().symbol)),
            right = ctx.right?.accept(this) as? Expr,
        )
    }

    override fun visitLeqtExpr(ctx: LimeParser.LeqtExprContext): LimeNode? {
        return CompBinOp(
            unit,
            loc(ctx),
            left = ctx.left?.accept(this) as? Expr,
            op = LeqOp(unit, loc(ctx.LEQ().symbol)),
            right = ctx.right?.accept(this) as? Expr,
        )
    }

    override fun visitGeqExpr(ctx: LimeParser.GeqExprContext): LimeNode? {
        return CompBinOp(
            unit,
            loc(ctx),
            left = ctx.left?.accept(this) as? Expr,
            op = GeqOp(unit, loc(ctx.GEQ().symbol)),
            right = ctx.right?.accept(this) as? Expr,
        )
    }

    // --- Arithmetical operations

    override fun visitUnPlusExpr(ctx: LimeParser.UnPlusExprContext): LimeNode? {
        return ArithUnOp(
            unit,
            loc(ctx),
            op = PlusOp(unit, loc(ctx.PLUS().symbol)),
            operand = ctx.operand?.accept(this) as? Expr,
        )
    }

    override fun visitUnMinusExpr(ctx: LimeParser.UnMinusExprContext): LimeNode? {
        return ArithUnOp(
            unit,
            loc(ctx),
            op = MinusOp(unit, loc(ctx.MINUS().symbol)),
            operand = ctx.operand?.accept(this) as? Expr,
        )
    }

    override fun visitPlusExpr(ctx: LimeParser.PlusExprContext): LimeNode? {
        return ArithBinOp(
            unit,
            loc(ctx),
            left = ctx.left?.accept(this) as? Expr,
            op = PlusOp(unit, loc(ctx.PLUS().symbol)),
            right = ctx.right?.accept(this) as? Expr,
        )
    }

    override fun visitMinusExpr(ctx: LimeParser.MinusExprContext): LimeNode? {
        return ArithBinOp(
            unit,
            loc(ctx),
            left = ctx.left?.accept(this) as? Expr,
            op = MinusOp(unit, loc(ctx.MINUS().symbol)),
            right = ctx.right?.accept(this) as? Expr,
        )
    }

    override fun visitMulExpr(ctx: LimeParser.MulExprContext): LimeNode? {
        return ArithBinOp(
            unit,
            loc(ctx),
            left = ctx.left?.accept(this) as? Expr,
            op = MulOp(unit, loc(ctx.MUL().symbol)),
            right = ctx.right?.accept(this) as? Expr,
        )
    }

    override fun visitDivExpr(ctx: LimeParser.DivExprContext): LimeNode? {
        return ArithBinOp(
            unit,
            loc(ctx),
            left = ctx.left?.accept(this) as? Expr,
            op = DivOp(unit, loc(ctx.DIV().symbol)),
            right = ctx.right?.accept(this) as? Expr,
        )
    }

    // --- Function call

    override fun visitFunCallExpr(ctx: LimeParser.FunCallExprContext): LimeNode? {
        return FunCall(
            unit,
            loc(ctx),
            callee = ctx.callee?.accept(this) as? Expr,
            args = ctx.args?.accept(this) as? ArgList,
        )
    }

    // --- Bracketed expression

    override fun visitBracketExpr(ctx: LimeParser.BracketExprContext): LimeNode? {
        return BracketExpr(
            unit,
            loc(ctx),
            ctx.inner?.accept(this) as? Expr,
        )
    }

    // --- Literals

    override fun visitUnitLiteral(ctx: LimeParser.UnitLiteralContext): LimeNode? {
        return UnitLiteral(unit, loc(ctx))
    }

    override fun visitTrueBooleanLiteral(ctx: LimeParser.TrueBooleanLiteralContext): LimeNode? {
        return BooleanLiteral(unit, loc(ctx), true)
    }

    override fun visitFalseBooleanLiteral(ctx: LimeParser.FalseBooleanLiteralContext): LimeNode? {
        return BooleanLiteral(unit, loc(ctx), false)
    }

    override fun visitIntLiteral(ctx: LimeParser.IntLiteralContext): LimeNode? {
        return IntLiteral(unit, loc(ctx), ctx.text.toBigInteger())
    }

    override fun visitSymbolLiteral(ctx: LimeParser.SymbolLiteralContext): LimeNode? {
        return SymbolLiteral(unit, loc(ctx), ctx.text)
    }

    // --- Parameter

    override fun visitParams(ctx: LimeParser.ParamsContext): LimeNode? {
        val res = ParamList(unit, loc(ctx))
        ctx.parameter().forEach { res.children.add(it.accept(this) as Param) }
        return res
    }

    override fun visitParam(ctx: LimeParser.ParamContext): LimeNode? {
        return Param(
            unit,
            loc(ctx),
            name = id(ctx.name),
            type = id(ctx.type),
            defaultValue = Optional.ofNullable(ctx.defaultValue?.accept(this) as? Expr),
        )
    }

    // --- Argument

    override fun visitArgs(ctx: LimeParser.ArgsContext): LimeNode? {
        val res = ArgList(unit, loc(ctx))
        ctx.argument().forEach { res.children.add(it.accept(this) as Arg) }
        return res
    }

    override fun visitArg(ctx: LimeParser.ArgContext): LimeNode? {
        return Arg(
            unit,
            loc(ctx),
            name = Optional.ofNullable(id(ctx.name)),
            value = ctx.value?.accept(this) as? Expr,
        )
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
        if (token == null || token.text == "<missing ID>") {
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
