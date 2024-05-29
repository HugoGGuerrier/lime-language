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
CONST : 'const' ;
LET : 'let' ;
FUN : 'fun' ;

// Symbols
ID : [a-zA-Z_][a-zA-A_0-9]* ;
INT : [+\-]?[0-9]+ ;

IGNORED_ : [ \t\r\n]+ -> skip;

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
var_decl: LET name=ID (COLON type=ID)? (EQ value=expr)? # VarDecl ;
var_affect: name=ID EQ value=expr # VarAffect ;
const_decl: CONST name=ID (COLON type=ID)? EQ value=expr? # ConstDecl ;
fun_decl: FUN name=ID L_PAREN params=fun_params R_PAREN (ARROW type=ID)? body=block_expr # FunDecl ;

// Expressions
expr:
     literal # LiteralExpr
    | L_PAREN inner=expr R_PAREN # BracketExpr
    | callee=expr L_PAREN args=fun_args R_PAREN # FunCallExpr
    | block_expr # BlockExpr
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
    | head=expr SEMI_COLON tail=block_elems # MultipleBlockElem
    ;

// Literals
literal:
      UNIT # UnitLiteral
    | value=INT # IntLiteral
    | id=ID # SymbolLiteral
    ;

// Function parameters and arguments
fun_params:
    # EmptyFunParam
    | param=fun_param COMMA? # SingleFunParam
    | head=fun_param COMMA tail=fun_params # MultipleFunParam
    ;
fun_param: name=ID COLON type=ID # FunParam ;

fun_args:
    # EmptyFunArg
    | arg=expr COMMA? # SingleFunArg
    | head=expr COMMA tail=fun_args # MultipleFunArg
    ;
