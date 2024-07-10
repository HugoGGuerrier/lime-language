grammar Lime;

// --- Lexing

// Glyphs
COMMA : ',' ;
DOT : '.' ;
L_PAREN : '(' ;
R_PAREN : ')' ;
L_CURL : '{' ;
R_CURL : '}' ;
ARROW : '->' ;
COLON : ':' ;
SEMI_COLON : ';' ;
EQ : '=' ;

// Operators
PLUS : '+' ;
MINUS : '-' ;
MUL : '*' ;
DIV : '/' ;
AND : '&&' ;
OR : '||' ;
NOT : '!' ;
EQ_OP : '==' ;
NEQ : '!=' ;
LT : '<' ;
GT : '>' ;
LEQ : '<=' ;
GEQ : '>=' ;

// Keywords
TRUE : 'true' ;
FALSE : 'false' ;
IF : 'if' ;
ELSE : 'else' ;
FUN : 'fun' ;
CONST : 'const' ;
VAR : 'var' ;

// Symbols
ID : [a-zA-Z_][a-zA-A_0-9]* ;
INT : [0-9]+ ;

IGNORED : [ \t\r\n]+ -> skip ;
LINE_COMMENT : '//' ~('\n'|'\r')* '\r'? '\n' -> channel(1) ;
MULTI_COMMENT : '/*' .*? '*/' -> channel(1) ;

// --- Parsing

// File top-level compilation unit
compilation_unit: module_elems EOF # CompilationUnit ;

// Module
module_elems: module_elem* # ModuleElems ;
module_elem: const_decl | fun_decl ;

// Declarations/Affectations
var_decl: VAR name=ID (COLON type=type_expr)? (EQ value=expr)? # VarDecl ;
var_affect: name=ID EQ value=expr # VarAffect ;
const_decl: CONST name=ID (COLON type=type_expr)? EQ value=expr # ConstDecl ;
fun_decl: FUN name=ID L_PAREN params=parameters R_PAREN (ARROW type=type_expr)? body=block_expr # FunDecl ;

// Expressions
expr: fun_decl | decl_expr ;

decl_expr:
      const_decl # ConstDeclExpr
    | var_decl # VarDeclExpr
    | var_affect # VarAffectExpr
    | logic_expr # LogicExpr
    ;

logic_expr:
      left=logic_expr AND right=logic_unop_expr # AndExpr
    | left=logic_expr OR right=logic_unop_expr # OrExpr
    | logic_unop_expr # LogicUnopExpr
    ;

logic_unop_expr:
      NOT operand=comp_expr # NotExpr
    | comp_expr # CompExpr
    ;

comp_expr:
      left=comp_expr EQ_OP right=sum_expr # EqExpr
    | left=comp_expr NEQ right=sum_expr # NeqExpr
    | left=comp_expr LT right=sum_expr # LtExpr
    | left=comp_expr GT right=sum_expr # GtExpr
    | left=comp_expr LEQ right=sum_expr # LeqtExpr
    | left=comp_expr GEQ right=sum_expr # GeqExpr
    | sum_expr # SumExpr
    ;

sum_expr:
      left=sum_expr PLUS right=prod_expr # PlusExpr
    | left=sum_expr MINUS right=prod_expr # MinusExpr
    | prod_expr # ProdExpr
    ;

prod_expr:
      left=prod_expr MUL right=arith_unop_expr # MulExpr
    | left=prod_expr DIV right=arith_unop_expr # DivExpr
    | arith_unop_expr # ArithUnopExpr
    ;

arith_unop_expr:
      PLUS operand=value_expr # UnPlusExpr
    | MINUS operand=value_expr # UnMinusExpr
    | value_expr # ValueExpr
    ;

value_expr:
      callee=value_expr L_PAREN args=arguments R_PAREN # FunCallExpr
    | L_PAREN inner=expr R_PAREN # BracketExpr
    | bounded_expr # BoundedExpr
    | literal # LiteralExpr
    ;

bounded_expr:
     IF condition=expr thenExpr=block_expr (ELSE elseExpr=block_expr)? # ConditionalExpr
    | block_expr # BlockExpr
    ;

literal:
      L_PAREN R_PAREN # UnitLiteral
    | TRUE # TrueBooleanLiteral
    | FALSE # FalseBooleanLiteral
    | value=INT # IntLiteral
    | ID # SymbolLiteral
    ;

// Block expression
block_expr: L_CURL block_elem* value=expr? R_CURL # Block ;
block_elem: (fdelem=fun_decl | belem=bounded_expr | uelem=decl_expr SEMI_COLON | SEMI_COLON) # BlockElem ;

// Parameters and arguments
parameters: (parameter COMMA)* (parameter COMMA?)? # Params ;
parameter: name=ID COLON type=type_expr (EQ defaultValue=expr)? # Param ;

arguments: (argument COMMA)* (argument COMMA?)? # Args ;
argument: (name=ID EQ)? value=expr # Arg ;

// Type expressions
type_expr:
      ID # SymbolType
    | L_PAREN param_types=type_exprs R_PAREN ARROW return_type=type_expr # FunType
    ;
type_exprs: (type_expr COMMA)* (type_expr COMMA?)? # TypeExprs ;
