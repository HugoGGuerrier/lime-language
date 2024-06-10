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
UNIT : '()' ;

// Keywords
TRUE : 'true' ;
FALSE : 'false' ;
IF : 'if' ;
ELSE : 'else' ;
FUN : 'fun' ;
CONST : 'const' ;
LET : 'let' ;

// Symbols
ID : [a-zA-Z_][a-zA-A_0-9]* ;
INT : [+\-]?[0-9]+ ;

IGNORED : [ \t\r\n]+ -> skip ;
LINE_COMMENT : '//' ~('\n'|'\r')* '\r'? '\n' -> channel(1) ;
MULTI_COMMENT : '/*' .*? '*/' -> channel(1) ;

// --- Parsing

// File top-level module
file_module: module_elems EOF # FileModule ;

// Generic module parsing
module_elems:
    # EmptyModuleElem
    | elem=module_elem # SingleModuleElem
    | head=module_elem tail=module_elems # MultipleModuleElem
    ;
module_elem: const_decl | fun_decl ;

// Declarations/Affectations
fun_decl: FUN name=ID L_PAREN params=parameters R_PAREN (ARROW type=ID)? body=block_expr # FunDecl ;
const_decl: CONST name=ID (COLON type=ID)? EQ value=expr # ConstDecl ;
var_decl: LET name=ID (COLON type=ID)? (EQ value=expr)? # VarDecl ;
var_affect: name=ID EQ value=expr # VarAffect ;

// Expressions
expr:
      literal # LiteralExpr
    | L_PAREN inner=expr R_PAREN # BracketExpr
    | block_expr # BlockExpr
    | callee=expr L_PAREN args=arguments R_PAREN # FunCallExpr
    | cond_expr # ConditionalExpr
    | var_decl # VarDeclExpr
    | var_affect # VarAffectExpr
    | const_decl # ConstDeclExpr
    | fun_decl # FunDeclExpr
    ;

// Block expression
block_expr: L_CURL elems=block_elems R_CURL # Block ;
block_elems:
    # EmptyBlockElem
    | elem=expr SEMI_COLON? # SingleBlockElem
    | head=block_elem tail=block_elems # MultipleBlockElem
    ;
block_elem:
      block_expr SEMI_COLON?
    | cond_expr SEMI_COLON?
    | fun_decl SEMI_COLON?
    | expr SEMI_COLON
    ;

// Conditional expression
cond_expr: IF condition=expr thenExpr=block_expr (ELSE elseExpr=block_expr)? # Conditional ;

// Literals
literal:
      UNIT # UnitLiteral
    | TRUE # TrueBooleanLiteral
    | FALSE # FalseBooleanLiteral
    | value=INT # IntLiteral
    | id=ID # SymbolLiteral
    ;

// Function parameters and arguments
parameters:
    # EmptyParam
    | param=parameter COMMA? # SingleParam
    | head=parameter COMMA tail=parameters # MultipleParam
    ;
parameter: name=ID COLON type=ID # Param ;

arguments:
    # EmptyArg
    | arg=argument COMMA? # SingleArg
    | head=argument COMMA tail=arguments # MultipleArg
    ;
argument: value=expr # Arg ;
