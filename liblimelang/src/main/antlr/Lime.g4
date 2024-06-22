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

// File top-level compilation unit
compilation_unit: module_elems EOF # CompilationUnit ;

// Generic module parsing
module_elems: module_elem* # ModuleElems ;
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
block_elems: (block_elem SEMI_COLON? | expr SEMI_COLON)* expr SEMI_COLON? # BlockElems ;
block_elem: (block_expr | cond_expr | fun_decl);

// Conditional expression
cond_expr: IF condition=expr thenExpr=block_expr (ELSE elseExpr=block_expr)? # Conditional ;

// Literals
literal:
      L_PAREN R_PAREN # UnitLiteral
    | TRUE # TrueBooleanLiteral
    | FALSE # FalseBooleanLiteral
    | value=INT # IntLiteral
    | id=ID # SymbolLiteral
    ;

// Function parameters and arguments
parameters: (parameter COMMA)* (parameter COMMA?)? # Params ;
parameter: name=ID COLON type=ID # Param ;

arguments: (argument COMMA)* (argument COMMA?)? # Args ;
argument: value=expr # Arg ;
