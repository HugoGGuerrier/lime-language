package com.limelanguage.parser

import org.antlr.v4.kotlinruntime.tree.AbstractParseTreeVisitor
import org.antlr.v4.kotlinruntime.tree.RuleNode

/** This class is the null-safe version of the [LimeBaseVisitor]. It is used to handle nullable AST parts. */
open class LimeSafeBaseVisitor<T> : AbstractParseTreeVisitor<T?>(), LimeVisitor<T?> {
    // ----- Parsing dispatching -----

    override fun visitCompilationUnit(ctx: LimeParser.CompilationUnitContext): T? {
        return visitChildren(ctx)
    }

    override fun visitModuleElems(ctx: LimeParser.ModuleElemsContext): T? {
        return visitChildren(ctx)
    }

    override fun visitModule_elem(ctx: LimeParser.Module_elemContext): T? {
        return visitChildren(ctx)
    }

    override fun visitExpr(ctx: LimeParser.ExprContext): T? {
        return visitChildren(ctx)
    }

    override fun visitFunCallExpr(ctx: LimeParser.FunCallExprContext): T? {
        return visitChildren(ctx)
    }

    override fun visitBracketExpr(ctx: LimeParser.BracketExprContext): T? {
        return visitChildren(ctx)
    }

    override fun visitLiteralExpr(ctx: LimeParser.LiteralExprContext): T? {
        return visitChildren(ctx)
    }

    override fun visitConditionalExpr(ctx: LimeParser.ConditionalExprContext): T? {
        return visitChildren(ctx)
    }

    override fun visitBlockExpr(ctx: LimeParser.BlockExprContext): T? {
        return visitChildren(ctx)
    }

    override fun visitConstDeclExpr(ctx: LimeParser.ConstDeclExprContext): T? {
        return visitChildren(ctx)
    }

    override fun visitVarDeclExpr(ctx: LimeParser.VarDeclExprContext): T? {
        return visitChildren(ctx)
    }

    override fun visitVarAffectExpr(ctx: LimeParser.VarAffectExprContext): T? {
        return visitChildren(ctx)
    }

    override fun visitLogicExpr(ctx: LimeParser.LogicExprContext): T? {
        return visitChildren(ctx)
    }

    override fun visitLogicUnopExpr(ctx: LimeParser.LogicUnopExprContext): T? {
        return visitChildren(ctx)
    }

    override fun visitAndExpr(ctx: LimeParser.AndExprContext): T? {
        return visitChildren(ctx)
    }

    override fun visitOrExpr(ctx: LimeParser.OrExprContext): T? {
        return visitChildren(ctx)
    }

    override fun visitNotExpr(ctx: LimeParser.NotExprContext): T? {
        return visitChildren(ctx)
    }

    override fun visitCompExpr(ctx: LimeParser.CompExprContext): T? {
        return visitChildren(ctx)
    }

    override fun visitGeqExpr(ctx: LimeParser.GeqExprContext): T? {
        return visitChildren(ctx)
    }

    override fun visitEqExpr(ctx: LimeParser.EqExprContext): T? {
        return visitChildren(ctx)
    }

    override fun visitNeqExpr(ctx: LimeParser.NeqExprContext): T? {
        return visitChildren(ctx)
    }

    override fun visitLeqtExpr(ctx: LimeParser.LeqtExprContext): T? {
        return visitChildren(ctx)
    }

    override fun visitLtExpr(ctx: LimeParser.LtExprContext): T? {
        return visitChildren(ctx)
    }

    override fun visitGtExpr(ctx: LimeParser.GtExprContext): T? {
        return visitChildren(ctx)
    }

    override fun visitSumExpr(ctx: LimeParser.SumExprContext): T? {
        return visitChildren(ctx)
    }

    override fun visitPlusExpr(ctx: LimeParser.PlusExprContext): T? {
        return visitChildren(ctx)
    }

    override fun visitProdExpr(ctx: LimeParser.ProdExprContext): T? {
        return visitChildren(ctx)
    }

    override fun visitMinusExpr(ctx: LimeParser.MinusExprContext): T? {
        return visitChildren(ctx)
    }

    override fun visitMulExpr(ctx: LimeParser.MulExprContext): T? {
        return visitChildren(ctx)
    }

    override fun visitValueExpr(ctx: LimeParser.ValueExprContext): T? {
        return visitChildren(ctx)
    }

    override fun visitBoundedExpr(ctx: LimeParser.BoundedExprContext): T? {
        return visitChildren(ctx)
    }

    override fun visitDivExpr(ctx: LimeParser.DivExprContext): T? {
        return visitChildren(ctx)
    }

    override fun visitArithUnopExpr(ctx: LimeParser.ArithUnopExprContext): T? {
        return visitChildren(ctx)
    }

    override fun visitUnPlusExpr(ctx: LimeParser.UnPlusExprContext): T? {
        return visitChildren(ctx)
    }

    override fun visitUnMinusExpr(ctx: LimeParser.UnMinusExprContext): T? {
        return visitChildren(ctx)
    }

    override fun visitUnitLiteral(ctx: LimeParser.UnitLiteralContext): T? {
        return visitChildren(ctx)
    }

    override fun visitTrueBooleanLiteral(ctx: LimeParser.TrueBooleanLiteralContext): T? {
        return visitChildren(ctx)
    }

    override fun visitFalseBooleanLiteral(ctx: LimeParser.FalseBooleanLiteralContext): T? {
        return visitChildren(ctx)
    }

    override fun visitIntLiteral(ctx: LimeParser.IntLiteralContext): T? {
        return visitChildren(ctx)
    }

    override fun visitSymbolLiteral(ctx: LimeParser.SymbolLiteralContext): T? {
        return visitChildren(ctx)
    }

    override fun visitBlock(ctx: LimeParser.BlockContext): T? {
        return visitChildren(ctx)
    }

    override fun visitBlockElem(ctx: LimeParser.BlockElemContext): T? {
        return visitChildren(ctx)
    }

    override fun visitVarDecl(ctx: LimeParser.VarDeclContext): T? {
        return visitChildren(ctx)
    }

    override fun visitVarAffect(ctx: LimeParser.VarAffectContext): T? {
        return visitChildren(ctx)
    }

    override fun visitConstDecl(ctx: LimeParser.ConstDeclContext): T? {
        return visitChildren(ctx)
    }

    override fun visitFunDecl(ctx: LimeParser.FunDeclContext): T? {
        return visitChildren(ctx)
    }

    override fun visitParams(ctx: LimeParser.ParamsContext): T? {
        return visitChildren(ctx)
    }

    override fun visitParam(ctx: LimeParser.ParamContext): T? {
        return visitChildren(ctx)
    }

    override fun visitArgs(ctx: LimeParser.ArgsContext): T? {
        return visitChildren(ctx)
    }

    override fun visitArg(ctx: LimeParser.ArgContext): T? {
        return visitChildren(ctx)
    }

    // ----- Special visiting methods -----

    override fun visitChildren(node: RuleNode): T? {
        var res: T? = defaultResult()
        for (i in 0..<node.childCount) {
            val child = node.getChild(i)
            res = aggregateResult(res, child?.accept(this))
        }
        return res
    }
}
