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

// Keywords
CONST : 'const' ;
LET : 'let' ;
FUN : 'fun' ;

// Symbols
ID : [a-zA-Z_][a-zA-A_0-9]* ;
INT : [+\-]?[0-9]+ ;

IGNORED_ : [ \t\r\n]+ -> skip;

// --- Parsing

// File top-level
file_module: top_levels EOF # FileModule ;
top_levels:
    # EmptyTopLevel
    | elem=top_level # SingleTopLevel
    | head=top_level tail=top_levels # MultipleTopLevel
    ;
top_level:
      const=const_decl # ConstDeclTopLevel
    | fun=fun_decl # FunDeclTopLevel
    ;

// Declarations
const_decl: CONST name=ID EQ value=expr # ConstDecl ;
var_decl: CONST name=ID EQ value=expr # VarDecl ;
fun_decl: FUN name=ID L_PAREN params=fun_params R_PAREN (ARROW type=ID) body=expr # FunDecl ;

// Expressions
expr:
      L_CURL body=block_exprs R_CURL # BlockExpr
    | var_decl # VarDeclExpr
    | const_decl # ConstDeclExpr
    | fun_decl # FunDeclExpr
    | callee=expr L_PAREN args=fun_args R_PAREN # FunCallExpr
    | literal # LiteralExpr
    ;

// Block expression
block_exprs:
    # EmptyBlockExpr
    | elem=expr SEMI_COLON? # SingleBlockExpr
    | head=expr SEMI_COLON tail=block_exprs # MultipleBlockExpr
    ;

// Literals
literal:
      value=INT # IntLiteral
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
