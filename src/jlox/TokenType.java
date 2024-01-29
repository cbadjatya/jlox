package jlox;


// NOTE that many operators such as += etc are  not part of  the language
public enum TokenType{
	
	// Single character tokens
	PLUS, MINUS, STAR, SLASH, LEFT_PAREN, RIGHT_PAREN, LEFT_BRACE, RIGHT_BRACE, COMMA, DOT, SEMICOLON,
	
	// One or two-character  tokens
	BANG, BANG_EQUAL, EQUAL, GREATER, GREATER_EQUAL, LESS, LESS_EQUAL, EQUAL_EQUAL,
	
	// Keywords
	AND, CLASS, ELSE, FALSE, IF, FOR, WHILE, NIL, RETURN, FUN, OR, PRINT, SUPER, THIS, TRUE, VAR,
	
	// Literals
	STRING, NUMBER, 
	
	//IDENTIFIER
	IDENTIFIER,
	
	EOF
	
}