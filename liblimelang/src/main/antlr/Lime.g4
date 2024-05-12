grammar Lime;

// --- Lexing

// Glyphs
EQ : '=' ;

// Keywords
CONST : 'const' ;

// Symbols
ID : [a-zA-Z_][a-zA-A_0-9]* ;
INT : [+\-]?[0-9]+ ;

IGNORED_ : [ \t\r\n]+ -> skip;

// --- Parsing

module: top_levels EOF # FileModule ;

top_levels:
    # EmptyTopLevel
    | const=const_decl # ConstDeclTopLevel
    ;

const_decl: CONST name=ID EQ value=expr # ConstDecl;

expr:
    value=INT # IntLiteral
    ;
