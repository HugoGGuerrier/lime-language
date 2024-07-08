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
var_decl: VAR name=ID (COLON type=ID)? (EQ value=expr)? # VarDecl ;
var_affect: name=ID EQ value=expr # VarAffect ;
const_decl: CONST name=ID (COLON type=ID)? EQ value=expr # ConstDecl ;
fun_decl: FUN name=ID L_PAREN params=parameters R_PAREN (ARROW type=ID)? body=block_expr # FunDecl ;

// Expressions
expr: fun_decl | decl_expr ;

decl_expr:
      const_decl # ConstDeclExpr
    | var_decl # VarDeclExpr
    | var_affect # VarAffectExpr
    | sum_expr # SumExpr
    ;

sum_expr:
      left=sum_expr PLUS right=prod_expr # PlusExpr
    | left=sum_expr MINUS right=prod_expr # MinusExpr
    | prod_expr # ProdExpr
    ;

prod_expr:
      left=prod_expr MUL right=arith_unop # MulExpr
    | left=prod_expr DIV right=arith_unop # DivExpr
    | arith_unop # ArithUnopExpr
    ;

arith_unop:
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
    | id=ID # SymbolLiteral
    ;

// Block expression
block_expr: L_CURL block_elem* value=expr? R_CURL # Block ;
block_elem: (fdelem=fun_decl | belem=bounded_expr | uelem=decl_expr SEMI_COLON | SEMI_COLON) # BlockElem ;

// Parameters and arguments
parameters: (parameter COMMA)* (parameter COMMA?)? # Params ;
parameter: name=ID COLON type=ID (EQ defaultValue=expr)? # Param ;

arguments: (argument COMMA)* (argument COMMA?)? # Args ;
argument: (name=ID EQ)? value=expr # Arg ;
